package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.controller.TextInputController;
import io.github.avinashio.lazyspringboot.ui.service.VisibleProjectService;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import io.github.avinashio.lazyspringboot.ui.model.ProjectRuntimeInfo;
import io.github.avinashio.lazyspringboot.ui.service.ProjectRuntimeInfoFactory;

@Component
public class ProjectPanel {

    private final StatusFormatter
            statusFormatter;

    private final ProjectBadgeFormatter
            projectBadgeFormatter;

    private final VisibleProjectService
            visibleProjectService;

    private final TextInputController
            textInputController;

    private final ProjectRuntimeInfoFactory
            projectRuntimeInfoFactory;

    public ProjectPanel(
            StatusFormatter statusFormatter,
            ProjectBadgeFormatter projectBadgeFormatter,
            VisibleProjectService visibleProjectService,
            TextInputController textInputController,
            ProjectRuntimeInfoFactory projectRuntimeInfoFactory) {

        this.projectRuntimeInfoFactory =
                projectRuntimeInfoFactory;

        this.statusFormatter =
                statusFormatter;

        this.projectBadgeFormatter =
                projectBadgeFormatter;

        this.visibleProjectService =
                visibleProjectService;

        this.textInputController =
                textInputController;
    }

    public List<String> render(
            UiState state,
            int visibleHeight) {

        List<String> lines =
                new ArrayList<>();

        if (state.projects().isEmpty()) {

            lines.add(
                    " No Spring Boot projects found.");

            return lines;
        }

        List<SpringProject> visibleProjects =
                visibleProjectService.visibleProjects(
                        state.projects());

        if (visibleProjects.isEmpty()) {

            lines.add(
                    " No projects match \""
                            + textInputController.value()
                            + "\"");

            return lines;
        }

        int selectedVisibleIndex =
                selectedVisibleIndex(
                        state,
                        visibleProjects);

        state.projectViewport()
                .update(
                        selectedVisibleIndex,
                        visibleProjects.size(),
                        visibleHeight);

        int start =
                state.projectViewport()
                        .offset();

        int end =
                Math.min(
                        start + visibleHeight,
                        visibleProjects.size());

        lines.add(
                String.format(
                        "%-25s %-10s %-6s %-8s",
                        "NAME",
                        "STATUS",
                        "PORT",
                        "UPTIME"));

        lines.add(
                "────────────────────────────────────────────────────");

        for (int visibleIndex = start;
             visibleIndex < end;
             visibleIndex++) {

            SpringProject project =
                    visibleProjects.get(
                            visibleIndex);

            int projectIndex =
                    state.projects()
                            .indexOf(project);

            String prefix =
                    projectIndex
                            == state.selectedProjectIndex()
                            ? " > "
                            : "   ";

            lines.add(
                    projectLine(
                            prefix,
                            project));
        }

        return lines;
    }

    private int selectedVisibleIndex(
            UiState state,
            List<SpringProject> visibleProjects) {

        SpringProject selectedProject =
                state.selectedProject();

        if (selectedProject == null) {
            return 0;
        }

        int index =
                visibleProjects.indexOf(
                        selectedProject);

        return Math.max(
                index,
                0);
    }

    private String projectLine(
            String prefix,
            SpringProject project) {

        ProjectRuntimeInfo runtime =
                projectRuntimeInfoFactory.create(
                        project);

        return String.format(
                "%s%-22s %-10s %-6s %-8s%s",
                prefix,
                project.name(),
                runtime.status(),
                runtime.port(),
                runtime.uptime(),
                badges(project));
    }

    private String badges(
            SpringProject project) {

        String badges =
                projectBadgeFormatter.format(
                        project);

        if (badges.isBlank()) {
            return "";
        }

        return "  " + badges;
    }
}