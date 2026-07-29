package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.action.ProjectAction;
import io.github.avinashio.lazyspringboot.ui.action.ProjectActionProvider;
import io.github.avinashio.lazyspringboot.ui.component.ModalRenderer;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectManagerController;
import java.util.ArrayList;
import java.util.List;

import io.github.avinashio.lazyspringboot.ui.service.InstalledToolsService;
import org.springframework.stereotype.Component;

@Component
public class ProjectManagerScreen {

    private static final int POPUP_WIDTH = 80;

    private static final int MINIMUM_POPUP_WIDTH = 60;

    private static final int POPUP_PADDING = 4;

    private final ModalRenderer
            modalRenderer;

    private final ProjectManagerController
            controller;

    private final ProjectActionProvider
            projectActionProvider;

    public ProjectManagerScreen(
            ModalRenderer modalRenderer,
            ProjectManagerController controller, ProjectActionProvider projectActionProvider) {

        this.modalRenderer =
                modalRenderer;

        this.controller =
                controller;

        this.projectActionProvider =
                projectActionProvider;

    }

    private String buildFooter() {

        StringBuilder footer =
                new StringBuilder();

        footer.append("Enter Open   ");

        for (ProjectAction action :
                projectActionProvider
                        .projectActions()) {

            if (!action.enabled()) {
                continue;
            }

            footer.append(
                    Character.toUpperCase(
                            action.type()
                                    .shortcut()));

            footer.append(' ');

            footer.append(
                    action.type()
                            .displayName());

            footer.append("   ");
        }

        footer.append("Esc Close");

        return footer.toString();
    }

    public void render() {

        modalRenderer.renderFixedWidth(
                "Project Manager",
                buildContent(),
                buildFooter(),
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