package io.github.avinashio.lazyspringboot.ui.screen;

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

    private static final int HEADER_HEIGHT = 7;

    private static final int FOOTER_HEIGHT = 4;

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
        PrintWriter writer =
                terminal.writer();

        terminal.puts(
                InfoCmp.Capability.clear_screen);

        terminal.puts(
                InfoCmp.Capability.cursor_address,
                0,
                0);

        ProjectActionOutput output =
                state.projectActionOutput();

        if (output == null) {
            writer.flush();

            return;
        }

        int width =
                terminal.getWidth();

        int visibleHeight =
                visibleHeight();

        int offset =
                state
                        .outputViewport()
                        .offset();

        writer.println("LazySpringBoot");
        writer.println();

        writer.println(
                buildTitle(output));

        writer.println(
                "Project: "
                        + output.projectName());

        writer.println(
                "Action: "
                        + output
                        .action()
                        .displayName());

        writer.println(
                "Exit Code: "
                        + output.exitCode());

        writer.println();

        renderOutput(
                writer,
                output.lines(),
                offset,
                visibleHeight,
                width);

        writer.println();

        writer.println(
                buildPositionText(
                        output.lines().size(),
                        offset,
                        visibleHeight));

        writer.println(
                "↑↓ Scroll"
                        + "    PgUp/PgDn Page"
                        + "    g Top"
                        + "    G Bottom"
                        + "    Esc Close");

        writer.flush();
    }

    public int visibleHeight() {
        return Math.max(
                1,
                terminal.getHeight()
                        - HEADER_HEIGHT
                        - FOOTER_HEIGHT);
    }

    private String buildTitle(
            ProjectActionOutput output) {
        return output
                .action()
                .displayName()
                + " Output";
    }

    private void renderOutput(
            PrintWriter writer,
            List<String> lines,
            int offset,
            int visibleHeight,
            int width) {
        int endIndex =
                Math.min(
                        lines.size(),
                        offset + visibleHeight);

        for (int index = offset;
             index < endIndex;
             index++) {
            writer.println(
                    textFormatter.fit(
                            lines.get(index),
                            width));
        }

        int renderedLineCount =
                endIndex - offset;

        for (int index = renderedLineCount;
             index < visibleHeight;
             index++) {
            writer.println();
        }
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
                        offset + visibleHeight);

        return "Lines "
                + firstLine
                + "-"
                + lastLine
                + " of "
                + contentSize;
    }
}