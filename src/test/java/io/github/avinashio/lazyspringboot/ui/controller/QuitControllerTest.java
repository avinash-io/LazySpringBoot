package io.github.avinashio.lazyspringboot.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;
import io.github.avinashio.lazyspringboot.ui.input.KeyType;
import io.github.avinashio.lazyspringboot.ui.state.UiMessageType;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class QuitControllerTest {

    @Test
    void shouldQuitImmediatelyWhenNoProjectsAreRunning() {

        UiState uiState =
                new UiState();

        GetProjectProcessUseCase
                getProjectProcessUseCase =
                mock(
                        GetProjectProcessUseCase.class);

        QuitController controller =
                new QuitController(
                        uiState,
                        getProjectProcessUseCase);

        QuitDecision decision =
                controller.requestQuit();

        assertThat(decision)
                .isEqualTo(
                        QuitDecision.QUIT);

        assertThat(
                controller
                        .confirmationPending())
                .isFalse();

        assertThat(
                uiState.message())
                .isNull();
    }

    @Test
    void shouldRequestConfirmationWhenProjectIsRunning() {

        UiState uiState =
                new UiState();

        GetProjectProcessUseCase
                getProjectProcessUseCase =
                mock(
                        GetProjectProcessUseCase.class);

        SpringProject project =
                mock(
                        SpringProject.class);

        uiState.setProjects(
                List.of(
                        project));

        when(
                getProjectProcessUseCase.get(
                        project))
                .thenReturn(
                        Optional.of(
                                process(
                                        ProjectProcessStatus.RUNNING)));

        QuitController controller =
                new QuitController(
                        uiState,
                        getProjectProcessUseCase);

        QuitDecision decision =
                controller.requestQuit();

        assertThat(decision)
                .isEqualTo(
                        QuitDecision.WARNING);

        assertThat(
                controller
                        .confirmationPending())
                .isTrue();

        assertThat(
                uiState.message())
                .isNotNull();

        assertThat(
                uiState.message()
                        .type())
                .isEqualTo(
                        UiMessageType.WARNING);

        assertThat(
                uiState.message()
                        .text())
                .isEqualTo(
                        "1 project is still running. "
                                + "Press q/y to quit anyway "
                                + "or Esc/n to cancel.");
    }

    @Test
    void shouldRequestConfirmationWhenProjectIsStarting() {

        UiState uiState =
                new UiState();

        GetProjectProcessUseCase
                getProjectProcessUseCase =
                mock(
                        GetProjectProcessUseCase.class);

        SpringProject project =
                mock(
                        SpringProject.class);

        uiState.setProjects(
                List.of(
                        project));

        when(
                getProjectProcessUseCase.get(
                        project))
                .thenReturn(
                        Optional.of(
                                process(
                                        ProjectProcessStatus.STARTING)));

        QuitController controller =
                new QuitController(
                        uiState,
                        getProjectProcessUseCase);

        QuitDecision decision =
                controller.requestQuit();

        assertThat(decision)
                .isEqualTo(
                        QuitDecision.WARNING);

        assertThat(
                controller
                        .confirmationPending())
                .isTrue();
    }

    @Test
    void shouldShowNumberOfActiveProjectsInWarning() {

        UiState uiState =
                new UiState();

        GetProjectProcessUseCase
                getProjectProcessUseCase =
                mock(
                        GetProjectProcessUseCase.class);

        SpringProject firstProject =
                mock(
                        SpringProject.class);

        SpringProject secondProject =
                mock(
                        SpringProject.class);

        SpringProject thirdProject =
                mock(
                        SpringProject.class);

        uiState.setProjects(
                List.of(
                        firstProject,
                        secondProject,
                        thirdProject));

        when(
                getProjectProcessUseCase.get(
                        firstProject))
                .thenReturn(
                        Optional.of(
                                process(
                                        ProjectProcessStatus.RUNNING)));

        when(
                getProjectProcessUseCase.get(
                        secondProject))
                .thenReturn(
                        Optional.of(
                                process(
                                        ProjectProcessStatus.STARTING)));

        when(
                getProjectProcessUseCase.get(
                        thirdProject))
                .thenReturn(
                        Optional.empty());

        QuitController controller =
                new QuitController(
                        uiState,
                        getProjectProcessUseCase);

        QuitDecision decision =
                controller.requestQuit();

        assertThat(decision)
                .isEqualTo(
                        QuitDecision.WARNING);

        assertThat(
                uiState.message()
                        .text())
                .isEqualTo(
                        "2 projects are still running. "
                                + "Press q/y to quit anyway "
                                + "or Esc/n to cancel.");
    }

    @Test
    void shouldQuitWhenQConfirmsPendingQuit() {

        QuitController controller =
                controllerWithRunningProject();

        controller.requestQuit();

        QuitDecision decision =
                controller.handleConfirmation(
                        character(
                                'q'));

        assertThat(decision)
                .isEqualTo(
                        QuitDecision.QUIT);
    }

    @Test
    void shouldQuitWhenYConfirmsPendingQuit() {

        QuitController controller =
                controllerWithRunningProject();

        controller.requestQuit();

        QuitDecision decision =
                controller.handleConfirmation(
                        character(
                                'y'));

        assertThat(decision)
                .isEqualTo(
                        QuitDecision.QUIT);
    }

    @Test
    void shouldCancelPendingQuitWhenEscapeIsPressed() {

        UiState uiState =
                new UiState();

        QuitController controller =
                controllerWithRunningProject(
                        uiState);

        controller.requestQuit();

        assertThat(
                uiState.message())
                .isNotNull();

        QuitDecision decision =
                controller.handleConfirmation(
                        new KeyEvent(
                                KeyType.ESCAPE,
                                null));

        assertThat(decision)
                .isEqualTo(
                        QuitDecision.CONTINUE);

        assertThat(
                controller
                        .confirmationPending())
                .isFalse();

        assertThat(
                uiState.message())
                .isNull();
    }

    @Test
    void shouldCancelPendingQuitWhenNIsPressed() {

        UiState uiState =
                new UiState();

        QuitController controller =
                controllerWithRunningProject(
                        uiState);

        controller.requestQuit();

        QuitDecision decision =
                controller.handleConfirmation(
                        character(
                                'n'));

        assertThat(decision)
                .isEqualTo(
                        QuitDecision.CONTINUE);

        assertThat(
                controller
                        .confirmationPending())
                .isFalse();

        assertThat(
                uiState.message())
                .isNull();
    }

    @Test
    void shouldWarnAgainAfterQuitIsCancelled() {

        UiState uiState =
                new UiState();

        QuitController controller =
                controllerWithRunningProject(
                        uiState);

        QuitDecision firstDecision =
                controller.requestQuit();

        assertThat(firstDecision)
                .isEqualTo(
                        QuitDecision.WARNING);

        controller.handleConfirmation(
                new KeyEvent(
                        KeyType.ESCAPE,
                        null));

        QuitDecision secondDecision =
                controller.requestQuit();

        assertThat(secondDecision)
                .isEqualTo(
                        QuitDecision.WARNING);

        assertThat(
                controller
                        .confirmationPending())
                .isTrue();

        assertThat(
                uiState.message())
                .isNotNull();
    }

    @Test
    void shouldIgnoreUnrelatedKeyWhileConfirmationIsPending() {

        QuitController controller =
                controllerWithRunningProject();

        controller.requestQuit();

        QuitDecision decision =
                controller.handleConfirmation(
                        character(
                                'x'));

        assertThat(decision)
                .isEqualTo(
                        QuitDecision.CONTINUE);

        assertThat(
                controller
                        .confirmationPending())
                .isTrue();
    }

    @Test
    void shouldIgnoreConfirmationWhenNoConfirmationIsPending() {

        UiState uiState =
                new UiState();

        GetProjectProcessUseCase
                getProjectProcessUseCase =
                mock(
                        GetProjectProcessUseCase.class);

        QuitController controller =
                new QuitController(
                        uiState,
                        getProjectProcessUseCase);

        QuitDecision decision =
                controller.handleConfirmation(
                        character(
                                'q'));

        assertThat(decision)
                .isEqualTo(
                        QuitDecision.CONTINUE);

        assertThat(
                controller
                        .confirmationPending())
                .isFalse();
    }

    @Test
    void shouldCheckEveryProjectForActiveProcess() {

        UiState uiState =
                new UiState();

        GetProjectProcessUseCase
                getProjectProcessUseCase =
                mock(
                        GetProjectProcessUseCase.class);

        SpringProject firstProject =
                mock(
                        SpringProject.class);

        SpringProject secondProject =
                mock(
                        SpringProject.class);

        uiState.setProjects(
                List.of(
                        firstProject,
                        secondProject));

        when(
                getProjectProcessUseCase.get(
                        firstProject))
                .thenReturn(
                        Optional.empty());

        when(
                getProjectProcessUseCase.get(
                        secondProject))
                .thenReturn(
                        Optional.of(
                                process(
                                        ProjectProcessStatus.RUNNING)));

        QuitController controller =
                new QuitController(
                        uiState,
                        getProjectProcessUseCase);

        controller.requestQuit();

        verify(
                getProjectProcessUseCase)
                .get(
                        firstProject);

        verify(
                getProjectProcessUseCase)
                .get(
                        secondProject);
    }

    private QuitController
    controllerWithRunningProject() {

        return controllerWithRunningProject(
                new UiState());
    }

    private QuitController
    controllerWithRunningProject(
            UiState uiState) {

        GetProjectProcessUseCase
                getProjectProcessUseCase =
                mock(
                        GetProjectProcessUseCase.class);

        SpringProject project =
                mock(
                        SpringProject.class);

        uiState.setProjects(
                List.of(
                        project));

        when(
                getProjectProcessUseCase.get(
                        project))
                .thenReturn(
                        Optional.of(
                                process(
                                        ProjectProcessStatus.RUNNING)));

        return new QuitController(
                uiState,
                getProjectProcessUseCase);
    }

    private KeyEvent character(
            char character) {

        return new KeyEvent(
                KeyType.CHARACTER,
                character);
    }

    private ProjectProcess process(
            ProjectProcessStatus status) {

        return new ProjectProcess(
                "test-app",
                status,
                List.of(),
                null,
                12345L,
                null,
                null);
    }
}