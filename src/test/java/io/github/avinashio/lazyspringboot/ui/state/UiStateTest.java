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

    @Test
    void shouldToggleSelectedDependency() {
        UiState state = createStateWithDependencies();

        state.toggleSelectedDependency();

        assertThat(
                state.selectedDependencyItem().selected())
                .isTrue();
    }

    @Test
    void shouldUnselectSelectedDependency() {
        UiState state = createStateWithDependencies();

        state.toggleSelectedDependency();
        state.toggleSelectedDependency();

        assertThat(
                state.selectedDependencyItem().selected())
                .isFalse();
    }

    @Test
    void shouldToggleCurrentlyHighlightedDependency() {
        UiState state = createStateWithDependencies();

        state.selectNextDependency();
        state.toggleSelectedDependency();

        assertThat(
                state.dependencyItems()
                        .get(1)
                        .selected())
                .isTrue();

        assertThat(
                state.dependencyItems()
                        .getFirst()
                        .selected())
                .isFalse();
    }

    @Test
    void shouldNotToggleAlreadyPresentDependency() {
        UiState state = new UiState();

        state.setDependencyItems(
                List.of(
                        new DependencyItem(
                                dependency(
                                        "web",
                                        "Spring Web",
                                        "Web applications",
                                        "Web"),
                                DependencyAvailability.ALREADY_PRESENT,
                                false)));

        state.toggleSelectedDependency();

        assertThat(
                state.selectedDependencyItem().selected())
                .isFalse();

        assertThat(state.selectedDependencyIds())
                .isEmpty();
    }

    @Test
    void shouldPreserveDependencySelectionWhenItemsAreRebuilt() {
        UiState state = createStateWithDependencies();

        state.toggleSelectedDependency();

        state.setDependencyItems(
                List.of(
                        dependencyItem("native"),
                        dependencyItem("devtools"),
                        dependencyItem("lombok")));

        assertThat(
                state.dependencyItems()
                        .getFirst()
                        .selected())
                .isTrue();
    }

    @Test
    void shouldHideSelectionWhenDependencyAlreadyExists() {
        UiState state = createStateWithDependencies();

        state.toggleSelectedDependency();

        state.setDependencyItems(
                List.of(
                        dependencyItem(
                                "native",
                                DependencyAvailability.ALREADY_PRESENT)));

        assertThat(
                state.selectedDependencyItem().selected())
                .isFalse();

        assertThat(state.selectedDependencyIds())
                .containsExactly("native");
    }

    @Test
    void shouldRestoreSelectionWhenDependencyBecomesAvailableAgain() {
        UiState state = createStateWithDependencies();

        state.toggleSelectedDependency();

        state.setDependencyItems(
                List.of(
                        dependencyItem(
                                "native",
                                DependencyAvailability.ALREADY_PRESENT)));

        state.setDependencyItems(
                List.of(dependencyItem("native")));

        assertThat(
                state.selectedDependencyItem().selected())
                .isTrue();
    }

    @Test
    void shouldRemoveDependencyIdWhenUnselected() {
        UiState state = createStateWithDependencies();

        state.toggleSelectedDependency();
        state.toggleSelectedDependency();

        assertThat(state.selectedDependencyIds())
                .doesNotContain("native");
    }

    @Test
    void shouldStartDependencyConfirmationWhenDependenciesAreSelected() {
        UiState state = createStateWithDependencies();

        state.toggleSelectedDependency();
        state.startDependencyConfirmation();

        assertThat(state.dependencyConfirmationActive())
                .isTrue();

        assertThat(state.inputMode())
                .isEqualTo(
                        InputMode.DEPENDENCY_CONFIRMATION);
    }

    @Test
    void shouldNotStartDependencyConfirmationWithoutSelection() {
        UiState state = createStateWithDependencies();

        state.startDependencyConfirmation();

        assertThat(state.inputMode())
                .isEqualTo(InputMode.NAVIGATION);
    }

    @Test
    void shouldCancelDependencyConfirmationWithoutClearingSelection() {
        UiState state = createStateWithDependencies();

        state.toggleSelectedDependency();
        state.startDependencyConfirmation();
        state.stopDependencyConfirmation();

        assertThat(state.inputMode())
                .isEqualTo(InputMode.NAVIGATION);

        assertThat(state.selectedDependencyIds())
                .contains("native");
    }

    @Test
    void shouldReturnSelectedAvailableDependencies() {
        UiState state = createStateWithDependencies();

        state.toggleSelectedDependency();

        assertThat(state.selectedDependencyItems())
                .extracting(
                        item -> item.dependency().id())
                .containsExactly("native");
    }

    @Test
    void shouldClearDependencySelections() {
        UiState state = createStateWithDependencies();

        state.toggleSelectedDependency();

        state.clearDependencySelections();

        assertThat(state.selectedDependencyIds())
                .isEmpty();

        assertThat(
                state.selectedDependencyItem().selected())
                .isFalse();
    }

    @Test
    void shouldReplaceSelectedProject() {
        UiState state = createStateWithProjects();

        state.selectNextProject();

        SpringProject refreshed =
                createProject("payment-service-refreshed");

        state.replaceSelectedProject(refreshed);

        assertThat(state.selectedProject())
                .isEqualTo(refreshed);

        assertThat(state.selectedProjectIndex())
                .isEqualTo(1);
    }

    @Test
    void shouldIgnoreReplacingProjectWhenNoProjectExists() {
        UiState state = new UiState();

        state.replaceSelectedProject(
                createProject("demo"));

        assertThat(state.projects())
                .isEmpty();
    }

    @Test
    void shouldClearHiddenSelectionAfterDependencyBecomesPresent() {
        UiState state = createStateWithDependencies();

        state.toggleSelectedDependency();

        state.setDependencyItems(
                List.of(
                        dependencyItem(
                                "native",
                                DependencyAvailability.ALREADY_PRESENT)));

        assertThat(state.selectedDependencyIds())
                .contains("native");

        state.clearDependencySelections();

        assertThat(state.selectedDependencyIds())
                .isEmpty();

        assertThat(state.selectedDependencyItem().selected())
                .isFalse();
    }

    @Test
    void shouldShowSuccessMessage() {
        UiState state = new UiState();

        state.showSuccessMessage(
                "Dependencies added");

        assertThat(state.hasMessage())
                .isTrue();

        assertThat(state.message().type())
                .isEqualTo(
                        UiMessageType.SUCCESS);

        assertThat(state.message().text())
                .isEqualTo(
                        "Dependencies added");
    }

    @Test
    void shouldShowErrorMessage() {
        UiState state = new UiState();

        state.showErrorMessage(
                "Failed to update pom.xml");

        assertThat(state.hasMessage())
                .isTrue();

        assertThat(state.message().type())
                .isEqualTo(
                        UiMessageType.ERROR);

        assertThat(state.message().text())
                .isEqualTo(
                        "Failed to update pom.xml");
    }

    @Test
    void shouldClearMessage() {
        UiState state = new UiState();

        state.showSuccessMessage(
                "Dependencies added");

        state.clearMessage();

        assertThat(state.hasMessage())
                .isFalse();

        assertThat(state.message())
                .isNull();
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
            String id) {

        return dependencyItem(
                id,
                id,
                "Test dependency",
                "Test");
    }

    private DependencyItem dependencyItem(
            String id,
            DependencyAvailability availability) {

        return new DependencyItem(
                dependency(
                        id,
                        id,
                        "Test dependency",
                        "Test"),
                availability,
                false);
    }

    private DependencyItem dependencyItem(
            String id,
            String name,
            String description,
            String group) {

        return new DependencyItem(
                dependency(
                        id,
                        name,
                        description,
                        group),
                DependencyAvailability.AVAILABLE,
                false);
    }

    private SpringDependency dependency(
            String id,
            String name,
            String description,
            String group) {

        return new SpringDependency(
                id,
                name,
                description,
                group);
    }

    private SpringProject createProject(
            String name) {

        return new SpringProject(
                name,
                Path.of(
                        "/projects/"
                                + name),
                new ProjectMetadata(
                        "com.example",
                        name,
                        "4.1.0",
                        "26",
                        BuildTool.MAVEN,
                        List.of()));
    }
}