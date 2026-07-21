package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.controller.TextInputController;
import io.github.avinashio.lazyspringboot.ui.service.VisibleProjectService;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectPanel {

    private final GetProjectProcessUseCase
            getProjectProcessUseCase;

    private final StatusFormatter
            statusFormatter;

    private final ProjectBadgeFormatter
            projectBadgeFormatter;

    private final VisibleProjectService
            visibleProjectService;

    private final TextInputController
            textInputController;

    public ProjectPanel(
            GetProjectProcessUseCase getProjectProcessUseCase,
            StatusFormatter statusFormatter,
            ProjectBadgeFormatter projectBadgeFormatter,
            VisibleProjectService visibleProjectService,
            TextInputController textInputController) {

        this.getProjectProcessUseCase =
                getProjectProcessUseCase;

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

        ProjectProcessStatus processStatus =
                processStatus(
                        project);

        return prefix
                + statusFormatter.icon(
                processStatus)
                + " "
                + project.name()
                + badges(
                project);
    }

    private ProjectProcessStatus processStatus(
            SpringProject project) {

        return getProjectProcessUseCase
                .get(project)
                .map(
                        process ->
                                process.status())
                .orElse(
                        ProjectProcessStatus.STOPPED);
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