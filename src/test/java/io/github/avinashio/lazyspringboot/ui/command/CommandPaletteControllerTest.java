package io.github.avinashio.lazyspringboot.ui.command;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;
import io.github.avinashio.lazyspringboot.ui.input.KeyType;
import org.junit.jupiter.api.Test;

class CommandPaletteControllerTest {

    @Test
    void shouldNavigateCommands() {

        CommandPaletteController controller =
                new CommandPaletteController(
                        new CommandCatalog(),
                        new CommandPaletteState());

        controller.open();

        controller.handleKey(
                KeyEvent.of(
                        KeyType.DOWN));

        assertThat(
                controller.state()
                        .selectedIndex())
                .isEqualTo(1);
    }

    @Test
    void shouldClosePalette() {

        CommandPaletteController controller =
                new CommandPaletteController(
                        new CommandCatalog(),
                        new CommandPaletteState());

        controller.open();

        controller.handleKey(
                KeyEvent.of(
                        KeyType.ESCAPE));

        assertThat(
                controller.active())
                .isFalse();
    }
}