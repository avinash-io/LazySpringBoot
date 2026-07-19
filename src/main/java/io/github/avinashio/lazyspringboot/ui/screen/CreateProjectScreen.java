package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import java.io.PrintWriter;
import java.util.List;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.stereotype.Component;

@Component
public class CreateProjectScreen {

    private static final int MAX_VISIBLE_DEPENDENCIES =
            15;

    private final Terminal terminal;

    public CreateProjectScreen(
            Terminal terminal) {

        this.terminal = terminal;
    }

    public void render(
            CreateProjectState state) {

        PrintWriter writer =
                terminal.writer();

        terminal.puts(
                InfoCmp.Capability.clear_screen);

        terminal.puts(
                InfoCmp.Capability.cursor_address,
                0,
                0);

        writer.println(
                "LazySpringBoot");

        writer.println();

        if (state.metadataStage()) {

            renderMetadataStage(
                    writer,
                    state);

        } else {

            renderDependencyStage(
                    writer,
                    state);
        }

        writer.flush();
    }

    private void renderMetadataStage(
            PrintWriter writer,
            CreateProjectState state) {

        writer.println(
                "Create Spring Boot Project");

        writer.println();

        if (state.versionSelecting()) {

            renderVersionSelection(
                    writer,
                    state);

            return;
        }

        renderField(
                writer,
                state,
                0,
                "Name",
                state.name());

        renderField(
                writer,
                state,
                1,
                "Group",
                state.groupId());

        renderField(
                writer,
                state,
                2,
                "Artifact",
                state.artifactId());

        renderField(
                writer,
                state,
                3,
                "Package",
                state.packageName());

        renderField(
                writer,
                state,
                4,
                "Java",
                state.javaVersion());

        renderField(
                writer,
                state,
                5,
                "Spring Boot",
                state.springBootVersion());

        if (state.hasErrorMessage()) {

            writer.println();

            writer.println(
                    "Error: "
                            + state.errorMessage());
        }

        writer.println();

        writer.println(
                "↑↓ Navigate"
                        + "    Enter Edit/Select"
                        + "    Tab Continue"
                        + "    Esc Cancel");
    }

    private void renderVersionSelection(
            PrintWriter writer,
            CreateProjectState state) {

        String title =
                state.selectedField() == 4
                        ? "Select Java Version"
                        : "Select Spring Boot Version";

        writer.println(
                title);

        writer.println();

        List<String> versions =
                state.currentVersionOptions();

        if (versions.isEmpty()) {

            writer.println(
                    "  No versions available");

        } else {

            for (int index = 0;
                 index < versions.size();
                 index++) {

                String marker =
                        index
                                == state.selectedVersionIndex()
                                ? ">"
                                : " ";

                writer.printf(
                        "%s %s%n",
                        marker,
                        versions.get(index));
            }
        }

        writer.println();

        writer.println(
                "↑↓ Navigate"
                        + "    Enter Edit/Select"
                        + "    Tab Continue"
                        + "    Esc Cancel");
    }

    private void renderDependencyStage(
            PrintWriter writer,
            CreateProjectState state) {

        writer.println(
                "Select Dependencies");

        writer.println();

        if (state.dependencySearchActive()) {

            writer.println(
                    "Search: "
                            + state.dependencySearchQuery()
                            + "_");

        } else {

            writer.println(
                    "Search: /");
        }

        writer.println();

        List<SpringDependency> dependencies =
                state.filteredDependencies();

        if (dependencies.isEmpty()) {

            writer.println(
                    "  No matching dependencies");

        } else {

            renderDependencies(
                    writer,
                    state,
                    dependencies);
        }

        writer.println();

        writer.println(
                "Selected: "
                        + state.selectedDependencies()
                        .size());

        writer.println();

        if (state.hasErrorMessage()) {

            writer.println();

            writer.println(
                    "Error: "
                            + state.errorMessage());
        }

        if (state.dependencySearchActive()) {

            writer.println(
                    "Type to Search"
                            + "    ↑↓ Navigate"
                            + "    Space/Enter Toggle"
                            + "    Esc Close Search");

        } else {

            writer.println(
                    "↑↓ Navigate"
                            + "    Space Toggle"
                            + "    / Search"
                            + "    Enter Create"
                            + "    Esc Back");
        }
    }

    private void renderDependencies(
            PrintWriter writer,
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
                    dependencies.get(
                            index);

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

            writer.printf(
                    "%s %s %-30s %s%n",
                    cursor,
                    checkbox,
                    dependency.name(),
                    dependency.group());
        }

        if (dependencies.size()
                > MAX_VISIBLE_DEPENDENCIES) {

            writer.println();

            writer.printf(
                    "  Showing %d-%d of %d%n",
                    startIndex + 1,
                    endIndex,
                    dependencies.size());
        }
    }

    private int calculateStartIndex(
            int selectedIndex,
            int dependencyCount) {

        if (dependencyCount
                <= MAX_VISIBLE_DEPENDENCIES) {

            return 0;
        }

        int halfWindow =
                MAX_VISIBLE_DEPENDENCIES / 2;

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

    private void renderField(
            PrintWriter writer,
            CreateProjectState state,
            int index,
            String label,
            String value) {

        String marker =
                state.selectedField() == index
                        ? (state.editing()
                        ? "*"
                        : ">")
                        : " ";

        writer.printf(
                "%s %-15s %s%n",
                marker,
                label,
                value);
    }
}