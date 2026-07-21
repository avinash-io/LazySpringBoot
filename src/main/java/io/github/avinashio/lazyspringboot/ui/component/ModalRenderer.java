package io.github.avinashio.lazyspringboot.ui.component;

import java.io.PrintWriter;
import java.util.List;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.stereotype.Component;

@Component
public class ModalRenderer {

    private final Terminal terminal;

    private final TextFormatter textFormatter;

    public ModalRenderer(
            Terminal terminal,
            TextFormatter textFormatter) {

        this.terminal = terminal;
        this.textFormatter = textFormatter;
    }

    public boolean renderFixedWidth(
            String title,
            List<String> content,
            String footer,
            int preferredWidth,
            int minimumWidth,
            int padding) {

        int terminalWidth =
                terminal.getWidth();

        int terminalHeight =
                terminal.getHeight();

        int width =
                Math.min(
                        preferredWidth,
                        terminalWidth - padding);

        if (width < minimumWidth) {
            return false;
        }

        int height =
                content.size() + 4;

        int startColumn =
                Math.max(
                        0,
                        (terminalWidth - width) / 2);

        int startRow =
                Math.max(
                        0,
                        (terminalHeight - height) / 2);

        PrintWriter writer =
                terminal.writer();

        renderHeader(
                writer,
                title,
                startRow,
                startColumn,
                width);

        renderContent(
                writer,
                content,
                startRow,
                startColumn,
                width);

        renderFooter(
                writer,
                footer,
                startRow,
                startColumn,
                width,
                content.size());

        writer.flush();

        return true;
    }

    private void renderHeader(
            PrintWriter writer,
            String title,
            int startRow,
            int startColumn,
            int width) {

        moveCursor(
                startRow,
                startColumn);

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
            List<String> content,
            int startRow,
            int startColumn,
            int width) {

        for (int index = 0;
             index < content.size();
             index++) {

            moveCursor(
                    startRow + index + 1,
                    startColumn);

            writer.print("│");

            writer.print(
                    textFormatter.fit(
                            content.get(index),
                            width - 2));

            writer.print("│");
        }
    }

    private void renderFooter(
            PrintWriter writer,
            String footer,
            int startRow,
            int startColumn,
            int width,
            int contentSize) {

        int separatorRow =
                startRow + contentSize + 1;

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
                        footer,
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

    public void renderPercentageSize(
            String title,
            List<String> content,
            String footer,
            int widthPercentage,
            int heightPercentage,
            int minimumWidth,
            int minimumHeight) {

        int terminalWidth =
                terminal.getWidth();

        int terminalHeight =
                terminal.getHeight();

        int calculatedWidth =
                terminalWidth
                        * widthPercentage
                        / 100;

        int calculatedHeight =
                terminalHeight
                        * heightPercentage
                        / 100;

        int width =
                Math.min(
                        terminalWidth,
                        Math.max(
                                minimumWidth,
                                calculatedWidth));

        int height =
                Math.min(
                        terminalHeight,
                        Math.max(
                                minimumHeight,
                                calculatedHeight));

        int startColumn =
                Math.max(
                        0,
                        (terminalWidth - width) / 2);

        int startRow =
                Math.max(
                        0,
                        (terminalHeight - height) / 2);

        PrintWriter writer =
                terminal.writer();

        renderHeader(
                writer,
                title,
                startRow,
                startColumn,
                width);

        renderFixedHeightContent(
                writer,
                content,
                startRow,
                startColumn,
                width,
                height);

        renderFixedHeightFooter(
                writer,
                footer,
                startRow,
                startColumn,
                width,
                height);

        writer.flush();
    }

    private void renderFixedHeightContent(
            PrintWriter writer,
            List<String> content,
            int startRow,
            int startColumn,
            int width,
            int height) {

        int contentHeight =
                Math.max(
                        1,
                        height - 4);

        for (int row = 0;
             row < contentHeight;
             row++) {

            moveCursor(
                    startRow + row + 1,
                    startColumn);

            writer.print("│");

            String line =
                    row < content.size()
                            ? content.get(row)
                            : "";

            writer.print(
                    textFormatter.fit(
                            line,
                            width - 2));

            writer.print("│");
        }
    }

    private void renderFixedHeightFooter(
            PrintWriter writer,
            String footer,
            int startRow,
            int startColumn,
            int width,
            int height) {

        int separatorRow =
                startRow + height - 3;

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
                        footer,
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
}