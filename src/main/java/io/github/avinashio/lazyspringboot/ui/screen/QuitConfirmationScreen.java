package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.ui.component.ModalRenderer;
import io.github.avinashio.lazyspringboot.ui.state.QuitFocus;
import io.github.avinashio.lazyspringboot.ui.state.QuitOption;
import io.github.avinashio.lazyspringboot.ui.state.QuitPhase;
import io.github.avinashio.lazyspringboot.ui.state.QuitState;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class QuitConfirmationScreen {

    private static final int POPUP_WIDTH = 56;

    private static final int MINIMUM_POPUP_WIDTH = 36;

    private static final int POPUP_PADDING = 4;

    private static final String FOOTER =
            " ↑↓ Navigate"
                    + "  Tab Switch"
                    + "  Space Toggle"
                    + "  A All"
                    + "  N None"
                    + "  Enter Select"
                    + "  Esc Cancel";

    private final ModalRenderer modalRenderer;

    public QuitConfirmationScreen(
            ModalRenderer modalRenderer) {

        this.modalRenderer =
                modalRenderer;
    }

    public void render(
            QuitState state) {

        modalRenderer.renderFixedWidth(
                "Quit LazySpringBoot",
                buildContent(state),
                FOOTER,
                POPUP_WIDTH,
                MINIMUM_POPUP_WIDTH,
                POPUP_PADDING);
    }

    List<String> buildContent(
            QuitState state) {

        List<String> lines =
                new ArrayList<>();

        if (state.phase()
                == QuitPhase.RUNNING_PROJECTS) {

            lines.add(
                    state.focus()
                            == QuitFocus.PROJECTS
                            ? "▶ Running projects"
                            : "  Running projects");

            lines.add("");

            lines.add(
                    " "
                            + state.selectedRunningProjectCount()
                            + " selected of "
                            + state.runningProjectCount()
                            + " running project"
                            + (state.runningProjectCount() == 1
                            ? ""
                            : "s"));

            lines.add("");

            List<String> runningProjects =
                    state.runningProjects();

            int visibleRows = 8;

            int offset =
                    state.projectViewport()
                            .offset();

            int end =
                    Math.min(
                            offset + visibleRows,
                            runningProjects.size());

            if (offset > 0) {

                lines.add(
                        "   ↑");
            }

            for (int index = offset;
                 index < end;
                 index++) {

                String project =
                        runningProjects.get(
                                index);

                boolean selected =
                        state.selectedRunningProjects()
                                .contains(project);

                String cursor =
                        state.focus()
                                == QuitFocus.PROJECTS
                                && state.selectedRunningProjectIndex()
                                == index
                                ? ">"
                                : " ";

                String checkbox =
                        selected
                                ? "[x]"
                                : "[ ]";

                lines.add(
                        " "
                                + cursor
                                + " "
                                + checkbox
                                + " "
                                + project);
            }

            if (end < runningProjects.size()) {

                lines.add(
                        "   ↓");
            }

            lines.add("");

            lines.add(
                    "────────────────────────");

            lines.add("");

            lines.add(
                    state.focus()
                            == QuitFocus.ACTIONS
                            ? "▶ Actions"
                            : "  Actions");

            lines.add("");

            lines.add(
                    " What would you like to do?");

        } else {

            lines.add(
                    " ✓ All running projects have");

            lines.add(
                    "   been stopped.");

            lines.add("");

            lines.add(
                    "▶ Actions");

            lines.add("");

            lines.add(
                    " What would you like to do?");
        }

        lines.add("");

        List<QuitOption> options =
                state.options();

        for (int index = 0;
             index < options.size();
             index++) {

            String marker =
                    state.focus()
                            == QuitFocus.ACTIONS
                            && index
                            == state.selectedOptionIndex()
                            ? ">"
                            : " ";

            lines.add(
                    " "
                            + marker
                            + " "
                            + optionLabel(
                            options.get(index)));
        }

        return lines;
    }

    private String optionLabel(
            QuitOption option) {

        return switch (option) {

            case STOP_RUNNING ->
                    "Stop running projects";

            case QUIT_ANYWAY ->
                    "Quit without stopping";

            case CANCEL ->
                    "Cancel";

            case QUIT ->
                    "Quit LazySpringBoot";
        };
    }
}