package io.github.avinashio.lazyspringboot.infrastructure.maven;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;

class MavenProjectInspectorTest {

    private final MavenProjectInspector inspector = new MavenProjectInspector(
            new MavenDependencyParser());

    @TempDir
    Path temporaryDirectory;

    @Test
    void shouldIdentifySpringBootProject() throws IOException {
        Path pomFile = temporaryDirectory.resolve("pom.xml");

        Files.writeString(
                pomFile,
                """
                <project>
                  <parent>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-parent</artifactId>
                  </parent>
                </project>
                """);

        assertThat(inspector.isSpringBootProject(pomFile)).isTrue();
    }

    @Test
    void shouldRejectPlainMavenProject() throws IOException {
        Path pomFile = temporaryDirectory.resolve("pom.xml");

        Files.writeString(
                pomFile,
                """
                <project>
                  <groupId>com.example</groupId>
                  <artifactId>demo</artifactId>
                </project>
                """);

        assertThat(inspector.isSpringBootProject(pomFile)).isFalse();
    }

    @Test
    void shouldRejectMalformedPomFile() throws IOException {
        Path pomFile = temporaryDirectory.resolve("pom.xml");

        Files.writeString(pomFile, "<project>");

        assertThat(inspector.isSpringBootProject(pomFile)).isFalse();
    }

    @Test
    void shouldRejectPomContainingDoctypeDeclaration()
            throws IOException {
        Path pomFile = temporaryDirectory.resolve("pom.xml");

        Files.writeString(
                pomFile,
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE project [
                  <!ENTITY external SYSTEM "file:///etc/passwd">
                ]>
                <project>
                  <groupId>&external;</groupId>
                </project>
                """);

        assertThat(inspector.isSpringBootProject(pomFile)).isFalse();
    }

    @Test
    void shouldIgnoreSpringBootGroupIdInsideComment()
            throws IOException {
        Path pomFile = temporaryDirectory.resolve("pom.xml");

        Files.writeString(
                pomFile,
                """
                <project>
                  <!-- org.springframework.boot -->
                  <groupId>com.example</groupId>
                  <artifactId>demo</artifactId>
                </project>
                """);

        assertThat(inspector.isSpringBootProject(pomFile)).isFalse();
    }

    @Test
    void shouldExtractMavenProjectMetadata() throws IOException {
        Path pomFile = temporaryDirectory.resolve("pom.xml");

        Files.writeString(
                pomFile,
                """
                <project>
                  <parent>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-parent</artifactId>
                    <version>4.1.0</version>
                  </parent>
          
                  <groupId>com.example</groupId>
                  <artifactId>testboot</artifactId>
          
                <dependencies>
                  <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-web</artifactId>
                  </dependency>
                
                  <dependency>
                    <groupId>org.projectlombok</groupId>
                    <artifactId>lombok</artifactId>
                  </dependency>
                </dependencies>                    
          
                  <properties>
                    <java.version>26</java.version>
                  </properties>
                </project>
                """);

        MavenProjectMetadata metadata = inspector.inspect(pomFile);

        assertThat(metadata.groupId()).isEqualTo("com.example");
        assertThat(metadata.artifactId()).isEqualTo("testboot");
        assertThat(metadata.springBootVersion()).isEqualTo("4.1.0");
        assertThat(metadata.javaVersion()).isEqualTo("26");
        assertThat(metadata.dependencies())
                .containsExactly(
                        new DependencyCoordinate(
                                "org.springframework.boot",
                                "spring-boot-starter-web"),
                        new DependencyCoordinate(
                                "org.projectlombok",
                                "lombok"));
    }

    @Test
    void shouldIgnoreDependencyManagementEntries()
            throws IOException {
        Path pomFile = temporaryDirectory.resolve("pom.xml");

        Files.writeString(
                pomFile,
                """
                <project>
                  <parent>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-parent</artifactId>
                    <version>4.1.0</version>
                  </parent>
          
                  <groupId>com.example</groupId>
                  <artifactId>testboot</artifactId>
          
                  <dependencyManagement>
                    <dependencies>
                      <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-web</artifactId>
                      </dependency>
                    </dependencies>
                  </dependencyManagement>
          
                  <dependencies>
                    <dependency>
                      <groupId>org.projectlombok</groupId>
                      <artifactId>lombok</artifactId>
                    </dependency>
                  </dependencies>
                </project>
                """);

        MavenProjectMetadata metadata =
                inspector.inspect(pomFile);

        assertThat(metadata.dependencies())
                .containsExactly(
                        new DependencyCoordinate(
                                "org.projectlombok",
                                "lombok"));
    }

    @Test
    void shouldReturnEmptyDependenciesWhenPomHasNoDependencies()
            throws IOException {
        Path pomFile = temporaryDirectory.resolve("pom.xml");

        Files.writeString(
                pomFile,
                """
                <project>
                  <parent>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-parent</artifactId>
                    <version>4.1.0</version>
                  </parent>
          
                  <groupId>com.example</groupId>
                  <artifactId>testboot</artifactId>
                </project>
                """);

        MavenProjectMetadata metadata =
                inspector.inspect(pomFile);

        assertThat(metadata.dependencies()).isEmpty();
    }
}