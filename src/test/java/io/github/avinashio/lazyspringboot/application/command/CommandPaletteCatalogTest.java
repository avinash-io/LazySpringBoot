package io.github.avinashio.lazyspringboot.application.command;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.command.CommandPaletteItem;
import org.junit.jupiter.api.Test;

class CommandPaletteCatalogTest {

    private final CommandPaletteCatalog catalog =
            new CommandPaletteCatalog();

    @Test
    void shouldReturnCommands() {

        assertThat(catalog.commands())
                .containsExactly(
                        CommandPaletteItem.BUILD,
                        CommandPaletteItem.TEST,
                        CommandPaletteItem.RUN,
                        CommandPaletteItem.STOP,
                        CommandPaletteItem.VIEW_LOGS,
                        CommandPaletteItem.ADD_DEPENDENCY,
                        CommandPaletteItem.REMOVE_DEPENDENCY,
                        CommandPaletteItem.CREATE_PROJECT,
                        CommandPaletteItem.REFRESH_PROJECTS);
    }
}