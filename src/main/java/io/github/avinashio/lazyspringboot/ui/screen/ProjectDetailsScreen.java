package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.action.ProjectAction;
import io.github.avinashio.lazyspringboot.ui.action.ProjectActionProvider;
import io.github.avinashio.lazyspringboot.ui.component.ModalRenderer;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectDetailsController;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.ui.service.InstalledToolsService;

@Component
public class ProjectDetailsScreen {

    private static final int WIDTH = 90;

    private static final int MINIMUM_WIDTH = 70;

    private static final int PADDING = 4;

    private final ModalRenderer
            modalRenderer;

    private final ProjectDetailsController
            controller;

    private final ProjectActionProvider
            projectActionProvider;

    public ProjectDetailsScreen(
            ModalRenderer modalRenderer,
            ProjectDetailsController controller,
            ProjectActionProvider projectActionProvider) {

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

        footer.append("Esc Back");

        return footer.toString();
    }

    public void render() {

        SpringProject project =
                controller.selectedProject();

        if (project == null) {
            return;
        }

        modalRenderer.renderFixedWidth(
                "Project Details",
                buildContent(project),
                buildFooter(),
                WIDTH,
                MINIMUM_WIDTH,
                PADDING);
    }

    private List<String> buildContent(
            SpringProject project) {

        List<String> lines =
                new ArrayList<>();

        ProjectMetadata metadata =
                project.metadata();

        if (metadata == null) {

            lines.add("Metadata unavailable.");

            lines.add("");

            lines.add(project.path().toString());

            return lines;
        }

        lines.add("📦 " + project.name());
        lines.add("");

        addProperty(
                lines,
                "Group",
                metadata.groupId());

        addProperty(
                lines,
                "Artifact",
                metadata.artifactId());

        addProperty(
                lines,
                "Build",
                metadata.buildTool().toString());

        addProperty(
                lines,
                "Java",
                metadata.javaVersion());

        addProperty(
                lines,
                "Spring Boot",
                metadata.springBootVersion());

        addProperty(
                lines,
                "Dependencies",
                String.valueOf(
                        metadata.dependencies().size()));

        lines.add("");

        addProperty(
                lines,
                "Location",
                project.path().toString());

        return lines;
    }

    private void addProperty(
            List<String> lines,
            String label,
            String value) {

        lines.add(
                String.format(
                        "%-18s %s",
                        label,
                        value));
    }
}