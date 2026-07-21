package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.action.ActionItem;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.component.ModalRenderer;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectActionsScreen {

    private static final int POPUP_WIDTH = 50;

    private static final int MINIMUM_POPUP_WIDTH = 30;

    private static final int POPUP_PADDING = 4;

    private static final String FOOTER =
            " ↑↓ Navigate"
                    + "  Enter Execute"
                    + "  Esc Close";

    private final ModalRenderer modalRenderer;

    public ProjectActionsScreen(
            ModalRenderer modalRenderer) {

        this.modalRenderer = modalRenderer;
    }

    public void render(
            UiState state,
            List<ActionItem> actions) {

        modalRenderer.renderFixedWidth(
                "Project Actions",
                buildContent(
                        state,
                        actions),
                FOOTER,
                POPUP_WIDTH,
                MINIMUM_POPUP_WIDTH,
                POPUP_PADDING);
    }

    private List<String> buildContent(
            UiState state,
            List<ActionItem> actions) {

        List<String> lines =
                new ArrayList<>();

        SpringProject project =
                state.selectedProject();

        if (project == null) {
            lines.add(
                    " Project: No project selected");
        } else {
            lines.add(
                    " Project: "
                            + project.name());
        }

        lines.add("");

        for (int index = 0;
             index < actions.size();
             index++) {

            ActionItem item =
                    actions.get(index);

            String selectionMarker =
                    index
                            == state
                            .selectedProjectActionIndex()
                            ? ">"
                            : " ";

            String disabledMarker =
                    item.enabled()
                            ? ""
                            : " (disabled)";

            lines.add(
                    " "
                            + selectionMarker
                            + " "
                            + item.action()
                            .displayName()
                            + disabledMarker);
        }

        return lines;
    }
}