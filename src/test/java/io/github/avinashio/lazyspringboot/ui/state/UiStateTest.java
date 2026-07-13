package io.github.avinashio.lazyspringboot.ui.state;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyAvailability;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
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

    @Test
    void shouldResetSelectionWhenProjectsAreUpdated() {
        UiState state = createStateWithProjects();

        state.selectNextProject();
        state.selectNextProject();

        state.setProjects(
                List.of(createProject("new-project")));

        assertThat(state.selectedProjectIndex()).isZero();
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
                .isEqualTo(PanelFocus.DEPENDENCIES);
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
        state.focusNextPanel();

        assertThat(state.panelFocus())
                .isEqualTo(PanelFocus.PROJECTS);
    }

    @Test
    void shouldSelectNextDependency() {
        UiState state = createStateWithDependencies();

        state.selectNextDependency();

        assertThat(state.selectedDependencyIndex())
                .isEqualTo(1);
    }

    @Test
    void shouldSelectPreviousDependency() {
        UiState state = createStateWithDependencies();

        state.selectNextDependency();
        state.selectPreviousDependency();

        assertThat(state.selectedDependencyIndex()).isZero();
    }

    @Test
    void shouldNotMovePastLastDependency() {
        UiState state = createStateWithDependencies();

        state.selectNextDependency();
        state.selectNextDependency();
        state.selectNextDependency();

        assertThat(state.selectedDependencyIndex())
                .isEqualTo(2);
    }

    @Test
    void shouldReturnSelectedDependencyItem() {
        UiState state = createStateWithDependencies();

        state.selectNextDependency();

        assertThat(
                state
                        .selectedDependencyItem()
                        .dependency()
                        .id())
                .isEqualTo("devtools");
    }

    @Test
    void shouldResetDependencySelectionWhenItemsAreUpdated() {
        UiState state = createStateWithDependencies();

        state.selectNextDependency();

        state.setDependencyItems(
                List.of(
                        dependencyItem(
                                "data-jpa",
                                "Spring Data JPA",
                                "Persist data",
                                "SQL")));

        assertThat(state.selectedDependencyIndex()).isZero();
    }

    @Test
    void shouldReturnNullWhenNoDependencyItemIsAvailable() {
        UiState state = new UiState();

        assertThat(state.selectedDependencyItem()).isNull();
    }

    @Test
    void shouldExposeDependencyItems() {
        UiState state = createStateWithDependencies();

        assertThat(state.dependencyItems())
                .extracting(
                        item -> item.dependency().id())
                .containsExactly(
                        "native",
                        "devtools",
                        "lombok");
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

    private UiState createStateWithDependencies() {
        UiState state = new UiState();

        state.setDependencyItems(
                List.of(
                        dependencyItem(
                                "native",
                                "GraalVM Native Support",
                                "Native executable support",
                                "Developer Tools"),
                        dependencyItem(
                                "devtools",
                                "Spring Boot DevTools",
                                "Development tools",
                                "Developer Tools"),
                        dependencyItem(
                                "lombok",
                                "Lombok",
                                "Reduce boilerplate",
                                "Developer Tools")));

        return state;
    }

    private DependencyItem dependencyItem(
            String id,
            String name,
            String description,
            String group) {
        return new DependencyItem(
                new SpringDependency(
                        id,
                        name,
                        description,
                        group),
                DependencyAvailability.AVAILABLE,
                false);
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
                        BuildTool.MAVEN,
                        List.of()));
    }
}