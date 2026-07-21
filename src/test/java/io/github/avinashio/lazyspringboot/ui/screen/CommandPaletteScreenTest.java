package io.github.avinashio.lazyspringboot.ui.screen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.avinashio.lazyspringboot.ui.command.Command;
import io.github.avinashio.lazyspringboot.ui.component.ModalRenderer;
import io.github.avinashio.lazyspringboot.ui.component.TextFormatter;
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

        TextFormatter textFormatter =
                new TextFormatter();

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

        ModalRenderer modalRenderer =
                new ModalRenderer(
                        terminal,
                        textFormatter);

        CommandPaletteScreen screen =
                new CommandPaletteScreen(
                        modalRenderer);

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