package io.github.avinashio.lazyspringboot.infrastructure.process;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ProjectProcessCommandFactoryTest {

    private final ProjectProcessCommandFactory
            commandFactory =
            new ProjectProcessCommandFactory();

    @TempDir
    Path temporaryDirectory;

    @Test
    void shouldUseMavenWrapperWhenAvailable()
            throws IOException {
        Files.createFile(
                temporaryDirectory.resolve("mvnw"));

        SpringProject project =
                mockProject();

        List<String> command =
                commandFactory.create(project);

        assertThat(command)
                .containsExactly(
                        "./mvnw",
                        "spring-boot:run");
    }

    @Test
    void shouldUseSystemMavenWhenWrapperIsMissing() {
        SpringProject project =
                mockProject();

        List<String> command =
                commandFactory.create(project);

        assertThat(command)
                .containsExactly(
                        "mvn",
                        "spring-boot:run");
    }

    private SpringProject mockProject() {
        SpringProject project =
                mock(SpringProject.class);

        ProjectMetadata metadata =
                mock(ProjectMetadata.class);

        when(project.metadata())
                .thenReturn(metadata);

        when(metadata.buildTool())
                .thenReturn(BuildTool.MAVEN);

        when(project.path())
                .thenReturn(temporaryDirectory);

        return project;
    }
}