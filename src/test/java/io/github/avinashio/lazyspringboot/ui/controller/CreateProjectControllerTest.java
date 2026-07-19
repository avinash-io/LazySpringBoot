package io.github.avinashio.lazyspringboot.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.avinashio.lazyspringboot.application.project.CreateSpringProjectUseCase;
import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.ui.service.DependencyItemsService;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.List;
import org.junit.jupiter.api.Test;

class CreateProjectControllerTest {

    @Test
    void shouldOpenWizard() {

        CreateProjectState state =
                new CreateProjectState();

        DependencyItemsService dependencyItemsService =
                mock(
                        DependencyItemsService.class);

        CreateProjectController controller =
                createController(
                        state,
                        dependencyItemsService);

        controller.open();

        assertThat(state.active())
                .isTrue();
    }

    @Test
    void shouldLoadDependenciesWhenOpeningWizard() {

        CreateProjectState state =
                new CreateProjectState();

        DependencyItemsService dependencyItemsService =
                mock(
                        DependencyItemsService.class);

        List<SpringDependency> dependencies =
                List.of(
                        new SpringDependency(
                                "web",
                                "Spring Web",
                                "Build web applications",
                                "Web"),
                        new SpringDependency(
                                "actuator",
                                "Spring Boot Actuator",
                                "Monitor and manage applications",
                                "Ops"));

        when(dependencyItemsService.catalog())
                .thenReturn(
                        dependencies);

        CreateProjectController controller =
                createController(
                        state,
                        dependencyItemsService);

        controller.open();

        assertThat(state.dependencies())
                .containsExactlyElementsOf(
                        dependencies);
    }

    @Test
    void shouldCloseWizard() {

        CreateProjectState state =
                new CreateProjectState();

        DependencyItemsService dependencyItemsService =
                mock(
                        DependencyItemsService.class);

        CreateProjectController controller =
                createController(
                        state,
                        dependencyItemsService);

        controller.open();

        controller.close();

        assertThat(state.active())
                .isFalse();
    }

    private CreateProjectController createController(
            CreateProjectState state,
            DependencyItemsService dependencyItemsService) {

        return new CreateProjectController(
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
                        CreateProjectValidator.class),
                dependencyItemsService);
    }
}