package io.github.avinashio.lazyspringboot.ui.state;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TextInputStateTest {

    @Test
    void shouldInitiallyBeInactive() {

        TextInputState state =
                new TextInputState();

        assertThat(state.active())
                .isFalse();

        assertThat(state.purpose())
                .isNull();

        assertThat(state.value())
                .isEmpty();
    }

    @Test
    void shouldStartDependencySearch() {

        TextInputState state =
                new TextInputState();

        state.start(
                TextInputPurpose.DEPENDENCY_SEARCH);

        assertThat(state.active())
                .isTrue();

        assertThat(state.isActive(
                TextInputPurpose.DEPENDENCY_SEARCH))
                .isTrue();

        assertThat(state.purpose())
                .isEqualTo(
                        TextInputPurpose.DEPENDENCY_SEARCH);
    }

    @Test
    void shouldAppendCharacters() {

        TextInputState state =
                new TextInputState();

        state.start(
                TextInputPurpose.DEPENDENCY_SEARCH);

        state.append('w');
        state.append('e');
        state.append('b');

        assertThat(state.value())
                .isEqualTo("web");
    }

    @Test
    void shouldIgnoreAppendWhenInactive() {

        TextInputState state =
                new TextInputState();

        state.append('w');

        assertThat(state.value())
                .isEmpty();
    }

    @Test
    void shouldRemoveLastCharacter() {

        TextInputState state =
                new TextInputState();

        state.start(
                TextInputPurpose.DEPENDENCY_SEARCH);

        state.append('w');
        state.append('e');
        state.append('b');

        state.backspace();

        assertThat(state.value())
                .isEqualTo("we");
    }

    @Test
    void shouldIgnoreBackspaceWhenValueIsEmpty() {

        TextInputState state =
                new TextInputState();

        state.start(
                TextInputPurpose.DEPENDENCY_SEARCH);

        state.backspace();

        assertThat(state.value())
                .isEmpty();
    }

    @Test
    void shouldStopAndClearInput() {

        TextInputState state =
                new TextInputState();

        state.start(
                TextInputPurpose.DEPENDENCY_SEARCH);

        state.append('w');

        state.stop();

        assertThat(state.active())
                .isFalse();

        assertThat(state.purpose())
                .isNull();

        assertThat(state.value())
                .isEmpty();
    }

    @Test
    void shouldSwitchInputPurposeAndClearValue() {

        TextInputState state =
                new TextInputState();

        state.start(
                TextInputPurpose.DEPENDENCY_SEARCH);

        state.append('w');

        state.start(
                TextInputPurpose.PROJECT_SEARCH);

        assertThat(state.isActive(
                TextInputPurpose.PROJECT_SEARCH))
                .isTrue();

        assertThat(state.isActive(
                TextInputPurpose.DEPENDENCY_SEARCH))
                .isFalse();

        assertThat(state.value())
                .isEmpty();
    }
}