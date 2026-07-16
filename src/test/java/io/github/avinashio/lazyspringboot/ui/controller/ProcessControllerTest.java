package io.github.avinashio.lazyspringboot.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.StartProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.StopProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.ui.screen.ProjectActionOutputScreen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.junit.jupiter.api.Test;

class ProcessControllerTest {

    @Test
    void shouldCreateController() {

        ProcessController controller =
                new ProcessController(
                        mock(UiState.class),
                        mock(StartProjectProcessUseCase.class),
                        mock(StopProjectProcessUseCase.class),
                        mock(GetProjectProcessUseCase.class),
                        mock(ProjectActionOutputScreen.class));

        assertThat(controller)
                .isNotNull();
    }
}