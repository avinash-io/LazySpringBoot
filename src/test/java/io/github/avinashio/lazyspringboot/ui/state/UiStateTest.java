package io.github.avinashio.lazyspringboot.ui.state;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class UiStateTest {

    @Test
    void shouldSelectNextProject() {
        UiState state = createStateWithProjects();

        state.selectNextProject();

        assertThat(state.selectedProjectIndex()).isEqualTo(1);
    }

    @Test
    void shouldSelectPreviousProject() {
        UiState state = createStateWithProjects();

        state.selectNextProject();
        state.selectPreviousProject();

        assertThat(state.selectedProjectIndex()).isZero();
    }

    @Test
    void shouldNotMoveBeforeFirstProject() {
        UiState state = createStateWithProjects();

        state.selectPreviousProject();

        assertThat(state.selectedProjectIndex()).isZero();
    }

    @Test
    void shouldNotMovePastLastProject() {
        UiState state = createStateWithProjects();

        state.selectNextProject();
        state.selectNextProject();
        state.selectNextProject();

        assertThat(state.selectedProjectIndex()).isEqualTo(2);
    }

    private UiState createStateWithProjects() {
        UiState state = new UiState();

        state.setProjects(
                List.of(
                        new SpringProject("cv-api", Path.of("/projects/cv-api")),
                        new SpringProject(
                                "payment-service", Path.of("/projects/payment-service")),
                        new SpringProject("demo", Path.of("/projects/demo"))));

        return state;
    }

    @Test
    void shouldResetSelectionWhenProjectsAreUpdated() {
        UiState state = createStateWithProjects();

        state.selectNextProject();
        state.selectNextProject();

        state.setProjects(
                List.of(
                        new SpringProject(
                                "new-project", Path.of("/projects/new-project"))));

        assertThat(state.selectedProjectIndex()).isZero();
    }
}