package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectActionOutput;
import io.github.avinashio.lazyspringboot.ui.component.TextFormatter;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.PrintWriter;
import java.util.List;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.stereotype.Component;

@Component
public class ProjectActionOutputScreen {

    private static final int WIDTH_PERCENTAGE = 80;

    private static final int HEIGHT_PERCENTAGE = 80;

    private static final int MINIMUM_POPUP_WIDTH = 50;

    private static final int MINIMUM_POPUP_HEIGHT = 12;

    private static final int OUTPUT_HEADER_HEIGHT = 4;

    private static final int OUTPUT_FOOTER_HEIGHT = 4;

    private final Terminal terminal;

    private final TextFormatter textFormatter;

    public ProjectActionOutputScreen(
            Terminal terminal,
            TextFormatter textFormatter) {

        this.terminal =
                terminal;

        this.textFormatter =
                textFormatter;
    }

    public void render(
            UiState state) {

        ProjectActionOutput output =
                state.projectActionOutput();

        if (output == null) {
            return;
        }

        PrintWriter writer =
                terminal.writer();

        int popupWidth =
                popupWidth();

        int popupHeight =
                popupHeight();

        int startColumn =
                Math.max(
                        0,
                        (terminal.getWidth()
                                - popupWidth)
                                / 2);

        int startRow =
                Math.max(
                        0,
                        (terminal.getHeight()
                                - popupHeight)
                                / 2);

        int visibleHeight =
                visibleHeight();

        int offset =
                state
                        .outputViewport()
                        .offset();

        renderHeader(
                writer,
                output,
                startRow,
                startColumn,
                popupWidth);

        renderOutput(
                writer,
                output.lines(),
                offset,
                visibleHeight,
                startRow,
                startColumn,
                popupWidth);

        renderFooter(
                writer,
                output,
                offset,
                visibleHeight,
                startRow,
                startColumn,
                popupWidth);

        writer.flush();
    }

    public int visibleHeight() {

        return Math.max(
                1,
                popupHeight()
                        - OUTPUT_HEADER_HEIGHT
                        - OUTPUT_FOOTER_HEIGHT);
    }

    private int popupWidth() {

        int terminalWidth =
                terminal.getWidth();

        int calculatedWidth =
                terminalWidth
                        * WIDTH_PERCENTAGE
                        / 100;

        return Math.min(
                terminalWidth,
                Math.max(
                        MINIMUM_POPUP_WIDTH,
                        calculatedWidth));
    }

    private int popupHeight() {

        int terminalHeight =
                terminal.getHeight();

        int calculatedHeight =
                terminalHeight
                        * HEIGHT_PERCENTAGE
                        / 100;

        return Math.min(
                terminalHeight,
                Math.max(
                        MINIMUM_POPUP_HEIGHT,
                        calculatedHeight));
    }

    private void renderHeader(
            PrintWriter writer,
            ProjectActionOutput output,
            int startRow,
            int startColumn,
            int width) {

        moveCursor(
                startRow,
                startColumn);

        String title =
                buildTitle(output);

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

        moveCursor(
                startRow + 1,
                startColumn);

        writer.print("│");

        writer.print(
                textFormatter.fit(
                        " Project: "
                                + output.projectName(),
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

    private void renderOutput(
            PrintWriter writer,
            List<String> lines,
            int offset,
            int visibleHeight,
            int startRow,
            int startColumn,
            int width) {

        int endIndex =
                Math.min(
                        lines.size(),
                        offset + visibleHeight);

        for (int row = 0;
             row < visibleHeight;
             row++) {

            moveCursor(
                    startRow
                            + OUTPUT_HEADER_HEIGHT
                            - 1
                            + row,
                    startColumn);

            writer.print("│");

            String line =
                    row
                            < endIndex - offset
                            ? lines.get(
                            offset + row)
                            : "";

            writer.print(
                    textFormatter.fit(
                            line,
                            width - 2));

            writer.print("│");
        }
    }

    private void renderFooter(
            PrintWriter writer,
            ProjectActionOutput output,
            int offset,
            int visibleHeight,
            int startRow,
            int startColumn,
            int width) {

        int footerStartRow =
                startRow
                        + OUTPUT_HEADER_HEIGHT
                        - 1
                        + visibleHeight;

        moveCursor(
                footerStartRow,
                startColumn);

        writer.print("├");

        writer.print(
                "─".repeat(
                        width - 2));

        writer.print("┤");

        moveCursor(
                footerStartRow + 1,
                startColumn);

        writer.print("│");

        writer.print(
                textFormatter.fit(
                        " "
                                + buildPositionText(
                                output.lines().size(),
                                offset,
                                visibleHeight),
                        width - 2));

        writer.print("│");

        moveCursor(
                footerStartRow + 2,
                startColumn);

        writer.print("│");

        writer.print(
                textFormatter.fit(
                        " "
                                + buildNavigationText(
                                output,
                                offset,
                                visibleHeight),
                        width - 2));

        writer.print("│");

        moveCursor(
                footerStartRow + 3,
                startColumn);

        writer.print("└");

        writer.print(
                "─".repeat(
                        width - 2));

        writer.print("┘");
    }

    private String buildTitle(
            ProjectActionOutput output) {

        return output
                .action()
                .displayName()
                + " Output";
    }

    private String buildNavigationText(
            ProjectActionOutput output,
            int offset,
            int visibleHeight) {

        String navigation =
                "↑↓ Scroll"
                        + "    PgUp/PgDn Page"
                        + "    g Top"
                        + "    G Bottom"
                        + "    Esc Close";

        if (output.action()
                != ProjectAction.VIEW_LOGS) {
            return navigation;
        }

        String followStatus =
                isAtBottom(
                        output.lines().size(),
                        offset,
                        visibleHeight)
                        ? "[Following]"
                        : "[Paused]";

        return followStatus
                + "    "
                + navigation;
    }

    private boolean isAtBottom(
            int contentSize,
            int offset,
            int visibleHeight) {

        int maximumOffset =
                Math.max(
                        0,
                        contentSize
                                - visibleHeight);

        return offset
                >= maximumOffset;
    }

    private String buildPositionText(
            int contentSize,
            int offset,
            int visibleHeight) {

        if (contentSize == 0) {
            return "Lines 0-0 of 0";
        }

        int firstLine =
                offset + 1;

        int lastLine =
                Math.min(
                        contentSize,
                        offset
                                + visibleHeight);

        return "Lines "
                + firstLine
                + "-"
                + lastLine
                + " of "
                + contentSize;
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