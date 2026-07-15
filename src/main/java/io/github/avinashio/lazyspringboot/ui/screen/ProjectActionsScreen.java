package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.action.ActionItem;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.PrintWriter;
import java.util.List;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.stereotype.Component;

@Component
public class ProjectActionsScreen {

    private final Terminal terminal;

    public ProjectActionsScreen(
            Terminal terminal) {
        this.terminal = terminal;
    }

    public void render(
            UiState state,
            List<ActionItem> actions) {
        PrintWriter writer =
                terminal.writer();

        terminal.puts(
                InfoCmp.Capability.clear_screen);

        terminal.puts(
                InfoCmp.Capability.cursor_address,
                0,
                0);

        writer.println("LazySpringBoot");
        writer.println();
        writer.println("Project Actions");
        writer.println();

        renderProject(
                writer,
                state.selectedProject());

        writer.println();

        renderActions(
                writer,
                state,
                actions);

        writer.println();
        writer.println(
                "↑↓ Navigate    Enter Execute"
                        + "    Esc Close");

        writer.flush();
    }

    private void renderProject(
            PrintWriter writer,
            SpringProject project) {
        if (project == null) {
            writer.println(
                    "Project: No project selected");

            return;
        }

        writer.println(
                "Project: " + project.name());
    }

    private void renderActions(
            PrintWriter writer,
            UiState state,
            List<ActionItem> actions) {
        for (int index = 0;
             index < actions.size();
             index++) {
            ActionItem item =
                    actions.get(index);

            String selectionMarker =
                    index
                            == state
                            .selectedProjectActionIndex()
                            ? ">"
                            : " ";

            String disabledMarker =
                    item.enabled()
                            ? ""
                            : " (disabled)";

            writer.println(
                    selectionMarker
                            + " "
                            + item.action()
                            .displayName()
                            + disabledMarker);
        }
    }
}