package io.github.avinashio.lazyspringboot.ui.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.avinashio.lazyspringboot.ui.controller.QuitController;
import io.github.avinashio.lazyspringboot.ui.controller.QuitDecision;
import io.github.avinashio.lazyspringboot.ui.state.QuitFocus;
import io.github.avinashio.lazyspringboot.ui.state.QuitState;
import org.junit.jupiter.api.Test;

import java.util.List;

class QuitInputHandlerTest {

    @Test
    void shouldMoveSelectionUp() {

        QuitController controller =
                mock(
                        QuitController.class);

        QuitState state =
                new QuitState();

        state.focusActions();

        when(controller.state())
                .thenReturn(state);

        QuitInputHandler handler =
                new QuitInputHandler(
                        controller);

        handler.handle(
                new KeyEvent(
                        KeyType.UP,
                        null));

        verify(controller)
                .focusPrevious();
    }

    @Test
    void shouldMoveSelectionDown() {

        QuitController controller =
                mock(
                        QuitController.class);

        QuitState state =
                new QuitState();

        state.focusActions();

        when(controller.state())
                .thenReturn(state);

        QuitInputHandler handler =
                new QuitInputHandler(
                        controller);

        handler.handle(
                new KeyEvent(
                        KeyType.DOWN,
                        null));

        verify(controller)
                .focusNext();
    }

    @Test
    void shouldCancelPopup() {

        QuitController controller =
                mock(
                        QuitController.class);

        QuitInputHandler handler =
                new QuitInputHandler(
                        controller);

        handler.handle(
                new KeyEvent(
                        KeyType.ESCAPE,
                        null));

        verify(controller)
                .cancel();
    }

    @Test
    void shouldExecuteSelection() {

        QuitController controller =
                mock(
                        QuitController.class);

        when(
                controller.executeSelection())
                .thenReturn(
                        QuitDecision.QUIT);

        QuitInputHandler handler =
                new QuitInputHandler(
                        controller);

        assertThat(
                handler.handle(
                        new KeyEvent(
                                KeyType.ENTER,
                                null)))
                .isEqualTo(
                        QuitDecision.QUIT);

        verify(controller)
                .executeSelection();
    }

    @Test
    void shouldIgnoreUnhandledKeys() {

        QuitController controller =
                mock(
                        QuitController.class);

        QuitInputHandler handler =
                new QuitInputHandler(
                        controller);

        assertThat(
                handler.handle(
                        new KeyEvent(
                                KeyType.LEFT,
                                null)))
                .isEqualTo(
                        QuitDecision.CONTINUE);
    }

    @Test
    void shouldToggleFocusWithTab() {

        QuitState state =
                new QuitState();

        state.open(
                List.of("demo"));

        QuitController controller =
                mock(
                        QuitController.class);

        when(controller.state())
                .thenReturn(state);

        QuitInputHandler handler =
                new QuitInputHandler(
                        controller);

        handler.handle(
                KeyEvent.of(
                        KeyType.TAB));

        assertThat(
                state.focus())
                .isEqualTo(
                        QuitFocus.ACTIONS);
    }

    @Test
    void shouldToggleSelectedProject() {

        QuitState state =
                new QuitState();

        state.open(
                List.of("demo"));

        QuitController controller =
                mock(
                        QuitController.class);

        when(controller.state())
                .thenReturn(state);

        QuitInputHandler handler =
                new QuitInputHandler(
                        controller);

        handler.handle(
                KeyEvent.of(
                        KeyType.SPACE));

        assertThat(
                state.selectedRunningProjects())
                .isEmpty();
    }
}