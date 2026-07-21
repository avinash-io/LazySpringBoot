package io.github.avinashio.lazyspringboot.ui.screen;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.Test;
import io.github.avinashio.lazyspringboot.ui.component.ModalRenderer;
import io.github.avinashio.lazyspringboot.ui.component.TextFormatter;

class CreateProjectScreenTest {

    @Test
    void shouldRenderScreen() {

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

        TextFormatter textFormatter =
                new TextFormatter();

        ModalRenderer modalRenderer =
                new ModalRenderer(
                        terminal,
                        textFormatter);

        CreateProjectScreen screen =
                new CreateProjectScreen(
                        modalRenderer);

        screen.render(
                new CreateProjectState());

        writer.flush();

        verify(terminal)
                .writer();
    }
}