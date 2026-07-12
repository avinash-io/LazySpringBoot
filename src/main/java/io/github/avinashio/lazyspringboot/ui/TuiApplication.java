package io.github.avinashio.lazyspringboot.ui;

import io.github.avinashio.lazyspringboot.ui.input.Key;
import io.github.avinashio.lazyspringboot.ui.input.KeyReader;
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

    private final Terminal terminal;
    private final KeyReader keyReader;

    public TuiApplication(Terminal terminal, KeyReader keyReader) {
        this.terminal = terminal;
        this.keyReader = keyReader;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        PrintWriter writer = terminal.writer();

        terminal.enterRawMode();

        writer.println("LazySpringBoot");
        writer.println("Terminal size: " + terminal.getWidth() + "x" + terminal.getHeight());
        writer.println();
        writer.println("Press arrow keys");
        writer.println("Press q to quit");
        writer.flush();

        while (true) {
            Key key = keyReader.read();

            if (key == Key.QUIT) {
                break;
            }

            writer.println("Key: " + key);
            writer.flush();
        }
    }
}