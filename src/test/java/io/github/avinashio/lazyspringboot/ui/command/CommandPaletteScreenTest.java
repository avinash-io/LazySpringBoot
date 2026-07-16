package io.github.avinashio.lazyspringboot.ui.command;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.Test;

class CommandPaletteScreenTest {

    @Test
    void shouldRenderPalette() {

        Terminal terminal =
                mock(Terminal.class);

        StringWriter output =
                new StringWriter();

        when(terminal.writer())
                .thenReturn(
                        new PrintWriter(output));

        CommandPaletteScreen screen =
                new CommandPaletteScreen(
                        terminal);

        screen.render(
                new CommandPaletteState(),
                List.of(
                        new Command(
                                "create-project",
                                "Create Project")));

        verify(terminal)
                .writer();
    }
}