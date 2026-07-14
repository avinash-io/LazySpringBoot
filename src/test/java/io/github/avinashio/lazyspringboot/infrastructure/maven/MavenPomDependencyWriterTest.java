package io.github.avinashio.lazyspringboot.infrastructure.maven;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MavenPomDependencyWriterTest {

    private final MavenPomDependencyWriter writer =
            new MavenPomDependencyWriter(
                    new MavenDependencyParser());

    @TempDir
    Path temporaryDirectory;

    @Test
    void shouldAddDependencyToExistingDependencies()
            throws Exception {
        Path pomPath =
                writePom(
                        """
                        <project>
                          <dependencies>
                            <dependency>
                              <groupId>org.projectlombok</groupId>
                              <artifactId>lombok</artifactId>
                            </dependency>
                          </dependencies>
                        </project>
                        """);

        writer.addDependencies(
                pomPath,
                List.of(
                        coordinate(
                                "org.postgresql",
                                "postgresql")));

        String pom =
                Files.readString(pomPath);

        assertThat(pom)
                .contains(
                        "<groupId>org.postgresql</groupId>")
                .contains(
                        "<artifactId>postgresql</artifactId>");
    }

    @Test
    void shouldCreateDependenciesSectionWhenMissing()
            throws Exception {
        Path pomPath =
                writePom(
                        """
                        <project>
                          <groupId>com.example</groupId>
                          <artifactId>demo</artifactId>
                        </project>
                        """);

        writer.addDependencies(
                pomPath,
                List.of(
                        coordinate(
                                "org.postgresql",
                                "postgresql")));

        String pom =
                Files.readString(pomPath);

        assertThat(pom)
                .contains("<dependencies>")
                .contains(
                        "<groupId>org.postgresql</groupId>")
                .contains("</dependencies>");
    }

    @Test
    void shouldNotAddDuplicateDependency()
            throws Exception {
        Path pomPath =
                writePom(
                        """
                        <project>
                          <dependencies>
                            <dependency>
                              <groupId>org.postgresql</groupId>
                              <artifactId>postgresql</artifactId>
                            </dependency>
                          </dependencies>
                        </project>
                        """);

        writer.addDependencies(
                pomPath,
                List.of(
                        coordinate(
                                "org.postgresql",
                                "postgresql")));

        String pom =
                Files.readString(pomPath);

        assertThat(
                countOccurrences(
                        pom,
                        "<artifactId>postgresql</artifactId>"))
                .isEqualTo(1);
    }

    @Test
    void shouldUseExactCoordinateForDuplicateDetection()
            throws Exception {
        Path pomPath =
                writePom(
                        """
                        <project>
                          <dependencies>
                            <dependency>
                              <groupId>com.example</groupId>
                              <artifactId>first</artifactId>
                            </dependency>
                            <dependency>
                              <groupId>org.example</groupId>
                              <artifactId>demo</artifactId>
                            </dependency>
                          </dependencies>
                        </project>
                        """);

        DependencyCoordinate expected =
                coordinate(
                        "com.example",
                        "demo");

        writer.addDependencies(
                pomPath,
                List.of(expected));

        try (var inputStream =
                     Files.newInputStream(pomPath)) {
            assertThat(
                    new MavenDependencyParser()
                            .parse(inputStream))
                    .contains(expected);
        }
    }

    @Test
    void shouldCreateBackupBeforeReplacingPom()
            throws Exception {
        Path pomPath =
                writePom(
                        """
                        <project>
                        </project>
                        """);

        String original =
                Files.readString(pomPath);

        writer.addDependencies(
                pomPath,
                List.of(
                        coordinate(
                                "org.postgresql",
                                "postgresql")));

        Path backupPath =
                temporaryDirectory.resolve(
                        "pom.xml.lazyspringboot.bak");

        assertThat(backupPath)
                .exists();

        assertThat(
                Files.readString(backupPath))
                .isEqualTo(original);
    }

    @Test
    void shouldDeleteTemporaryFileAfterSuccess()
            throws Exception {
        Path pomPath =
                writePom(
                        """
                        <project>
                        </project>
                        """);

        writer.addDependencies(
                pomPath,
                List.of(
                        coordinate(
                                "org.postgresql",
                                "postgresql")));

        assertThat(
                temporaryDirectory.resolve(
                        "pom.xml.lazyspringboot.tmp"))
                .doesNotExist();
    }

    @Test
    void shouldPreserveExistingPomContent()
            throws Exception {
        Path pomPath =
                writePom(
                        """
                        <project>
                          <groupId>com.example</groupId>
                          <artifactId>demo</artifactId>
                          <version>1.0.0</version>
                        </project>
                        """);

        writer.addDependencies(
                pomPath,
                List.of(
                        coordinate(
                                "org.postgresql",
                                "postgresql")));

        String pom =
                Files.readString(pomPath);

        assertThat(pom)
                .contains(
                        "<groupId>com.example</groupId>")
                .contains(
                        "<artifactId>demo</artifactId>")
                .contains(
                        "<version>1.0.0</version>");
    }

    private Path writePom(String content)
            throws Exception {
        Path pomPath =
                temporaryDirectory.resolve("pom.xml");

        Files.writeString(
                pomPath,
                content);

        return pomPath;
    }

    private DependencyCoordinate coordinate(
            String groupId,
            String artifactId) {
        return new DependencyCoordinate(
                groupId,
                artifactId);
    }

    private int countOccurrences(
            String value,
            String target) {
        return value
                .split(
                        java.util.regex.Pattern.quote(target),
                        -1)
                .length
                - 1;
    }

    @Test
    void shouldAddDependencyToNamespacedMavenPom()
            throws Exception {
        Path pomPath =
                writePom(
                        """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0"
                            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                          <modelVersion>4.0.0</modelVersion>
              
                          <groupId>com.example</groupId>
                          <artifactId>demo</artifactId>
              
                          <dependencies>
                          </dependencies>
                        </project>
                        """);

        DependencyCoordinate dependency =
                coordinate(
                        "org.postgresql",
                        "postgresql");

        writer.addDependencies(
                pomPath,
                List.of(dependency));

        try (var inputStream =
                     Files.newInputStream(pomPath)) {
            assertThat(
                    new MavenDependencyParser()
                            .parse(inputStream))
                    .contains(dependency);
        }
    }

    @Test
    void shouldRejectPomWithoutProjectClosingTag()
            throws Exception {
        Path pomPath =
                writePom(
                        """
                        <project>
                          <groupId>com.example</groupId>
                        """);

        String original =
                Files.readString(pomPath);

        org.assertj.core.api.Assertions
                .assertThatThrownBy(
                        () ->
                                writer.addDependencies(
                                        pomPath,
                                        List.of(
                                                coordinate(
                                                        "org.postgresql",
                                                        "postgresql"))))
                .isInstanceOf(java.io.IOException.class)
                .hasMessageContaining(
                        "Invalid Maven POM");

        assertThat(
                Files.readString(pomPath))
                .isEqualTo(original);
    }

    @Test
    void shouldRejectMalformedPomWithoutModifyingFile()
            throws Exception {
        Path pomPath =
                writePom(
                        """
                        <project>
                          <dependencies>
                            <dependency>
                              <groupId>org.postgresql</groupId>
                            </wrong-element>
                          </dependencies>
                        </project>
                        """);

        String original =
                Files.readString(pomPath);

        assertThatThrownBy(
                () ->
                        writer.addDependencies(
                                pomPath,
                                List.of(
                                        coordinate(
                                                "org.postgresql",
                                                "postgresql"))))
                .isInstanceOf(java.io.IOException.class)
                .hasMessageContaining(
                        "Failed to parse Maven dependencies");

        assertThat(
                Files.readString(pomPath))
                .isEqualTo(original);
    }
}