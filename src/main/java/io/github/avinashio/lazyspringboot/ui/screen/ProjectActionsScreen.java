package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.action.ActionItem;
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
public class ProjectActionsScreen {

    private static final int POPUP_WIDTH = 50;

    private static final int MINIMUM_POPUP_WIDTH = 30;

    private static final int POPUP_PADDING = 4;

    private final Terminal terminal;

    private final TextFormatter textFormatter;

    public ProjectActionsScreen(
            Terminal terminal,
            TextFormatter textFormatter) {

        this.terminal = terminal;
        this.textFormatter = textFormatter;
    }

    public void render(
            UiState state,
            List<ActionItem> actions) {

        PrintWriter writer =
                terminal.writer();

        int terminalWidth =
                terminal.getWidth();

        int terminalHeight =
                terminal.getHeight();

        int popupWidth =
                Math.min(
                        POPUP_WIDTH,
                        terminalWidth
                                - POPUP_PADDING);

        if (popupWidth
                < MINIMUM_POPUP_WIDTH) {
            return;
        }

        List<String> content =
                buildContent(
                        state,
                        actions);

        int popupHeight =
                content.size()
                        + 4;

        int startColumn =
                Math.max(
                        0,
                        (terminalWidth
                                - popupWidth)
                                / 2);

        int startRow =
                Math.max(
                        0,
                        (terminalHeight
                                - popupHeight)
                                / 2);

        renderHeader(
                writer,
                startRow,
                startColumn,
                popupWidth);

        renderContent(
                writer,
                content,
                startRow,
                startColumn,
                popupWidth);

        renderFooter(
                writer,
                startRow,
                startColumn,
                popupWidth,
                content.size());

        writer.flush();
    }

    private List<String> buildContent(
            UiState state,
            List<ActionItem> actions) {

        List<String> lines =
                new ArrayList<>();

        SpringProject project =
                state.selectedProject();

        if (project == null) {
            lines.add(
                    " Project: No project selected");
        } else {
            lines.add(
                    " Project: "
                            + project.name());
        }

        lines.add("");

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

            lines.add(
                    " "
                            + selectionMarker
                            + " "
                            + item.action()
                            .displayName()
                            + disabledMarker);
        }

        return lines;
    }

    private void renderHeader(
            PrintWriter writer,
            int startRow,
            int startColumn,
            int width) {

        moveCursor(
                startRow,
                startColumn);

        String title =
                "Project Actions";

        writer.print(
                "┌─ "
                        + title
                        + " "
                        + "─".repeat(
                        Math.max(
                                0,
                                width
                                        - title.length()
                                        - 5))
                        + "┐");
    }

    private void renderContent(
            PrintWriter writer,
            List<String> lines,
            int startRow,
            int startColumn,
            int width) {

        for (int index = 0;
             index < lines.size();
             index++) {

            moveCursor(
                    startRow
                            + index
                            + 1,
                    startColumn);

            writer.print("│");

            writer.print(
                    textFormatter.fit(
                            lines.get(index),
                            width - 2));

            writer.print("│");
        }
    }

    private void renderFooter(
            PrintWriter writer,
            int startRow,
            int startColumn,
            int width,
            int contentSize) {

        int separatorRow =
                startRow
                        + contentSize
                        + 1;

        moveCursor(
                separatorRow,
                startColumn);

        writer.print("├");

        writer.print(
                "─".repeat(
                        width - 2));

        writer.print("┤");

        moveCursor(
                separatorRow + 1,
                startColumn);

        writer.print("│");

        writer.print(
                textFormatter.fit(
                        " ↑↓ Navigate"
                                + "  Enter Execute"
                                + "  Esc Close",
                        width - 2));

        writer.print("│");

        moveCursor(
                separatorRow + 2,
                startColumn);

        writer.print("└");

        writer.print(
                "─".repeat(
                        width - 2));

        writer.print("┘");
    }

    private void moveCursor(
            int row,
            int column) {

        terminal.puts(
                InfoCmp.Capability.cursor_address,
                row,
                column);
    }
}