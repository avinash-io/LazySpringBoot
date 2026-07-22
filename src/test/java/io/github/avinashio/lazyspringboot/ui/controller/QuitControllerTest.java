package io.github.avinashio.lazyspringboot.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.StopProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.state.QuitOption;
import io.github.avinashio.lazyspringboot.ui.state.QuitState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class QuitControllerTest {

    @Test
    void shouldQuitImmediatelyWhenNothingIsRunning() {

        QuitController controller =
                controller(
                        new UiState());

        assertThat(
                controller.requestQuit())
                .isEqualTo(
                        QuitDecision.QUIT);

        assertThat(
                controller.active())
                .isFalse();
    }

    @Test
    void shouldOpenPopupWhenProjectIsRunning() {

        UiState uiState =
                new UiState();

        SpringProject project =
                mock(
                        SpringProject.class);

        when(project.name())
                .thenReturn(
                        "demo");

        uiState.setProjects(
                List.of(
                        project));

        GetProjectProcessUseCase getProcess =
                mock(
                        GetProjectProcessUseCase.class);

        when(
                getProcess.get(
                        project))
                .thenReturn(
                        Optional.of(
                                process(
                                        ProjectProcessStatus.RUNNING)));

        QuitController controller =
                controller(
                        uiState,
                        getProcess);

        assertThat(
                controller.requestQuit())
                .isEqualTo(
                        QuitDecision.OPEN_POPUP);

        assertThat(
                controller.active())
                .isTrue();

        assertThat(
                controller.state()
                        .runningProjects())
                .containsExactly(
                        "demo");
    }

    @Test
    void shouldMoveSelection() {

        QuitState state =
                new QuitState();

        QuitController controller =
                controller(
                        state,
                        new UiState());

        state.open(
                List.of(
                        "demo"));

        assertThat(
                state.selectedOption())
                .isEqualTo(
                        QuitOption.STOP_RUNNING);

        controller.moveNext();

        assertThat(
                state.selectedOption())
                .isEqualTo(
                        QuitOption.QUIT_ANYWAY);

        controller.movePrevious();

        assertThat(
                state.selectedOption())
                .isEqualTo(
                        QuitOption.STOP_RUNNING);
    }

    @Test
    void shouldCancelPopup() {

        QuitState state =
                new QuitState();

        QuitController controller =
                controller(
                        state,
                        new UiState());

        state.open(
                List.of(
                        "demo"));

        controller.cancel();

        assertThat(
                state.active())
                .isFalse();
    }

    @Test
    void shouldQuitWhenQuitAnywaySelected() {

        QuitState state =
                new QuitState();

        state.open(
                List.of(
                        "demo"));

        state.moveNext();

        QuitController controller =
                controller(
                        state,
                        new UiState());

        assertThat(
                controller.executeSelection())
                .isEqualTo(
                        QuitDecision.QUIT);
    }

    @Test
    void shouldQuitWhenReadyToQuitSelected() {

        QuitState state =
                new QuitState();

        state.showReadyToQuit();

        QuitController controller =
                controller(
                        state,
                        new UiState());

        assertThat(
                controller.executeSelection())
                .isEqualTo(
                        QuitDecision.QUIT);
    }

    @Test
    void shouldCheckEveryProject() {

        UiState uiState =
                new UiState();

        SpringProject first =
                mock(
                        SpringProject.class);

        SpringProject second =
                mock(
                        SpringProject.class);

        uiState.setProjects(
                List.of(
                        first,
                        second));

        GetProjectProcessUseCase getProcess =
                mock(
                        GetProjectProcessUseCase.class);

        when(
                getProcess.get(
                        first))
                .thenReturn(
                        Optional.empty());

        when(
                getProcess.get(
                        second))
                .thenReturn(
                        Optional.of(
                                process(
                                        ProjectProcessStatus.RUNNING)));

        QuitController controller =
                controller(
                        uiState,
                        getProcess);

        when(first.name())
                .thenReturn(
                        "first");

        when(second.name())
                .thenReturn(
                        "second");

        controller.requestQuit();

        verify(getProcess).get(first);
        verify(getProcess).get(second);
    }

    private QuitController controller(
            UiState uiState) {

        return controller(
                new QuitState(),
                uiState,
                mock(
                        GetProjectProcessUseCase.class));
    }

    private QuitController controller(
            UiState uiState,
            GetProjectProcessUseCase
                    getProcess) {

        return controller(
                new QuitState(),
                uiState,
                getProcess);
    }

    private QuitController controller(
            QuitState state,
            UiState uiState) {

        return controller(
                state,
                uiState,
                mock(
                        GetProjectProcessUseCase.class));
    }

    private QuitController controller(
            QuitState state,
            UiState uiState,
            GetProjectProcessUseCase
                    getProcess) {

        return new QuitController(
                state,
                uiState,
                getProcess,
                mock(
                        StopProjectProcessUseCase.class),
                mock(
                        ProjectRefreshController.class));
    }

    private ProjectProcess process(
            ProjectProcessStatus status) {

        return new ProjectProcess(
                "demo",
                status,
                List.of(),
                null,
                1L,
                null,
                null);
    }

    @Test
    void shouldReturnContinueAfterCancel() {

        QuitState state =
                new QuitState();

        state.open(
                List.of("demo"));

        QuitController controller =
                controller(
                        state,
                        new UiState());

        controller.cancel();

        assertThat(
                controller.active())
                .isFalse();
    }

    @Test
    void shouldOpenPopupWithRunningProjectNames() {

        UiState uiState =
                new UiState();

        SpringProject project =
                mock(
                        SpringProject.class);

        when(project.name())
                .thenReturn(
                        "demo-project");

        uiState.setProjects(
                List.of(project));

        GetProjectProcessUseCase getProcess =
                mock(
                        GetProjectProcessUseCase.class);

        when(
                getProcess.get(project))
                .thenReturn(
                        Optional.of(
                                process(
                                        ProjectProcessStatus.RUNNING)));

        QuitController controller =
                controller(
                        uiState,
                        getProcess);

        controller.requestQuit();

        assertThat(
                controller.state()
                        .runningProjects())
                .containsExactly(
                        "demo-project");
    }

    @Test
    void shouldStopRunningProjectsAndShowReadyToQuit()
            throws Exception {

        QuitState state =
                new QuitState();

        UiState uiState =
                new UiState();

        SpringProject project =
                mock(
                        SpringProject.class);

        when(project.name())
                .thenReturn(
                        "demo");

        uiState.setProjects(
                List.of(project));

        GetProjectProcessUseCase getProcess =
                mock(
                        GetProjectProcessUseCase.class);

        when(getProcess.get(project))
                .thenReturn(
                        Optional.of(
                                process(
                                        ProjectProcessStatus.RUNNING)));

        StopProjectProcessUseCase stopProcess =
                mock(
                        StopProjectProcessUseCase.class);

        ProjectRefreshController refreshController =
                mock(
                        ProjectRefreshController.class);

        QuitController controller =
                new QuitController(
                        state,
                        uiState,
                        getProcess,
                        stopProcess,
                        refreshController);

        controller.requestQuit();

        assertThat(
                state.selectedOption())
                .isEqualTo(
                        QuitOption.STOP_RUNNING);

        assertThat(
                controller.executeSelection())
                .isEqualTo(
                        QuitDecision.CONTINUE);

        verify(stopProcess)
                .stop(project);

        verify(refreshController)
                .refresh();

        assertThat(
                state.readyToQuitPhase())
                .isTrue();
    }

    @Test
    void shouldMoveToNextProjectWhenProjectFocused() {

        QuitState state =
                new QuitState();

        state.open(
                List.of(
                        "project-1",
                        "project-2"));

        QuitController controller =
                controller(
                        state,
                        new UiState());

        controller.focusNext();

        assertThat(
                state.selectedRunningProjectIndex())
                .isEqualTo(1);
    }

    @Test
    void shouldMoveToPreviousProjectWhenProjectFocused() {

        QuitState state =
                new QuitState();

        state.open(
                List.of(
                        "project-1",
                        "project-2"));

        state.selectNextProject();

        QuitController controller =
                controller(
                        state,
                        new UiState());

        controller.focusPrevious();

        assertThat(
                state.selectedRunningProjectIndex())
                .isZero();
    }

    @Test
    void shouldMoveToNextActionWhenActionsFocused() {

        QuitState state =
                new QuitState();

        state.open(
                List.of("project"));

        state.focusActions();

        QuitController controller =
                controller(
                        state,
                        new UiState());

        controller.focusNext();

        assertThat(
                state.selectedOption())
                .isEqualTo(
                        QuitOption.QUIT_ANYWAY);
    }

    @Test
    void shouldMoveToPreviousActionWhenActionsFocused() {

        QuitState state =
                new QuitState();

        state.open(
                List.of("project"));

        state.focusActions();

        state.moveNext();

        QuitController controller =
                controller(
                        state,
                        new UiState());

        controller.focusPrevious();

        assertThat(
                state.selectedOption())
                .isEqualTo(
                        QuitOption.STOP_RUNNING);
    }
}