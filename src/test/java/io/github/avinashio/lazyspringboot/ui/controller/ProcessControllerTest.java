package io.github.avinashio.lazyspringboot.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.RestartProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.StartProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.StopProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectActionOutput;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.screen.ProjectActionOutputScreen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ProcessControllerTest {

    @Test
    void shouldCreateController() {

        ProcessController controller =
                new ProcessController(
                        mock(UiState.class),
                        mock(StartProjectProcessUseCase.class),
                        mock(StopProjectProcessUseCase.class),
                        mock(RestartProjectProcessUseCase.class),
                        mock(GetProjectProcessUseCase.class),
                        mock(ProjectActionOutputScreen.class));

        assertThat(controller)
                .isNotNull();
    }

    @Test
    void shouldRestartProject()
            throws IOException {

        UiState uiState =
                mock(UiState.class);

        RestartProjectProcessUseCase restartUseCase =
                mock(
                        RestartProjectProcessUseCase.class);

        SpringProject project =
                mock(
                        SpringProject.class);

        ProcessController controller =
                createController(
                        uiState,
                        restartUseCase);

        controller.restart(
                project);

        verify(restartUseCase)
                .restart(
                        project);

        verify(uiState)
                .stopProjectActions();
    }

    @Test
    void shouldShowSuccessMessageWhenProjectRestarts()
            throws IOException {

        UiState uiState =
                mock(UiState.class);

        RestartProjectProcessUseCase restartUseCase =
                mock(
                        RestartProjectProcessUseCase.class);

        SpringProject project =
                mock(
                        SpringProject.class);

        when(project.name())
                .thenReturn(
                        "test-app");

        ProcessController controller =
                createController(
                        uiState,
                        restartUseCase);

        controller.restart(
                project);

        verify(uiState)
                .showSuccessMessage(
                        "Restarted test-app");
    }

    @Test
    void shouldShowErrorMessageWhenRestartFails()
            throws IOException {

        UiState uiState =
                mock(UiState.class);

        RestartProjectProcessUseCase restartUseCase =
                mock(
                        RestartProjectProcessUseCase.class);

        SpringProject project =
                mock(
                        SpringProject.class);

        when(project.name())
                .thenReturn(
                        "test-app");

        doThrow(
                new IOException(
                        "Process restart failed"))
                .when(restartUseCase)
                .restart(
                        project);

        ProcessController controller =
                createController(
                        uiState,
                        restartUseCase);

        controller.restart(
                project);

        verify(uiState)
                .stopProjectActions();

        verify(uiState)
                .showErrorMessage(
                        "Failed to restart test-app: "
                                + "Process restart failed");
    }

    @Test
    void shouldShowLogsForProjectProcess() {

        UiState uiState =
                new UiState();

        GetProjectProcessUseCase getProjectProcessUseCase =
                mock(
                        GetProjectProcessUseCase.class);

        ProjectActionOutputScreen outputScreen =
                mock(
                        ProjectActionOutputScreen.class);

        SpringProject project =
                mock(
                        SpringProject.class);

        when(project.name())
                .thenReturn(
                        "test-app");

        when(outputScreen.visibleHeight())
                .thenReturn(
                        3);

        when(
                getProjectProcessUseCase.get(
                        project))
                .thenReturn(
                        Optional.of(
                                process(
                                        List.of(
                                                "line-1",
                                                "line-2",
                                                "line-3",
                                                "line-4",
                                                "line-5"))));

        ProcessController controller =
                createController(
                        uiState,
                        getProjectProcessUseCase,
                        outputScreen);

        controller.showLogs(
                project);

        ProjectActionOutput output =
                uiState.projectActionOutput();

        assertThat(output)
                .isNotNull();

        assertThat(output.projectName())
                .isEqualTo(
                        "test-app");

        assertThat(output.action())
                .isEqualTo(
                        ProjectAction.VIEW_LOGS);

        assertThat(output.lines())
                .containsExactly(
                        "line-1",
                        "line-2",
                        "line-3",
                        "line-4",
                        "line-5");

        assertThat(
                uiState.outputViewport()
                        .offset())
                .isEqualTo(
                        2);
    }

    @Test
    void shouldShowErrorWhenProjectProcessDoesNotExist() {

        UiState uiState =
                mock(
                        UiState.class);

        GetProjectProcessUseCase getProjectProcessUseCase =
                mock(
                        GetProjectProcessUseCase.class);

        ProjectActionOutputScreen outputScreen =
                mock(
                        ProjectActionOutputScreen.class);

        SpringProject project =
                mock(
                        SpringProject.class);

        when(project.name())
                .thenReturn(
                        "test-app");

        when(
                getProjectProcessUseCase.get(
                        project))
                .thenReturn(
                        Optional.empty());

        ProcessController controller =
                createController(
                        uiState,
                        getProjectProcessUseCase,
                        outputScreen);

        controller.showLogs(
                project);

        verify(uiState)
                .stopProjectActions();

        verify(uiState)
                .showErrorMessage(
                        "No process found for test-app");

        verify(
                outputScreen,
                never())
                .visibleHeight();
    }

    @Test
    void shouldRefreshVisibleLogs() {

        UiState uiState =
                new UiState();

        GetProjectProcessUseCase getProjectProcessUseCase =
                mock(
                        GetProjectProcessUseCase.class);

        ProjectActionOutputScreen outputScreen =
                mock(
                        ProjectActionOutputScreen.class);

        SpringProject project =
                mock(
                        SpringProject.class);

        when(outputScreen.visibleHeight())
                .thenReturn(
                        3);

        when(
                getProjectProcessUseCase.get(
                        project))
                .thenReturn(
                        Optional.of(
                                process(
                                        List.of(
                                                "line-1",
                                                "line-2",
                                                "line-3"))));

        ProcessController controller =
                createController(
                        uiState,
                        getProjectProcessUseCase,
                        outputScreen);

        controller.showLogs(
                project);

        when(
                getProjectProcessUseCase.get(
                        project))
                .thenReturn(
                        Optional.of(
                                process(
                                        List.of(
                                                "line-1",
                                                "line-2",
                                                "line-3",
                                                "line-4"))));

        controller.refreshLogs(
                project);

        assertThat(
                uiState.projectActionOutput()
                        .lines())
                .containsExactly(
                        "line-1",
                        "line-2",
                        "line-3",
                        "line-4");
    }

    @Test
    void shouldIgnoreRefreshWhenLogsAreNotVisible() {

        UiState uiState =
                new UiState();

        GetProjectProcessUseCase getProjectProcessUseCase =
                mock(
                        GetProjectProcessUseCase.class);

        ProjectActionOutputScreen outputScreen =
                mock(
                        ProjectActionOutputScreen.class);

        SpringProject project =
                mock(
                        SpringProject.class);

        ProcessController controller =
                createController(
                        uiState,
                        getProjectProcessUseCase,
                        outputScreen);

        controller.refreshLogs(
                project);

        verify(
                getProjectProcessUseCase,
                never())
                .get(
                        project);
    }

    @Test
    void shouldFollowNewLogsWhenViewportIsAtBottom() {

        UiState uiState =
                new UiState();

        GetProjectProcessUseCase getProjectProcessUseCase =
                mock(
                        GetProjectProcessUseCase.class);

        ProjectActionOutputScreen outputScreen =
                mock(
                        ProjectActionOutputScreen.class);

        SpringProject project =
                mock(
                        SpringProject.class);

        when(outputScreen.visibleHeight())
                .thenReturn(
                        3);

        when(
                getProjectProcessUseCase.get(
                        project))
                .thenReturn(
                        Optional.of(
                                process(
                                        List.of(
                                                "line-1",
                                                "line-2",
                                                "line-3",
                                                "line-4",
                                                "line-5"))));

        ProcessController controller =
                createController(
                        uiState,
                        getProjectProcessUseCase,
                        outputScreen);

        controller.showLogs(
                project);

        assertThat(
                uiState.outputViewport()
                        .offset())
                .isEqualTo(
                        2);

        when(
                getProjectProcessUseCase.get(
                        project))
                .thenReturn(
                        Optional.of(
                                process(
                                        List.of(
                                                "line-1",
                                                "line-2",
                                                "line-3",
                                                "line-4",
                                                "line-5",
                                                "line-6",
                                                "line-7"))));

        controller.refreshLogs(
                project);

        assertThat(
                uiState.outputViewport()
                        .offset())
                .isEqualTo(
                        4);

        assertThat(
                uiState.projectActionOutput()
                        .lines())
                .hasSize(
                        7);
    }

    @Test
    void shouldPreserveViewportWhenUserHasScrolledUp() {

        UiState uiState =
                new UiState();

        GetProjectProcessUseCase getProjectProcessUseCase =
                mock(
                        GetProjectProcessUseCase.class);

        ProjectActionOutputScreen outputScreen =
                mock(
                        ProjectActionOutputScreen.class);

        SpringProject project =
                mock(
                        SpringProject.class);

        when(outputScreen.visibleHeight())
                .thenReturn(
                        3);

        when(
                getProjectProcessUseCase.get(
                        project))
                .thenReturn(
                        Optional.of(
                                process(
                                        List.of(
                                                "line-1",
                                                "line-2",
                                                "line-3",
                                                "line-4",
                                                "line-5"))));

        ProcessController controller =
                createController(
                        uiState,
                        getProjectProcessUseCase,
                        outputScreen);

        controller.showLogs(
                project);

        assertThat(
                uiState.outputViewport()
                        .offset())
                .isEqualTo(
                        2);

        uiState.outputViewport()
                .scrollUp();

        assertThat(
                uiState.outputViewport()
                        .offset())
                .isEqualTo(
                        1);

        when(
                getProjectProcessUseCase.get(
                        project))
                .thenReturn(
                        Optional.of(
                                process(
                                        List.of(
                                                "line-1",
                                                "line-2",
                                                "line-3",
                                                "line-4",
                                                "line-5",
                                                "line-6",
                                                "line-7"))));

        controller.refreshLogs(
                project);

        assertThat(
                uiState.outputViewport()
                        .offset())
                .isEqualTo(
                        1);

        assertThat(
                uiState.projectActionOutput()
                        .lines())
                .hasSize(
                        7);
    }

    private ProcessController createController(
            UiState uiState,
            RestartProjectProcessUseCase restartUseCase) {

        return new ProcessController(
                uiState,
                mock(
                        StartProjectProcessUseCase.class),
                mock(
                        StopProjectProcessUseCase.class),
                restartUseCase,
                mock(
                        GetProjectProcessUseCase.class),
                mock(
                        ProjectActionOutputScreen.class));
    }

    private ProcessController createController(
            UiState uiState,
            GetProjectProcessUseCase getProjectProcessUseCase,
            ProjectActionOutputScreen outputScreen) {

        return new ProcessController(
                uiState,
                mock(
                        StartProjectProcessUseCase.class),
                mock(
                        StopProjectProcessUseCase.class),
                mock(
                        RestartProjectProcessUseCase.class),
                getProjectProcessUseCase,
                outputScreen);
    }

    private ProjectProcess process(
            List<String> output) {

        return new ProjectProcess(
                "test-app",
                ProjectProcessStatus.RUNNING,
                output,
                null,
                12345L,
                null,
                null);
    }
}