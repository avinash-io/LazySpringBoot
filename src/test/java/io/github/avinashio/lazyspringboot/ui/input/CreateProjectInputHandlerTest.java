package io.github.avinashio.lazyspringboot.ui.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.ui.controller.CreateProjectController;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class CreateProjectInputHandlerTest {

    @Test
    void shouldIgnoreInputWhenCreateProjectIsNotActive() {

        CreateProjectState state =
                new CreateProjectState();

        CreateProjectController controller =
                mock(
                        CreateProjectController.class);

        when(controller.state())
                .thenReturn(
                        state);

        CreateProjectInputHandler handler =
                new CreateProjectInputHandler(
                        controller);

        boolean handled =
                handler.handle(
                        KeyEvent.of(
                                KeyType.DOWN));

        assertThat(handled)
                .isFalse();
    }

    @Test
    void shouldNavigateMetadataFields() {

        CreateProjectState state =
                new CreateProjectState();

        state.open();

        CreateProjectInputHandler handler =
                createHandler(
                        state);

        handler.handle(
                KeyEvent.of(
                        KeyType.DOWN));

        assertThat(
                state.selectedField())
                .isEqualTo(1);

        handler.handle(
                KeyEvent.of(
                        KeyType.UP));

        assertThat(
                state.selectedField())
                .isZero();
    }

    @Test
    void shouldContinueToDependenciesWithTab() {

        CreateProjectState state =
                new CreateProjectState();

        state.open();

        CreateProjectController controller =
                mock(
                        CreateProjectController.class);

        when(controller.state())
                .thenReturn(
                        state);

        CreateProjectInputHandler handler =
                new CreateProjectInputHandler(
                        controller);

        handler.handle(
                KeyEvent.of(
                        KeyType.TAB));

        verify(controller)
                .continueToDependencies();
    }

    @Test
    void shouldNavigateDependencies() {

        CreateProjectState state =
                stateWithDependencies();

        CreateProjectInputHandler handler =
                createHandler(
                        state);

        handler.handle(
                KeyEvent.of(
                        KeyType.DOWN));

        assertThat(
                state.selectedDependency()
                        .id())
                .isEqualTo(
                        "actuator");

        handler.handle(
                KeyEvent.of(
                        KeyType.UP));

        assertThat(
                state.selectedDependency()
                        .id())
                .isEqualTo(
                        "web");
    }

    @Test
    void shouldToggleDependencyWithSpace() {

        CreateProjectState state =
                stateWithDependencies();

        CreateProjectInputHandler handler =
                createHandler(
                        state);

        handler.handle(
                KeyEvent.of(
                        KeyType.SPACE));

        assertThat(
                state.selectedDependencies())
                .containsExactly(
                        "web");
    }

    @Test
    void shouldStartDependencySearch() {

        CreateProjectState state =
                stateWithDependencies();

        CreateProjectInputHandler handler =
                createHandler(
                        state);

        handler.handle(
                KeyEvent.of(
                        KeyType.SEARCH));

        assertThat(
                state.dependencySearchActive())
                .isTrue();
    }

    @Test
    void shouldToggleFilteredDependencyWithSpace() {

        CreateProjectState state =
                stateWithDependencies();

        state.startDependencySearch();

        appendSearch(
                state,
                "jpa");

        CreateProjectInputHandler handler =
                createHandler(
                        state);

        handler.handle(
                KeyEvent.of(
                        KeyType.SPACE));

        assertThat(
                state.selectedDependencies())
                .containsExactly(
                        "data-jpa");

        assertThat(
                state.dependencySearchQuery())
                .isEqualTo(
                        "jpa");
    }

    @Test
    void shouldToggleFilteredDependencyWhenSpaceIsCharacter() {

        CreateProjectState state =
                stateWithDependencies();

        state.startDependencySearch();

        appendSearch(
                state,
                "jpa");

        CreateProjectInputHandler handler =
                createHandler(
                        state);

        handler.handle(
                KeyEvent.character(
                        ' '));

        assertThat(
                state.selectedDependencies())
                .containsExactly(
                        "data-jpa");

        assertThat(
                state.dependencySearchQuery())
                .isEqualTo(
                        "jpa");
    }

    @Test
    void shouldToggleFilteredDependencyWithEnter() {

        CreateProjectState state =
                stateWithDependencies();

        state.startDependencySearch();

        appendSearch(
                state,
                "actuator");

        CreateProjectInputHandler handler =
                createHandler(
                        state);

        handler.handle(
                KeyEvent.of(
                        KeyType.ENTER));

        assertThat(
                state.selectedDependencies())
                .containsExactly(
                        "actuator");
    }

    @Test
    void shouldCloseDependencySearchWithEscape() {

        CreateProjectState state =
                stateWithDependencies();

        state.startDependencySearch();

        CreateProjectInputHandler handler =
                createHandler(
                        state);

        handler.handle(
                KeyEvent.of(
                        KeyType.ESCAPE));

        assertThat(
                state.dependencySearchActive())
                .isFalse();

        assertThat(
                state.dependencyStage())
                .isTrue();
    }

    @Test
    void shouldReturnToMetadataStageWithEscape() {

        CreateProjectState state =
                stateWithDependencies();

        CreateProjectInputHandler handler =
                createHandler(
                        state);

        handler.handle(
                KeyEvent.of(
                        KeyType.ESCAPE));

        assertThat(
                state.metadataStage())
                .isTrue();
    }

    @Test
    void shouldGenerateProjectWithEnterFromDependencyStage() {

        CreateProjectState state =
                stateWithDependencies();

        CreateProjectController controller =
                mock(
                        CreateProjectController.class);

        when(controller.state())
                .thenReturn(
                        state);

        CreateProjectInputHandler handler =
                new CreateProjectInputHandler(
                        controller);

        handler.handle(
                KeyEvent.of(
                        KeyType.ENTER));

        verify(controller)
                .generate(
                        Path.of("")
                                .toAbsolutePath());
    }

    @Test
    void shouldStartEditingWithEnterOnMetadataField() {

        CreateProjectState state =
                new CreateProjectState();

        state.open();

        CreateProjectInputHandler handler =
                createHandler(
                        state);

        handler.handle(
                KeyEvent.of(
                        KeyType.ENTER));

        assertThat(
                state.editing())
                .isTrue();

        assertThat(
                state.metadataStage())
                .isTrue();
    }

    private CreateProjectInputHandler createHandler(
            CreateProjectState state) {

        CreateProjectController controller =
                mock(
                        CreateProjectController.class);

        when(controller.state())
                .thenReturn(
                        state);

        return new CreateProjectInputHandler(
                controller);
    }

    private CreateProjectState
    stateWithDependencies() {

        CreateProjectState state =
                new CreateProjectState();

        state.setDependencies(
                List.of(
                        dependency(
                                "web",
                                "Spring Web",
                                "Web"),
                        dependency(
                                "actuator",
                                "Spring Boot Actuator",
                                "Ops"),
                        dependency(
                                "data-jpa",
                                "Spring Data JPA",
                                "SQL")));

        state.open();

        state.showDependencyStage();

        return state;
    }

    private SpringDependency dependency(
            String id,
            String name,
            String group) {

        return new SpringDependency(
                id,
                name,
                "Test dependency",
                group);
    }

    private void appendSearch(
            CreateProjectState state,
            String query) {

        for (char character :
                query.toCharArray()) {

            state.appendDependencySearch(
                    character);
        }
    }
}