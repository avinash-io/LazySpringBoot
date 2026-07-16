package io.github.avinashio.lazyspringboot.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import io.github.avinashio.lazyspringboot.application.project.CreateSpringProjectUseCase;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.junit.jupiter.api.Test;
import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;


class CreateProjectControllerTest {

    @Test
    void shouldOpenWizard() {

        CreateProjectState state =
                new CreateProjectState();

        CreateProjectController controller =
                new CreateProjectController(
                        state,
                        mock(
                                CreateProjectRequestMapper.class),
                        mock(
                                CreateSpringProjectUseCase.class),
                        mock(
                                DiscoverProjectsUseCase.class),
                        mock(
                                UiState.class),
                        mock(
                                CreateProjectValidator.class));

        controller.open();

        assertThat(state.active())
                .isTrue();
    }

    @Test
    void shouldCloseWizard() {

        CreateProjectState state =
                new CreateProjectState();

        CreateProjectController controller =
                new CreateProjectController(
                        state,
                        mock(
                                CreateProjectRequestMapper.class),
                        mock(
                                CreateSpringProjectUseCase.class),
                        mock(
                                DiscoverProjectsUseCase.class),
                        mock(
                                UiState.class),
                        mock(
                                CreateProjectValidator.class));

        controller.open();

        controller.close();

        assertThat(state.active())
                .isFalse();
    }
}