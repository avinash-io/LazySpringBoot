package io.github.avinashio.lazyspringboot.application.dependency;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyAvailability;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenDependencyParser;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenPomDependencyWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;


class ApplyDependenciesUseCaseTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void shouldApplySelectedDependenciesToPom()
            throws Exception {
        Path pomPath =
                temporaryDirectory.resolve("pom.xml");

        Files.writeString(
                pomPath,
                """
                <project>
                  <groupId>com.example</groupId>
                  <artifactId>demo</artifactId>
                </project>
                """);

        ApplyDependenciesUseCase useCase =
                new ApplyDependenciesUseCase(
                        new DependencyCoordinateResolver(),
                        new MavenPomDependencyWriter(
                                new MavenDependencyParser()));

        useCase.apply(
                project(),
                List.of(
                        selectedDependency(
                                "postgresql"),
                        selectedDependency(
                                "data-jpa")));

        try (var inputStream =
                     Files.newInputStream(pomPath)) {
            assertThat(
                    new MavenDependencyParser()
                            .parse(inputStream))
                    .contains(
                            new DependencyCoordinate(
                                    "org.postgresql",
                                    "postgresql"),
                            new DependencyCoordinate(
                                    "org.springframework.boot",
                                    "spring-boot-starter-data-jpa"));
        }
    }

    @Test
    void shouldIgnoreAlreadyPresentDependencyItems()
            throws Exception {
        Path pomPath =
                temporaryDirectory.resolve("pom.xml");

        String original =
                """
                <project>
                  <dependencies>
                    <dependency>
                      <groupId>org.projectlombok</groupId>
                      <artifactId>lombok</artifactId>
                    </dependency>
                  </dependencies>
                </project>
                """;

        Files.writeString(
                pomPath,
                original);

        ApplyDependenciesUseCase useCase =
                new ApplyDependenciesUseCase(
                        new DependencyCoordinateResolver(),
                        new MavenPomDependencyWriter(
                                new MavenDependencyParser()));

        useCase.apply(
                project(),
                List.of(
                        new DependencyItem(
                                dependency("lombok"),
                                DependencyAvailability.ALREADY_PRESENT,
                                false)));

        assertThat(
                Files.readString(pomPath))
                .isEqualTo(original);
    }

    private DependencyItem selectedDependency(
            String id) {
        return new DependencyItem(
                dependency(id),
                DependencyAvailability.AVAILABLE,
                true);
    }

    private SpringDependency dependency(
            String id) {
        return new SpringDependency(
                id,
                id,
                "Test dependency",
                "Test");
    }

    private SpringProject project() {
        return new SpringProject(
                "demo",
                temporaryDirectory,
                new ProjectMetadata(
                        "com.example",
                        "demo",
                        "4.1.0",
                        "26",
                        BuildTool.MAVEN,
                        List.of()));
    }

    @Test
    void shouldIgnoreUnselectedAvailableDependencies()
            throws Exception {
        Path pomPath =
                temporaryDirectory.resolve("pom.xml");

        String original =
                """
                <project>
                </project>
                """;

        Files.writeString(
                pomPath,
                original);

        ApplyDependenciesUseCase useCase =
                new ApplyDependenciesUseCase(
                        new DependencyCoordinateResolver(),
                        new MavenPomDependencyWriter(
                                new MavenDependencyParser()));

        useCase.apply(
                project(),
                List.of(
                        new DependencyItem(
                                dependency("postgresql"),
                                DependencyAvailability.AVAILABLE,
                                false)));

        assertThat(
                Files.readString(pomPath))
                .isEqualTo(original);
    }
}