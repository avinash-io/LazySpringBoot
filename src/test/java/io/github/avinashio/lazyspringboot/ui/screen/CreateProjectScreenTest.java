package io.github.avinashio.lazyspringboot.ui.screen;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.Test;

class CreateProjectScreenTest {

    @Test
    void shouldRenderScreen() {

        Terminal terminal =
                mock(Terminal.class);

        StringWriter output =
                new StringWriter();

        when(terminal.writer())
                .thenReturn(
                        new PrintWriter(output));

        CreateProjectScreen screen =
                new CreateProjectScreen(
                        terminal);

        screen.render(
                new CreateProjectState());

        verify(terminal)
                .writer();
    }
}