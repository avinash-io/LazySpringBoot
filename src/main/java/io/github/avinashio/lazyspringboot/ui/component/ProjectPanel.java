package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.controller.TextInputController;
import io.github.avinashio.lazyspringboot.ui.service.ProjectFilterService;
import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;
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

    private final ProjectFilterService
            projectFilterService;

    private final TextInputController
            textInputController;

    public ProjectPanel(
            GetProjectProcessUseCase getProjectProcessUseCase,
            StatusFormatter statusFormatter,
            ProjectBadgeFormatter projectBadgeFormatter,
            ProjectFilterService projectFilterService,
            TextInputController textInputController) {

        this.getProjectProcessUseCase =
                getProjectProcessUseCase;

        this.statusFormatter =
                statusFormatter;

        this.projectBadgeFormatter =
                projectBadgeFormatter;

        this.projectFilterService =
                projectFilterService;

        this.textInputController =
                textInputController;
    }

    public List<String> render(
            UiState state) {

        List<String> lines =
                new ArrayList<>();

        if (state.projects().isEmpty()) {

            lines.add(
                    " No Spring Boot projects found.");

            return lines;
        }

        List<SpringProject> visibleProjects =
                visibleProjects(
                        state);

        if (visibleProjects.isEmpty()) {

            lines.add(
                    " No projects match \""
                            + textInputController.value()
                            + "\"");

            return lines;
        }

        for (SpringProject project :
                visibleProjects) {

            int projectIndex =
                    state.projects()
                            .indexOf(project);

            String prefix =
                    projectIndex
                            == state.selectedProjectIndex()
                            ? " > "
                            : "   ";

            lines.add(
                    prefix
                            + statusIcon(project)
                            + " "
                            + project.name()
                            + badges(project));
        }

        return lines;
    }

    private List<SpringProject> visibleProjects(
            UiState state) {

        if (!textInputController.active(
                TextInputPurpose.PROJECT_SEARCH)) {

            return state.projects();
        }

        return projectFilterService.filter(
                state.projects(),
                textInputController.value());
    }

    private String statusIcon(
            SpringProject project) {

        return getProjectProcessUseCase
                .get(project)
                .map(process ->
                        statusFormatter.icon(
                                process.status()))
                .orElseGet(
                        () ->
                                statusFormatter.icon(
                                        ProjectProcessStatus.STOPPED));
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