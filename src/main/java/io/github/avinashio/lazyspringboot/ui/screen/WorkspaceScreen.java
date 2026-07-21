package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.ui.component.ModalRenderer;
import io.github.avinashio.lazyspringboot.ui.state.WorkspaceState;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceScreen {

    private static final int POPUP_WIDTH = 70;

    private static final int MINIMUM_POPUP_WIDTH = 50;

    private static final int POPUP_PADDING = 4;

    private static final String FOOTER =
            " Enter Change Workspace"
                    + "  Esc Close";

    private final ModalRenderer modalRenderer;

    private final WorkspaceState workspaceState;

    public WorkspaceScreen(
            ModalRenderer modalRenderer,
            WorkspaceState workspaceState) {

        this.modalRenderer =
                modalRenderer;

        this.workspaceState =
                workspaceState;
    }

    public void render() {

        modalRenderer.renderFixedWidth(
                "Workspace",
                buildContent(),
                FOOTER,
                POPUP_WIDTH,
                MINIMUM_POPUP_WIDTH,
                POPUP_PADDING);
    }

    private List<String> buildContent() {

        List<String> lines =
                new ArrayList<>();

        lines.add(" Workspace");
        lines.add("");
        lines.add(
                "> "
                        + workspaceState.workspace()
                        + "█");

        if (workspaceState.hasErrorMessage()) {

            lines.add("");
            lines.add(
                    "Error: "
                            + workspaceState.errorMessage());
        }

        return lines;
    }
}