package io.github.avinashio.lazyspringboot.application.action;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectCommand;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ProjectCommandResolverTest {

    @TempDir
    Path temporaryDirectory;

    private final ProjectCommandResolver resolver =
            new ProjectCommandResolver();

    @Test
    void shouldResolveMavenBuildCommand()
            throws Exception {
        Files.createFile(
                temporaryDirectory.resolve("mvnw"));

        ProjectCommand command =
                resolver.resolve(
                        project(),
                        ProjectAction.BUILD);

        assertThat(command.arguments())
                .containsExactly(
                        temporaryDirectory
                                .resolve("mvnw")
                                .toString(),
                        "clean",
                        "package");

        assertThat(command.workingDirectory())
                .isEqualTo(
                        temporaryDirectory);
    }

    @Test
    void shouldResolveMavenTestCommand()
            throws Exception {
        Files.createFile(
                temporaryDirectory.resolve("mvnw"));

        ProjectCommand command =
                resolver.resolve(
                        project(),
                        ProjectAction.TEST);

        assertThat(command.arguments())
                .containsExactly(
                        temporaryDirectory
                                .resolve("mvnw")
                                .toString(),
                        "test");

        assertThat(command.workingDirectory())
                .isEqualTo(
                        temporaryDirectory);
    }

    @Test
    void shouldRejectRunAsCommandAction() {
        assertThatThrownBy(
                () ->
                        resolver.resolve(
                                project(),
                                ProjectAction.RUN))
                .isInstanceOf(
                        IllegalArgumentException.class)
                .hasMessage(
                        "Action is not a command action: RUN");
    }

    @Test
    void shouldRejectViewLogsAsCommandAction() {
        assertThatThrownBy(
                () ->
                        resolver.resolve(
                                project(),
                                ProjectAction.VIEW_LOGS))
                .isInstanceOf(
                        IllegalArgumentException.class)
                .hasMessage(
                        "Action is not a command action: VIEW_LOGS");
    }

    @Test
    void shouldRejectStopAsCommandAction() {
        assertThatThrownBy(
                () ->
                        resolver.resolve(
                                project(),
                                ProjectAction.STOP))
                .isInstanceOf(
                        IllegalArgumentException.class)
                .hasMessage(
                        "Action is not a command action: STOP");
    }

    @Test
    void shouldFallbackToInstalledMaven()
            throws Exception {
        ProjectCommand command =
                resolver.resolve(
                        project(),
                        ProjectAction.TEST);

        assertThat(command.arguments())
                .containsExactly(
                        "mvn",
                        "test");

        assertThat(command.workingDirectory())
                .isEqualTo(
                        temporaryDirectory);
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
}