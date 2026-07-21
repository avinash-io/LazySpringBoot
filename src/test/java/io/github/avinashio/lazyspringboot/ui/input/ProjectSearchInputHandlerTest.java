package io.github.avinashio.lazyspringboot.ui.input;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.controller.TextInputController;
import io.github.avinashio.lazyspringboot.ui.project.ProjectNavigation;
import io.github.avinashio.lazyspringboot.ui.service.ProjectFilterService;
import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;
import io.github.avinashio.lazyspringboot.ui.state.TextInputState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.github.avinashio.lazyspringboot.ui.service.ProjectSortService;
import io.github.avinashio.lazyspringboot.ui.service.VisibleProjectService;
import io.github.avinashio.lazyspringboot.ui.state.ProjectSortState;


class ProjectSearchInputHandlerTest {

    private UiState uiState;

    private TextInputController
            textInputController;

    private ProjectSearchInputHandler
            handler;

    @BeforeEach
    void setUp() {

        uiState =
                new UiState();

        uiState.setProjects(
                List.of(
                        project("payment-service"),
                        project("demo"),
                        project("user-service")));

        TextInputState textInputState =
                new TextInputState();

        textInputController =
                new TextInputController(
                        textInputState);

        VisibleProjectService visibleProjectService =
                new VisibleProjectService(
                        new ProjectFilterService(),
                        new ProjectSortService(),
                        new ProjectSortState(),
                        textInputController);

        ProjectNavigation projectNavigation =
                new ProjectNavigation(
                        uiState,
                        visibleProjectService);

        handler =
                new ProjectSearchInputHandler(
                        textInputController,
                        projectNavigation);
    }

    @Test
    void shouldIgnoreInputWhenProjectSearchIsInactive() {

        boolean handled =
                handler.handle(
                        KeyEvent.character(
                                'd'));

        assertThat(handled)
                .isFalse();

        assertThat(
                textInputController.value())
                .isEmpty();
    }

    @Test
    void shouldHandleInputWhenProjectSearchIsActive() {

        startProjectSearch();

        boolean handled =
                handler.handle(
                        KeyEvent.character(
                                'd'));

        assertThat(handled)
                .isTrue();

        assertThat(
                textInputController.value())
                .isEqualTo("d");
    }

    @Test
    void shouldAppendSearchCharacters() {

        startProjectSearch();

        handler.handle(
                KeyEvent.character('d'));

        handler.handle(
                KeyEvent.character('e'));

        handler.handle(
                KeyEvent.character('m'));

        handler.handle(
                KeyEvent.character('o'));

        assertThat(
                textInputController.value())
                .isEqualTo("demo");
    }

    @Test
    void shouldSelectFirstMatchingProjectAfterTyping() {

        startProjectSearch();

        handler.handle(
                KeyEvent.character('u'));

        assertThat(
                uiState.selectedProject().name())
                .isEqualTo(
                        "user-service");
    }

    @Test
    void shouldNavigateDownThroughMatchingProjects() {

        startProjectSearch();

        type(
                "service");

        handler.handle(
                new KeyEvent(
                        KeyType.DOWN,
                        null));

        assertThat(
                uiState.selectedProject().name())
                .isEqualTo(
                        "user-service");
    }

    @Test
    void shouldNavigateUpThroughMatchingProjects() {

        startProjectSearch();

        type(
                "service");

        handler.handle(
                new KeyEvent(
                        KeyType.DOWN,
                        null));

        handler.handle(
                new KeyEvent(
                        KeyType.UP,
                        null));

        assertThat(
                uiState.selectedProject().name())
                .isEqualTo(
                        "payment-service");
    }

    @Test
    void shouldRemoveLastSearchCharacter() {

        startProjectSearch();

        type(
                "demo");

        handler.handle(
                new KeyEvent(
                        KeyType.BACKSPACE,
                        null));

        assertThat(
                textInputController.value())
                .isEqualTo(
                        "dem");
    }

    @Test
    void shouldCloseSearchOnEscape() {

        startProjectSearch();

        type(
                "demo");

        handler.handle(
                new KeyEvent(
                        KeyType.ESCAPE,
                        null));

        assertThat(
                textInputController.active(
                        TextInputPurpose.PROJECT_SEARCH))
                .isFalse();

        assertThat(
                textInputController.value())
                .isEmpty();
    }

    @Test
    void shouldAcceptSelectionAndCloseSearchOnEnter() {

        startProjectSearch();

        type(
                "user");

        handler.handle(
                new KeyEvent(
                        KeyType.ENTER,
                        null));

        assertThat(
                textInputController.active(
                        TextInputPurpose.PROJECT_SEARCH))
                .isFalse();

        assertThat(
                uiState.selectedProject().name())
                .isEqualTo(
                        "user-service");
    }

    @Test
    void shouldNotHandleDependencySearch() {

        textInputController.start(
                TextInputPurpose.DEPENDENCY_SEARCH);

        boolean handled =
                handler.handle(
                        KeyEvent.character(
                                'w'));

        assertThat(handled)
                .isFalse();

        assertThat(
                textInputController.value())
                .isEmpty();
    }

    private void startProjectSearch() {

        textInputController.start(
                TextInputPurpose.PROJECT_SEARCH);
    }

    private void type(
            String value) {

        for (char character :
                value.toCharArray()) {

            handler.handle(
                    KeyEvent.character(
                            character));
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