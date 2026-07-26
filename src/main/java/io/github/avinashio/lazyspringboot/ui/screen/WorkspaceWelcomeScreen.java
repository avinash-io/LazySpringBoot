package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.ui.component.TextFormatter;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.nio.file.Paths;

@Component
public class WorkspaceWelcomeScreen {

    private static final int MINIMUM_TERMINAL_WIDTH =
            80;

    private final Terminal terminal;

    private final TextFormatter textFormatter;

    public WorkspaceWelcomeScreen(
            Terminal terminal,
            TextFormatter textFormatter) {

        this.terminal =
                terminal;

        this.textFormatter =
                textFormatter;


    }

    public void render(
            UiState state) {

        PrintWriter writer =
                terminal.writer();

        terminal.puts(
                InfoCmp.Capability.clear_screen);

        terminal.puts(
                InfoCmp.Capability.cursor_address,
                0,
                0);

        int terminalWidth =
                terminal.getWidth();

        int terminalHeight =
                terminal.getHeight();

        if (terminalWidth < MINIMUM_TERMINAL_WIDTH) {

            renderTerminalTooSmall(
                    writer,
                    terminalWidth);

            writer.flush();

            return;
        }

        int cardWidth =
                Math.min(
                        84,
                        terminalWidth - 6);

        int cardHeight =
                15;

        int leftPadding =
                Math.max(
                        0,
                        (terminalWidth - cardWidth) / 2);

        int topPadding =
                Math.max(
                        2,
                        (terminalHeight - cardHeight) / 2);

        for (int index = 0;
             index < topPadding;
             index++) {

            writer.println();
        }

        renderBorder(
                writer,
                leftPadding,
                cardWidth,
                '┌',
                '┐');

        renderCentered(
                writer,
                leftPadding,
                cardWidth,
                "🚀 LazySpringBoot");

        renderLine(
                writer,
                leftPadding,
                cardWidth,
                "");

        renderCentered(
                writer,
                leftPadding,
                cardWidth,
                "Terminal Workspace for Spring Boot Developers");

        renderDivider(
                writer,
                leftPadding,
                cardWidth);

        renderLine(
                writer,
                leftPadding,
                cardWidth,
                "No Spring Boot projects found in this workspace.");

        renderLine(
                writer,
                leftPadding,
                cardWidth,
                "");

        renderLine(
                writer,
                leftPadding,
                cardWidth,
                "Workspace");

        renderLine(
                writer,
                leftPadding,
                cardWidth,
                Paths.get("")
                        .toAbsolutePath()
                        .toString());

        renderLine(
                writer,
                leftPadding,
                cardWidth,
                "");

        renderLine(
                writer,
                leftPadding,
                cardWidth,
                "Quick Actions");

        renderLine(
                writer,
                leftPadding,
                cardWidth,
                "[N] Create Spring Boot Project");

        renderLine(
                writer,
                leftPadding,
                cardWidth,
                "[R] Refresh Workspace");

        renderLine(
                writer,
                leftPadding,
                cardWidth,
                "[Q] Quit");

        renderDivider(
                writer,
                leftPadding,
                cardWidth);

        renderLine(
                writer,
                leftPadding,
                cardWidth,
                "Press Ctrl+P for Command Palette");

        renderBorder(
                writer,
                leftPadding,
                cardWidth,
                '└',
                '┘');

        writer.flush();
    }

    private void renderCentered(
            PrintWriter writer,
            int leftPadding,
            int width,
            String text) {

        int visibleWidth =
                text.length();

        int padding =
                Math.max(
                        0,
                        (width - 2 - visibleWidth) / 2);

        renderLine(
                writer,
                leftPadding,
                width,
                " ".repeat(
                        padding)
                        + text);
    }

    private void renderDivider(
            PrintWriter writer,
            int leftPadding,
            int width) {

        renderBorder(
                writer,
                leftPadding,
                width,
                '├',
                '┤');
    }

    private void renderLine(
            PrintWriter writer,
            int leftPadding,
            int width,
            String text) {

        writer.print(
                " ".repeat(
                        leftPadding));

        writer.print(
                "│");

        writer.print(
                textFormatter.fit(
                        text,
                        width - 2));

        writer.print(
                "│");

        writer.println();
    }

    private void renderTerminalTooSmall(
            PrintWriter writer,
            int width) {

        writer.println(
                "LazySpringBoot");

        writer.println();

        writer.println(
                "Terminal width is too small: "
                        + width);

        writer.println(
                "Minimum required width: "
                        + MINIMUM_TERMINAL_WIDTH);
    }

    private void renderBorder(
            PrintWriter writer,
            int leftPadding,
            int width,
            char left,
            char right) {

        writer.print(
                " ".repeat(
                        leftPadding));

        writer.print(
                left);

        writer.print(
                "─".repeat(
                        width - 2));

        writer.print(
                right);

        writer.println();
    }
}