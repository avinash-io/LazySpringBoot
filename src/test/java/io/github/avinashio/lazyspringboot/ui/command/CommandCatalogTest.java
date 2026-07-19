package io.github.avinashio.lazyspringboot.ui.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CommandCatalogTest {

    @Test
    void shouldContainOnlyCreateProjectCommandWhenNoProjectSelected() {

        UiState uiState =
                mock(UiState.class);

        GetProjectProcessUseCase getProjectProcessUseCase =
                mock(GetProjectProcessUseCase.class);

        when(uiState.selectedProject())
                .thenReturn(null);

        CommandCatalog catalog =
                new CommandCatalog(
                        uiState,
                        getProjectProcessUseCase);

        assertThat(catalog.commands())
                .extracting(Command::id)
                .containsExactly(
                        "create-project");
    }

    @Test
    void shouldContainStartProjectCommandWhenProjectIsStopped() {

        UiState uiState =
                mock(UiState.class);

        GetProjectProcessUseCase getProjectProcessUseCase =
                mock(GetProjectProcessUseCase.class);

        SpringProject project =
                mock(SpringProject.class);

        when(uiState.selectedProject())
                .thenReturn(project);

        when(getProjectProcessUseCase.get(
                project))
                .thenReturn(
                        Optional.empty());

        CommandCatalog catalog =
                new CommandCatalog(
                        uiState,
                        getProjectProcessUseCase);

        assertThat(catalog.commands())
                .extracting(Command::id)
                .contains(
                        "start-project")
                .doesNotContain(
                        "stop-project",
                        "view-logs");
    }

    @Test
    void shouldContainStopAndViewLogsCommandsWhenProjectIsRunning() {

        UiState uiState =
                mock(UiState.class);

        GetProjectProcessUseCase getProjectProcessUseCase =
                mock(GetProjectProcessUseCase.class);

        SpringProject project =
                mock(SpringProject.class);

        ProjectProcess projectProcess =
                mock(ProjectProcess.class);

        when(uiState.selectedProject())
                .thenReturn(project);

        when(getProjectProcessUseCase.get(
                project))
                .thenReturn(
                        Optional.of(
                                projectProcess));

        when(projectProcess.running())
                .thenReturn(true);

        CommandCatalog catalog =
                new CommandCatalog(
                        uiState,
                        getProjectProcessUseCase);

        assertThat(catalog.commands())
                .extracting(Command::id)
                .contains(
                        "stop-project",
                        "view-logs")
                .doesNotContain(
                        "start-project");
    }
}