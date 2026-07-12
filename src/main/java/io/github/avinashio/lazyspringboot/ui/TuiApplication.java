package io.github.avinashio.lazyspringboot.ui;

import java.io.PrintWriter;
import org.jline.terminal.Terminal;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import io.github.avinashio.lazyspringboot.ui.terminal.TerminalManager;

@Component
public class TuiApplication implements ApplicationRunner {

    private final TerminalManager terminalManager;

    public TuiApplication(TerminalManager terminalManager) {
        this.terminalManager = terminalManager;
    }

    @Override
    public void run(ApplicationArguments args) {
        Terminal terminal = terminalManager.terminal();
        PrintWriter writer = terminal.writer();

        writer.println("LazySpringBoot");
        writer.println("Terminal size: " + terminal.getWidth() + "x" + terminal.getHeight());
        writer.flush();
    }
}