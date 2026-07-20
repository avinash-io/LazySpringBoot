package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.ui.command.Command;
import java.io.PrintWriter;
import java.util.List;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.stereotype.Component;

@Component
public class CommandPaletteScreen {

    private static final int POPUP_WIDTH = 60;

    private static final int MINIMUM_POPUP_WIDTH = 40;

    private static final int MAXIMUM_VISIBLE_COMMANDS = 10;

    private static final int POPUP_PADDING = 4;

    private final Terminal terminal;

    public CommandPaletteScreen(
            Terminal terminal) {

        this.terminal = terminal;
    }

    public void render(
            List<Command> commands,
            int selectedIndex,
            String searchQuery) {

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

        int visibleCommandCount =
                Math.min(
                        commands.size(),
                        MAXIMUM_VISIBLE_COMMANDS);

        int popupHeight =
                visibleCommandCount
                        + 6;

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

        renderSearch(
                writer,
                searchQuery,
                startRow,
                startColumn,
                popupWidth);

        renderCommands(
                writer,
                commands,
                selectedIndex,
                visibleCommandCount,
                startRow,
                startColumn,
                popupWidth);

        renderFooter(
                writer,
                visibleCommandCount,
                startRow,
                startColumn,
                popupWidth);

        writer.flush();
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
                "Command Palette";

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

    private void renderSearch(
            PrintWriter writer,
            String searchQuery,
            int startRow,
            int startColumn,
            int width) {

        moveCursor(
                startRow + 1,
                startColumn);

        writer.print("│");

        writer.print(
                fit(
                        " Search: "
                                + searchQuery
                                + "_",
                        width - 2));

        writer.print("│");

        moveCursor(
                startRow + 2,
                startColumn);

        writer.print("├");

        writer.print(
                "─".repeat(
                        width - 2));

        writer.print("┤");
    }

    private void renderCommands(
            PrintWriter writer,
            List<Command> commands,
            int selectedIndex,
            int visibleCommandCount,
            int startRow,
            int startColumn,
            int width) {

        int firstVisibleIndex =
                calculateFirstVisibleIndex(
                        commands.size(),
                        selectedIndex,
                        visibleCommandCount);

        for (int row = 0;
             row < visibleCommandCount;
             row++) {

            int commandIndex =
                    firstVisibleIndex
                            + row;

            moveCursor(
                    startRow
                            + 3
                            + row,
                    startColumn);

            writer.print("│");

            String line = "";

            if (commandIndex
                    < commands.size()) {

                String prefix =
                        commandIndex
                                == selectedIndex
                                ? " > "
                                : "   ";

                line =
                        prefix
                                + commands
                                .get(commandIndex)
                                .title();
            }

            writer.print(
                    fit(
                            line,
                            width - 2));

            writer.print("│");
        }
    }

    private void renderFooter(
            PrintWriter writer,
            int visibleCommandCount,
            int startRow,
            int startColumn,
            int width) {

        int separatorRow =
                startRow
                        + 3
                        + visibleCommandCount;

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
                fit(
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

    private int calculateFirstVisibleIndex(
            int commandCount,
            int selectedIndex,
            int visibleCommandCount) {

        if (commandCount
                <= visibleCommandCount) {
            return 0;
        }

        int firstVisibleIndex =
                selectedIndex
                        - visibleCommandCount
                        + 1;

        return Math.max(
                0,
                Math.min(
                        firstVisibleIndex,
                        commandCount
                                - visibleCommandCount));
    }

    private String fit(
            String value,
            int width) {

        if (value.length()
                > width) {

            return value.substring(
                    0,
                    width);
        }

        return value
                + " ".repeat(
                width
                        - value.length());
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