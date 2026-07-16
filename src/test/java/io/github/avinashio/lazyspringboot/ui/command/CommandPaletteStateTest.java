package io.github.avinashio.lazyspringboot.ui.command;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CommandPaletteStateTest {

    @Test
    void shouldNavigateCommands() {

        CommandPaletteState state =
                new CommandPaletteState();

        state.open();

        state.next(5);

        assertThat(state.selectedIndex())
                .isEqualTo(1);

        state.previous();

        assertThat(state.selectedIndex())
                .isZero();
    }
}