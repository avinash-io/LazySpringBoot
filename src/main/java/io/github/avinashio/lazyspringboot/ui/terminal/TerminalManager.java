package io.github.avinashio.lazyspringboot.ui.terminal;

import java.io.IOException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TerminalManager {

    @Bean(destroyMethod = "close")
    public Terminal terminal() throws IOException {
        return TerminalBuilder.builder().system(true).build();
    }
}