package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.PrintWriter;
import org.jline.terminal.Terminal;
import org.springframework.stereotype.Component;

@Component
public class MainScreen {

    private static final String CLEAR_SCREEN = "\033[2J";
    private static final String CURSOR_HOME = "\033[H";

    private final Terminal terminal;

    public MainScreen(Terminal terminal) {
        this.terminal = terminal;
    }

    public void render(UiState state) {
        PrintWriter writer = terminal.writer();

        writer.print(CLEAR_SCREEN);
        writer.print(CURSOR_HOME);

        writer.println("LazySpringBoot");
        writer.println();
        writer.println("Spring Boot projects");
        writer.println();

        for (int index = 0; index < state.projects().size(); index++) {
            String project = state.projects().get(index);

            if (index == state.selectedProjectIndex()) {
                writer.println("> " + project);
            } else {
                writer.println("  " + project);
            }
        }

        writer.println();
        writer.println("↑↓ Navigate    q Quit");

        writer.flush();
    }
}