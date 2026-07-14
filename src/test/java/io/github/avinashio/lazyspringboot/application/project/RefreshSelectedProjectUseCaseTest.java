package io.github.avinashio.lazyspringboot.application.project;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenDependencyParser;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenProjectInspector;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RefreshSelectedProjectUseCaseTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void shouldRefreshProjectFromPom()
            throws Exception {
        Path pomPath =
                temporaryDirectory.resolve("pom.xml");

        Files.writeString(
                pomPath,
                """
                <project>
                  <modelVersion>4.0.0</modelVersion>
                  <groupId>com.example</groupId>
                  <artifactId>demo</artifactId>
                  <version>1.0.0</version>
                </project>
                """);

        MavenProjectInspector inspector =
                new MavenProjectInspector(
                        new MavenDependencyParser());

        RefreshSelectedProjectUseCase useCase =
                new RefreshSelectedProjectUseCase(
                        inspector);

        SpringProject original =
                new SpringProject(
                        "demo",
                        temporaryDirectory,
                        new ProjectMetadata(
                                "com.example",
                                "demo",
                                null,
                                null,
                                BuildTool.MAVEN,
                                List.of()));

        Files.writeString(
                pomPath,
                """
                <project>
                  <modelVersion>4.0.0</modelVersion>
                  <groupId>com.example</groupId>
                  <artifactId>demo-updated</artifactId>
                  <version>1.0.0</version>
                </project>
                """);

        SpringProject refreshed =
                useCase.refresh(original);

        assertThat(
                refreshed.metadata().artifactId())
                .isEqualTo("demo-updated");
    }
}