package io.github.avinashio.lazyspringboot.infrastructure.maven;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MavenProjectInspectorTest {

    private final MavenProjectInspector inspector = new MavenProjectInspector();

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
}