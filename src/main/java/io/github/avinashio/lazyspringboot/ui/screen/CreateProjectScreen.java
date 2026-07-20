package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.stereotype.Component;

@Component
public class CreateProjectScreen {

    private static final int WIDTH_PERCENTAGE = 70;

    private static final int HEIGHT_PERCENTAGE = 80;

    private static final int MINIMUM_POPUP_WIDTH = 60;

    private static final int MINIMUM_POPUP_HEIGHT = 16;

    private static final int MAX_VISIBLE_DEPENDENCIES = 15;

    private final Terminal terminal;

    public CreateProjectScreen(
            Terminal terminal) {

        this.terminal = terminal;
    }

    public void render(
            CreateProjectState state) {

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

        List<String> content =
                buildContent(state);

        renderHeader(
                writer,
                state,
                startRow,
                startColumn,
                popupWidth);

        renderContent(
                writer,
                content,
                startRow,
                startColumn,
                popupWidth,
                popupHeight);

        renderFooter(
                writer,
                state,
                startRow,
                startColumn,
                popupWidth,
                popupHeight);

        writer.flush();
    }

    private List<String> buildContent(
            CreateProjectState state) {

        if (state.metadataStage()) {
            return buildMetadataContent(state);
        }

        return buildDependencyContent(state);
    }

    private List<String> buildMetadataContent(
            CreateProjectState state) {

        List<String> lines =
                new ArrayList<>();

        if (state.versionSelecting()) {
            buildVersionSelectionContent(
                    lines,
                    state);

            return lines;
        }

        lines.add(
                buildField(
                        state,
                        0,
                        "Name",
                        state.name()));

        lines.add(
                buildField(
                        state,
                        1,
                        "Group",
                        state.groupId()));

        lines.add(
                buildField(
                        state,
                        2,
                        "Artifact",
                        state.artifactId()));

        lines.add(
                buildField(
                        state,
                        3,
                        "Package",
                        state.packageName()));

        lines.add(
                buildField(
                        state,
                        4,
                        "Java",
                        state.javaVersion()));

        lines.add(
                buildField(
                        state,
                        5,
                        "Spring Boot",
                        state.springBootVersion()));

        if (state.hasErrorMessage()) {
            lines.add("");
            lines.add(
                    " Error: "
                            + state.errorMessage());
        }

        return lines;
    }

    private void buildVersionSelectionContent(
            List<String> lines,
            CreateProjectState state) {

        List<String> versions =
                state.currentVersionOptions();

        if (versions.isEmpty()) {
            lines.add(
                    "  No versions available");

            return;
        }

        for (int index = 0;
             index < versions.size();
             index++) {

            String marker =
                    index
                            == state.selectedVersionIndex()
                            ? ">"
                            : " ";

            lines.add(
                    " "
                            + marker
                            + " "
                            + versions.get(index));
        }
    }

    private List<String> buildDependencyContent(
            CreateProjectState state) {

        List<String> lines =
                new ArrayList<>();

        if (state.dependencySearchActive()) {
            lines.add(
                    " Search: "
                            + state.dependencySearchQuery()
                            + "_");
        } else {
            lines.add(
                    " Search: /");
        }

        lines.add("");

        List<SpringDependency> dependencies =
                state.filteredDependencies();

        if (dependencies.isEmpty()) {
            lines.add(
                    "  No matching dependencies");
        } else {
            buildDependencyLines(
                    lines,
                    state,
                    dependencies);
        }

        lines.add("");

        lines.add(
                " Selected: "
                        + state.selectedDependencies()
                        .size());

        if (state.hasErrorMessage()) {
            lines.add("");
            lines.add(
                    " Error: "
                            + state.errorMessage());
        }

        return lines;
    }

    private void buildDependencyLines(
            List<String> lines,
            CreateProjectState state,
            List<SpringDependency> dependencies) {

        int selectedIndex =
                state.selectedDependencyIndex();

        int startIndex =
                calculateStartIndex(
                        selectedIndex,
                        dependencies.size());

        int endIndex =
                Math.min(
                        startIndex
                                + MAX_VISIBLE_DEPENDENCIES,
                        dependencies.size());

        for (int index = startIndex;
             index < endIndex;
             index++) {

            SpringDependency dependency =
                    dependencies.get(index);

            boolean selected =
                    state.dependencySelected(
                            dependency.id());

            String cursor =
                    index == selectedIndex
                            ? ">"
                            : " ";

            String checkbox =
                    selected
                            ? "[x]"
                            : "[ ]";

            lines.add(
                    String.format(
                            " %s %s %-30s %s",
                            cursor,
                            checkbox,
                            dependency.name(),
                            dependency.group()));
        }

        if (dependencies.size()
                > MAX_VISIBLE_DEPENDENCIES) {

            lines.add("");

            lines.add(
                    String.format(
                            "  Showing %d-%d of %d",
                            startIndex + 1,
                            endIndex,
                            dependencies.size()));
        }
    }

    private void renderHeader(
            PrintWriter writer,
            CreateProjectState state,
            int startRow,
            int startColumn,
            int width) {

        moveCursor(
                startRow,
                startColumn);

        String title =
                buildTitle(state);

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
                    startRow
                            + row
                            + 1,
                    startColumn);

            writer.print("│");

            String line =
                    row < lines.size()
                            ? lines.get(row)
                            : "";

            writer.print(
                    fit(
                            line,
                            width - 2));

            writer.print("│");
        }
    }

    private void renderFooter(
            PrintWriter writer,
            CreateProjectState state,
            int startRow,
            int startColumn,
            int width,
            int height) {

        int separatorRow =
                startRow
                        + height
                        - 3;

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
                        " "
                                + buildNavigationText(
                                state),
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

    private String buildTitle(
            CreateProjectState state) {

        if (state.versionSelecting()) {
            return state.selectedField() == 4
                    ? "Select Java Version"
                    : "Select Spring Boot Version";
        }

        if (state.metadataStage()) {
            return "Create Spring Boot Project";
        }

        return "Select Dependencies";
    }

    private String buildNavigationText(
            CreateProjectState state) {

        if (state.versionSelecting()) {
            return "↑↓ Navigate"
                    + "    Enter Select"
                    + "    Esc Back";
        }

        if (state.metadataStage()) {
            return "↑↓ Navigate"
                    + "    Enter Edit/Select"
                    + "    Tab Continue"
                    + "    Esc Cancel";
        }

        if (state.dependencySearchActive()) {
            return "Type to Search"
                    + "    ↑↓ Navigate"
                    + "    Space/Enter Toggle"
                    + "    Esc Close Search";
        }

        return "↑↓ Navigate"
                + "    Space Toggle"
                + "    / Search"
                + "    Enter Create"
                + "    Esc Back";
    }

    private String buildField(
            CreateProjectState state,
            int index,
            String label,
            String value) {

        String marker =
                state.selectedField() == index
                        ? state.editing()
                        ? "*"
                        : ">"
                        : " ";

        return String.format(
                " %s %-15s %s",
                marker,
                label,
                value);
    }

    private int calculateStartIndex(
            int selectedIndex,
            int dependencyCount) {

        if (dependencyCount
                <= MAX_VISIBLE_DEPENDENCIES) {
            return 0;
        }

        int halfWindow =
                MAX_VISIBLE_DEPENDENCIES
                        / 2;

        int startIndex =
                selectedIndex
                        - halfWindow;

        if (startIndex < 0) {
            return 0;
        }

        int maximumStart =
                dependencyCount
                        - MAX_VISIBLE_DEPENDENCIES;

        return Math.min(
                startIndex,
                maximumStart);
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