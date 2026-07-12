package io.github.avinashio.lazyspringboot.ui.terminal;

import java.io.IOException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.stereotype.Component;

@Component
public class TerminalManager implements AutoCloseable {

    private final Terminal terminal;

    public TerminalManager() throws IOException {
        this.terminal = TerminalBuilder.builder().system(true).build();
    }

    public Terminal terminal() {
        return terminal;
    }

    @Override
    public void close() throws IOException {
        terminal.close();
    }
}