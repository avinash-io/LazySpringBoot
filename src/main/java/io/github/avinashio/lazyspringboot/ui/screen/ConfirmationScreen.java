package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.component.TextFormatter;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.stereotype.Component;

@Component
public class ConfirmationScreen {

    private static final int MINIMUM_WIDTH = 50;

    private final Terminal terminal;
    private final TextFormatter textFormatter;

    public ConfirmationScreen(
            Terminal terminal,
            TextFormatter textFormatter) {
        this.terminal = terminal;
        this.textFormatter = textFormatter;
    }

    public void render(UiState state) {
        PrintWriter writer = terminal.writer();

        terminal.puts(
                InfoCmp.Capability.clear_screen);

        terminal.puts(
                InfoCmp.Capability.cursor_address,
                0,
                0);

        int width = terminal.getWidth();

        for (String line : buildLines(state, width)) {
            writer.println(line);
        }

        writer.flush();
    }

    List<String> buildLines(
            UiState state,
            int width) {
        if (width < MINIMUM_WIDTH) {
            return List.of(
                    "Terminal width is too small.");
        }

        SpringProject project =
                state.selectedProject();

        List<DependencyItem> selectedItems =
                state.selectedDependencyItems();

        List<String> lines =
                new ArrayList<>();

        lines.add("LazySpringBoot");
        lines.add("");
        lines.add("Apply dependencies?");
        lines.add("");
        lines.add(
                "Project: " + project.name());
        lines.add("");

        for (DependencyItem item : selectedItems) {
            lines.add(
                    "  + "
                            + item.dependency().name());
        }

        lines.add("");
        lines.add(
                textFormatter.fit(
                        " Enter Apply    Esc Cancel",
                        width));

        return List.copyOf(lines);
    }
}