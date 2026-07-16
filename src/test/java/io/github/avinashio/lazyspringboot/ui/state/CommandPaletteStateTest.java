package io.github.avinashio.lazyspringboot.ui.state;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CommandPaletteStateTest {

    @Test
    void shouldOpenPalette() {

        CommandPaletteState state =
                new CommandPaletteState();

        state.openPalette();

        assertThat(state.open())
                .isTrue();

        assertThat(
                state.selectedCommandIndex())
                .isZero();
    }

    @Test
    void shouldClosePalette() {

        CommandPaletteState state =
                new CommandPaletteState();

        state.openPalette();

        state.closePalette();

        assertThat(state.open())
                .isFalse();
    }

    @Test
    void shouldNavigateCommands() {

        CommandPaletteState state =
                new CommandPaletteState();

        state.openPalette();

        state.selectNext(5);
        state.selectNext(5);

        assertThat(
                state.selectedCommandIndex())
                .isEqualTo(2);

        state.selectPrevious();

        assertThat(
                state.selectedCommandIndex())
                .isEqualTo(1);
    }
}