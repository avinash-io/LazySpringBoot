package io.github.avinashio.lazyspringboot.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.RestartProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.StartProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.StopProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.screen.ProjectActionOutputScreen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.IOException;
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
    void shouldRestartProject() throws IOException {

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

        org.mockito.Mockito
                .when(project.name())
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

        org.mockito.Mockito
                .when(project.name())
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
}