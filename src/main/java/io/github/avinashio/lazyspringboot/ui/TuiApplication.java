package io.github.avinashio.lazyspringboot.ui;

import io.github.avinashio.lazyspringboot.ui.terminal.TerminalManager;
import java.io.PrintWriter;
import org.jline.terminal.Terminal;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "lazyspringboot.tui.enabled",
        havingValue = "true",
        matchIfMissing = true)
public class TuiApplication implements ApplicationRunner {

    private final TerminalManager terminalManager;

    public TuiApplication(TerminalManager terminalManager) {
        this.terminalManager = terminalManager;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Terminal terminal = terminalManager.terminal();
        PrintWriter writer = terminal.writer();

        terminal.enterRawMode();

        writer.println("LazySpringBoot");
        writer.println("Terminal size: " + terminal.getWidth() + "x" + terminal.getHeight());
        writer.println();
        writer.println("Press q to quit");
        writer.flush();

        while (true) {
            int key = terminal.reader().read();

            if (key == 'q') {
                break;
            }
        }
    }
}