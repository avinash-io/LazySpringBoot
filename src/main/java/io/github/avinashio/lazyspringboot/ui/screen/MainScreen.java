package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.ui.component.ProjectDetailsPanel;
import io.github.avinashio.lazyspringboot.ui.component.ProjectPanel;
import io.github.avinashio.lazyspringboot.ui.component.StatusBar;
import io.github.avinashio.lazyspringboot.ui.component.TextFormatter;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.PrintWriter;
import java.util.List;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.stereotype.Component;

@Component
public class MainScreen {

    private static final int MINIMUM_TERMINAL_WIDTH = 80;
    private static final int LEFT_PANEL_PERCENTAGE = 35;

    private final Terminal terminal;
    private final ProjectPanel projectPanel;
    private final ProjectDetailsPanel projectDetailsPanel;
    private final StatusBar statusBar;
    private final TextFormatter textFormatter;

    public MainScreen(
            Terminal terminal,
            ProjectPanel projectPanel,
            ProjectDetailsPanel projectDetailsPanel,
            StatusBar statusBar,
            TextFormatter textFormatter) {
        this.terminal = terminal;
        this.projectPanel = projectPanel;
        this.projectDetailsPanel = projectDetailsPanel;
        this.statusBar = statusBar;
        this.textFormatter = textFormatter;
    }

    public void render(UiState state) {
        PrintWriter writer = terminal.writer();

        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.puts(InfoCmp.Capability.cursor_address, 0, 0);

        int width = terminal.getWidth();

        if (width < MINIMUM_TERMINAL_WIDTH) {
            renderTerminalTooSmall(writer, width);
            writer.flush();
            return;
        }

        int leftPanelWidth = width * LEFT_PANEL_PERCENTAGE / 100;
        int rightPanelWidth = width - leftPanelWidth - 1;

        List<String> projectLines = projectPanel.render(state);
        List<String> detailLines =
                projectDetailsPanel.render(state.selectedProject());

        renderHeader(writer, leftPanelWidth, rightPanelWidth);

        renderPanels(
                writer,
                projectLines,
                detailLines,
                leftPanelWidth,
                rightPanelWidth);

        renderFooter(writer, width);

        writer.flush();
    }

    private void renderHeader(
            PrintWriter writer,
            int leftPanelWidth,
            int rightPanelWidth) {
        writer.print(
                "┌─ Projects "
                        + "─".repeat(leftPanelWidth - 12)
                        + "┬─ Project Details "
                        + "─".repeat(rightPanelWidth - 20)
                        + "┐");
    }

    private void renderPanels(
            PrintWriter writer,
            List<String> projectLines,
            List<String> detailLines,
            int leftPanelWidth,
            int rightPanelWidth) {
        int contentHeight =
                Math.max(projectLines.size(), detailLines.size());

        for (int row = 0; row < contentHeight; row++) {
            writer.println();

            String projectLine = lineAt(projectLines, row);
            String detailLine = lineAt(detailLines, row);

            writer.print("│");
            writer.print(
                    textFormatter.fit(projectLine, leftPanelWidth - 1));

            writer.print("│");
            writer.print(
                    textFormatter.fit(detailLine, rightPanelWidth - 1));

            writer.print("│");
        }
    }

    private String lineAt(List<String> lines, int index) {
        if (index >= lines.size()) {
            return "";
        }

        return lines.get(index);
    }

    private void renderFooter(PrintWriter writer, int width) {
        writer.println();

        writer.print("├");
        writer.print("─".repeat(width - 2));
        writer.print("┤");

        writer.println();

        writer.print("│");
        writer.print(
                textFormatter.fit(statusBar.render(), width - 2));
        writer.print("│");

        writer.println();

        writer.print("└");
        writer.print("─".repeat(width - 2));
        writer.print("┘");
    }

    private void renderTerminalTooSmall(
            PrintWriter writer, int width) {
        writer.println("LazySpringBoot");
        writer.println();
        writer.println(
                "Terminal width is too small: " + width);
        writer.println(
                "Minimum required width: "
                        + MINIMUM_TERMINAL_WIDTH);
    }
}