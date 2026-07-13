package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyAvailability;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;

@Component
public class DependencyPanel {

    private final TerminalStyle terminalStyle;
    private final DependencyRowBuilder dependencyRowBuilder;

    public DependencyPanel(
            TerminalStyle terminalStyle,
            DependencyRowBuilder dependencyRowBuilder) {
        this.terminalStyle = terminalStyle;
        this.dependencyRowBuilder =
                dependencyRowBuilder;
    }

    public List<String> render(
            UiState state,
            int visibleHeight) {
        if (state.dependencyItems().isEmpty()) {
            return List.of(" No dependencies available.");
        }

        List<DependencyRow> rows =
                dependencyRowBuilder.build(
                        state.dependencyItems());

        int selectedRowIndex =
                dependencyRowBuilder.findDependencyRowIndex(
                        rows,
                        state.selectedDependencyIndex());

        state
                .dependencyViewport()
                .update(
                        selectedRowIndex,
                        rows.size(),
                        visibleHeight);

        int start =
                state.dependencyViewport().offset();

        int end =
                Math.min(
                        start + visibleHeight,
                        rows.size());

        List<String> lines =
                new ArrayList<>();

        for (int rowIndex = start;
             rowIndex < end;
             rowIndex++) {
            lines.add(
                    renderRow(
                            rows.get(rowIndex),
                            state.selectedDependencyIndex()));
        }

        return List.copyOf(lines);
    }

    private String style(
            DependencyItem item,
            String line) {
        if (item.availability()
                == DependencyAvailability.ALREADY_PRESENT) {
            return terminalStyle.dim(line);
        }

        return line;
    }

    private String marker(DependencyItem item) {
        if (item.availability()
                == DependencyAvailability.ALREADY_PRESENT) {
            return "[*]";
        }

        if (item.selected()) {
            return "[x]";
        }

        return "[ ]";
    }

    private String renderRow(
            DependencyRow row,
            int selectedDependencyIndex) {
        if (row
                instanceof DependencyRow.GroupHeader header) {
            return renderGroupHeader(header);
        }

        DependencyRow.Dependency dependencyRow =
                (DependencyRow.Dependency) row;

        return renderDependency(
                dependencyRow,
                selectedDependencyIndex);
    }

    private String renderGroupHeader(
            DependencyRow.GroupHeader header) {
        return " ── " + header.name() + " ──";
    }

    private String renderDependency(
            DependencyRow.Dependency dependencyRow,
            int selectedDependencyIndex) {
        DependencyItem item =
                dependencyRow.item();

        String cursor =
                dependencyRow.dependencyIndex()
                        == selectedDependencyIndex
                        ? ">"
                        : " ";

        String line =
                " "
                        + cursor
                        + " "
                        + marker(item)
                        + " "
                        + item.dependency().name();

        return style(item, line);
    }
}