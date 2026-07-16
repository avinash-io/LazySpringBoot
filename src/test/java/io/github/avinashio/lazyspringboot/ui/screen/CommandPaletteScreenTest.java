package io.github.avinashio.lazyspringboot.ui.screen;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import io.github.avinashio.lazyspringboot.domain.command.CommandPaletteItem;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.Test;

class CommandPaletteScreenTest {

    @Test
    void shouldRenderCommandPalette() {

        Terminal terminal =
                mock(Terminal.class);

        StringWriter output =
                new StringWriter();

        PrintWriter writer =
                new PrintWriter(output);

        org.mockito.Mockito.when(
                        terminal.writer())
                .thenReturn(writer);

        CommandPaletteScreen screen =
                new CommandPaletteScreen(
                        terminal);

        screen.render(
                List.of(
                        CommandPaletteItem.BUILD,
                        CommandPaletteItem.RUN),
                0);

        writer.flush();

        verify(terminal)
                .writer();
    }
}