package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.PrintWriter;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.stereotype.Component;

@Component
public class MainScreen {

    private final Terminal terminal;

    public MainScreen(Terminal terminal) {
        this.terminal = terminal;
    }

    public void render(UiState state) {
        PrintWriter writer = terminal.writer();

        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);

        writer.println("LazySpringBoot");
        writer.println();
        writer.println("Spring Boot projects");
        writer.println();

        if (state.projects().isEmpty()) {
            writer.println("No Spring Boot projects found.");
        }

        for (int index = 0; index < state.projects().size(); index++) {
            SpringProject project = state.projects().get(index);
            String prefix = index == state.selectedProjectIndex() ? "> " : "  ";

            writer.println(prefix + project.name());
        }

        writer.println();
        writer.print("↑↓ Navigate    q Quit");
        writer.flush();
    }
}