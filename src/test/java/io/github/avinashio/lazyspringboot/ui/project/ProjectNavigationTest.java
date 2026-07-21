package io.github.avinashio.lazyspringboot.ui.project;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.controller.TextInputController;
import io.github.avinashio.lazyspringboot.ui.service.ProjectFilterService;
import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;
import io.github.avinashio.lazyspringboot.ui.state.TextInputState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProjectNavigationTest {

    private UiState uiState;

    private TextInputController
            textInputController;

    private ProjectNavigation
            projectNavigation;

    @BeforeEach
    void setUp() {

        uiState =
                new UiState();

        uiState.setProjects(
                List.of(
                        project("payment-service"),
                        project("demo"),
                        project("user-service"),
                        project("inventory")));

        TextInputState textInputState =
                new TextInputState();

        textInputController =
                new TextInputController(
                        textInputState);

        projectNavigation =
                new ProjectNavigation(
                        uiState,
                        new ProjectFilterService(),
                        textInputController);
    }

    @Test
    void shouldSelectFirstVisibleProject() {

        startSearch(
                "service");

        projectNavigation
                .selectFirstVisible();

        assertThat(
                uiState.selectedProject().name())
                .isEqualTo(
                        "payment-service");
    }

    @Test
    void shouldSelectNextVisibleProject() {

        startSearch(
                "service");

        projectNavigation
                .selectFirstVisible();

        projectNavigation
                .selectNextVisible();

        assertThat(
                uiState.selectedProject().name())
                .isEqualTo(
                        "user-service");
    }

    @Test
    void shouldSelectPreviousVisibleProject() {

        startSearch(
                "service");

        projectNavigation
                .selectFirstVisible();

        projectNavigation
                .selectNextVisible();

        projectNavigation
                .selectPreviousVisible();

        assertThat(
                uiState.selectedProject().name())
                .isEqualTo(
                        "payment-service");
    }

    @Test
    void shouldNotMovePastLastVisibleProject() {

        startSearch(
                "service");

        projectNavigation
                .selectFirstVisible();

        projectNavigation
                .selectNextVisible();

        projectNavigation
                .selectNextVisible();

        assertThat(
                uiState.selectedProject().name())
                .isEqualTo(
                        "user-service");
    }

    @Test
    void shouldNotMoveBeforeFirstVisibleProject() {

        startSearch(
                "service");

        projectNavigation
                .selectFirstVisible();

        projectNavigation
                .selectPreviousVisible();

        assertThat(
                uiState.selectedProject().name())
                .isEqualTo(
                        "payment-service");
    }

    @Test
    void shouldSelectFirstVisibleWhenCurrentProjectIsNotVisible() {

        uiState.selectProject(
                1);

        startSearch(
                "service");

        projectNavigation
                .selectNextVisible();

        assertThat(
                uiState.selectedProject().name())
                .isEqualTo(
                        "payment-service");
    }

    @Test
    void shouldKeepSelectionWhenNoProjectsMatch() {

        uiState.selectProject(
                1);

        startSearch(
                "does-not-exist");

        projectNavigation
                .selectFirstVisible();

        assertThat(
                uiState.selectedProject().name())
                .isEqualTo(
                        "demo");
    }

    private void startSearch(
            String query) {

        textInputController.start(
                TextInputPurpose.PROJECT_SEARCH);

        for (char character :
                query.toCharArray()) {

            textInputController.append(
                    character);
        }
    }

    private SpringProject project(
            String name) {

        return new SpringProject(
                name,
                Path.of(
                        "/workspace/"
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