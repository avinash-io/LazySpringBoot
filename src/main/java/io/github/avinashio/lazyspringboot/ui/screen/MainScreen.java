package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.ui.component.DependencyPanel;
import io.github.avinashio.lazyspringboot.ui.component.ProjectDetailsPanel;
import io.github.avinashio.lazyspringboot.ui.component.ProjectPanel;
import io.github.avinashio.lazyspringboot.ui.component.StatusBar;
import io.github.avinashio.lazyspringboot.ui.component.TextFormatter;
import io.github.avinashio.lazyspringboot.ui.controller.TextInputController;
import io.github.avinashio.lazyspringboot.ui.service.ProjectFilterService;
import io.github.avinashio.lazyspringboot.ui.state.PanelFocus;
import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.PrintWriter;
import java.util.List;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.stereotype.Component;
import io.github.avinashio.lazyspringboot.ui.state.ProjectSortState;


@Component
public class MainScreen {

    private static final int MINIMUM_TERMINAL_WIDTH = 80;
    private static final int PROJECT_PANEL_PERCENTAGE = 25;
    private static final int DEPENDENCY_PANEL_PERCENTAGE = 35;

    private final Terminal terminal;

    private final ProjectPanel projectPanel;

    private final ProjectDetailsPanel projectDetailsPanel;

    private final StatusBar statusBar;

    private final TextFormatter textFormatter;

    private final DependencyPanel dependencyPanel;

    private final ProjectFilterService projectFilterService;

    private final TextInputController textInputController;

    private final ProjectSortState projectSortState;

    public MainScreen(
            Terminal terminal,
            ProjectPanel projectPanel,
            DependencyPanel dependencyPanel,
            ProjectDetailsPanel projectDetailsPanel,
            StatusBar statusBar,
            TextFormatter textFormatter,
            ProjectFilterService projectFilterService,
            TextInputController textInputController, ProjectSortState projectSortState) {

        this.terminal =
                terminal;

        this.projectPanel =
                projectPanel;

        this.dependencyPanel =
                dependencyPanel;

        this.projectDetailsPanel =
                projectDetailsPanel;

        this.statusBar =
                statusBar;

        this.textFormatter =
                textFormatter;

        this.projectFilterService =
                projectFilterService;

        this.textInputController =
                textInputController;
        this.projectSortState = projectSortState;
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

        int width =
                terminal.getWidth();

        if (width < MINIMUM_TERMINAL_WIDTH) {

            renderTerminalTooSmall(
                    writer,
                    width);

            writer.flush();

            return;
        }

        int projectPanelWidth =
                width
                        * PROJECT_PANEL_PERCENTAGE
                        / 100;

        int dependencyPanelWidth =
                width
                        * DEPENDENCY_PANEL_PERCENTAGE
                        / 100;

        int detailPanelWidth =
                width
                        - projectPanelWidth
                        - dependencyPanelWidth
                        - 1;

        int height =
                terminal.getHeight();

        int contentHeight =
                Math.max(
                        1,
                        height - 4);

        List<String> projectLines =
                projectPanel.render(
                        state,
                        contentHeight);

        List<String> dependencyLines =
                dependencyPanel.render(
                        state,
                        contentHeight);

        List<String> detailLines =
                projectDetailsPanel.render(
                        state.selectedProject());

        renderHeader(
                writer,
                state,
                projectPanelWidth,
                dependencyPanelWidth,
                detailPanelWidth);

        renderPanels(
                writer,
                projectLines,
                dependencyLines,
                detailLines,
                projectPanelWidth,
                dependencyPanelWidth,
                detailPanelWidth,
                contentHeight);

        renderFooter(
                writer,
                state,
                width);

        writer.flush();
    }

    private void renderHeader(
            PrintWriter writer,
            UiState state,
            int projectPanelWidth,
            int dependencyPanelWidth,
            int detailPanelWidth) {

        PanelFocus panelFocus =
                state.panelFocus();

        String projectsTitle =
                projectTitle(
                        state);

        if (panelFocus
                == PanelFocus.PROJECTS) {

            projectsTitle =
                    "["
                            + projectsTitle
                            + "]";
        }

        String dependenciesTitle =
                panelFocus
                        == PanelFocus.DEPENDENCIES
                        ? "[Dependencies]"
                        : "Dependencies";

        String detailsTitle =
                panelFocus
                        == PanelFocus.PROJECT_DETAILS
                        ? "[Project Details]"
                        : "Project Details";

        writer.print(
                panelHeader(
                        "┌",
                        projectsTitle,
                        projectPanelWidth));

        writer.print(
                panelHeader(
                        "┬",
                        dependenciesTitle,
                        dependencyPanelWidth));

        writer.print(
                panelHeader(
                        "┬",
                        detailsTitle,
                        detailPanelWidth));

        writer.print("┐");
    }

    private String projectTitle(
            UiState state) {

        int totalProjects =
                state.projects().size();

        String count;

        if (textInputController.active(
                TextInputPurpose.PROJECT_SEARCH)) {

            int visibleProjects =
                    projectFilterService
                            .filter(
                                    state.projects(),
                                    textInputController.value())
                            .size();

            count =
                    visibleProjects
                            + "/"
                            + totalProjects;

        } else {

            count =
                    String.valueOf(
                            totalProjects);
        }

        return "Projects ("
                + count
                + ") · "
                + projectSortState
                .mode()
                .label();
    }

    private String panelHeader(
            String border,
            String title,
            int width) {

        return border
                + "─ "
                + title
                + " "
                + "─".repeat(
                Math.max(
                        0,
                        width
                                - title.length()
                                - 4));
    }

    private void renderPanels(
            PrintWriter writer,
            List<String> projectLines,
            List<String> dependencyLines,
            List<String> detailLines,
            int projectPanelWidth,
            int dependencyPanelWidth,
            int detailPanelWidth,
            int contentHeight) {

        for (int row = 0;
             row < contentHeight;
             row++) {

            writer.println();

            writer.print("│");

            writer.print(
                    textFormatter.fit(
                            lineAt(
                                    projectLines,
                                    row),
                            projectPanelWidth - 1));

            writer.print("│");

            writer.print(
                    textFormatter.fit(
                            lineAt(
                                    dependencyLines,
                                    row),
                            dependencyPanelWidth - 1));

            writer.print("│");

            writer.print(
                    textFormatter.fit(
                            lineAt(
                                    detailLines,
                                    row),
                            detailPanelWidth - 1));

            writer.print("│");
        }
    }

    private String lineAt(
            List<String> lines,
            int index) {

        if (index >= lines.size()) {
            return "";
        }

        return lines.get(
                index);
    }

    private void renderFooter(
            PrintWriter writer,
            UiState state,
            int width) {

        writer.println();

        writer.print("├");

        writer.print(
                "─".repeat(
                        width - 2));

        writer.print("┤");

        writer.println();

        writer.print("│");

        writer.print(
                textFormatter.fit(
                        statusBar.render(
                                state),
                        width - 2));

        writer.print("│");

        writer.println();

        writer.print("└");

        writer.print(
                "─".repeat(
                        width - 2));

        writer.print("┘");
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
}