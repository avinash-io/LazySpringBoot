package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.component.ModalRenderer;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectManagerController;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectManagerScreen {

    private static final int POPUP_WIDTH = 80;

    private static final int MINIMUM_POPUP_WIDTH = 60;

    private static final int POPUP_PADDING = 4;

    private static final String FOOTER =
            " Enter Open  I IntelliJ  V VS Code"
                    + "  C Copy Path  Esc Close";

    private final ModalRenderer
            modalRenderer;

    private final ProjectManagerController
            controller;

    public ProjectManagerScreen(
            ModalRenderer modalRenderer,
            ProjectManagerController controller) {

        this.modalRenderer =
                modalRenderer;

        this.controller =
                controller;
    }

    public void render() {

        modalRenderer.renderFixedWidth(
                "Project Manager",
                buildContent(),
                FOOTER,
                POPUP_WIDTH,
                MINIMUM_POPUP_WIDTH,
                POPUP_PADDING);
    }

    private List<String> buildContent() {

        List<String> lines =
                new ArrayList<>();

        List<SpringProject> projects =
                controller.projects();

        SpringProject selected =
                controller.selectedProject();

        if (projects.isEmpty()) {

            lines.add(
                    " No projects found.");

            return lines;
        }

        for (SpringProject project : projects) {

            String prefix =
                    project.equals(selected)
                            ? "► "
                            : "  ";

            lines.add(
                    prefix
                            + project.name());
        }

        return lines;
    }
}