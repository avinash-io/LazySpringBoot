package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.component.ModalRenderer;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectDetailsController;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;

@Component
public class ProjectDetailsScreen {

    private static final int WIDTH = 90;

    private static final int MINIMUM_WIDTH = 70;

    private static final int PADDING = 4;

    private static final String FOOTER =
            "O Open Folder   C Copy Path   Esc Back";

    private final ModalRenderer
            modalRenderer;

    private final ProjectDetailsController
            controller;

    public ProjectDetailsScreen(
            ModalRenderer modalRenderer,
            ProjectDetailsController controller) {

        this.modalRenderer =
                modalRenderer;

        this.controller =
                controller;
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
                FOOTER,
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