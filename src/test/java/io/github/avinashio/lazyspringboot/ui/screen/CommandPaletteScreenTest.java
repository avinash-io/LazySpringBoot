package io.github.avinashio.lazyspringboot.ui.screen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.avinashio.lazyspringboot.ui.command.Command;
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

        when(terminal.writer())
                .thenReturn(writer);

        when(terminal.getWidth())
                .thenReturn(120);

        when(terminal.getHeight())
                .thenReturn(40);

        CommandPaletteScreen screen =
                new CommandPaletteScreen(
                        terminal);

        screen.render(
                List.of(
                        new Command(
                                "build-project",
                                "Build Project"),
                        new Command(
                                "run-project",
                                "Run Project")),
                0,
                "build");

        writer.flush();

        verify(terminal)
                .writer();

        assertThat(
                output.toString())
                .contains(
                        "Command Palette")
                .contains(
                        "Search: build_")
                .contains(
                        "> Build Project")
                .contains(
                        "Run Project")
                .contains(
                        "Enter Execute")
                .contains(
                        "Esc Close");
    }
}