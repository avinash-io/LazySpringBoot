package io.github.avinashio.lazyspringboot.ui.state;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;

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
                        createProject("cv-api"),
                        createProject("payment-service"),
                        createProject("demo")));

        return state;
    }

    @Test
    void shouldResetSelectionWhenProjectsAreUpdated() {
        UiState state = createStateWithProjects();

        state.selectNextProject();
        state.selectNextProject();

        state.setProjects(List.of(createProject("new-project")));

        assertThat(state.selectedProjectIndex()).isZero();
    }

    private SpringProject createProject(String name) {
        return new SpringProject(
                name,
                Path.of("/projects/" + name),
                new ProjectMetadata(
                        "com.example",
                        name,
                        "4.1.0",
                        "26",
                        BuildTool.MAVEN));
    }

    @Test
    void shouldReturnSelectedProject() {
        UiState state = createStateWithProjects();

        state.selectNextProject();

        assertThat(state.selectedProject().name())
                .isEqualTo("payment-service");
    }

    @Test
    void shouldReturnNullWhenNoProjectIsAvailable() {
        UiState state = new UiState();

        assertThat(state.selectedProject()).isNull();
    }

    @Test
    void shouldInitiallyFocusProjectsPanel() {
        UiState state = new UiState();

        assertThat(state.panelFocus())
                .isEqualTo(PanelFocus.PROJECTS);
    }

    @Test
    void shouldFocusNextPanel() {
        UiState state = new UiState();

        state.focusNextPanel();

        assertThat(state.panelFocus())
                .isEqualTo(PanelFocus.PROJECT_DETAILS);
    }

    @Test
    void shouldFocusPreviousPanel() {
        UiState state = new UiState();

        state.focusPreviousPanel();

        assertThat(state.panelFocus())
                .isEqualTo(PanelFocus.PROJECT_DETAILS);
    }

    @Test
    void shouldCycleFocusToProjectsPanel() {
        UiState state = new UiState();

        state.focusNextPanel();
        state.focusNextPanel();

        assertThat(state.panelFocus())
                .isEqualTo(PanelFocus.PROJECTS);
    }
}