package io.github.avinashio.lazyspringboot.ui.command;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.ui.state.CommandPaletteState;
import org.junit.jupiter.api.Test;

class CommandPaletteStateTest {

    @Test
    void shouldNavigateCommands() {

        CommandPaletteState state =
                new CommandPaletteState();

        state.openPalette();

        state.selectNext(5);

        assertThat(
                state.selectedCommandIndex())
                .isEqualTo(1);

        state.selectPrevious();

        assertThat(
                state.selectedCommandIndex())
                .isZero();
    }
}