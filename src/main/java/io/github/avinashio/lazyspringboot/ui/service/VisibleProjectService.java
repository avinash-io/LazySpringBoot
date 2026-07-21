package io.github.avinashio.lazyspringboot.ui.service;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.controller.TextInputController;
import io.github.avinashio.lazyspringboot.ui.state.ProjectSortState;
import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class VisibleProjectService {

    private final ProjectFilterService
            projectFilterService;

    private final ProjectSortService
            projectSortService;

    private final ProjectSortState
            projectSortState;

    private final TextInputController
            textInputController;

    public VisibleProjectService(
            ProjectFilterService projectFilterService,
            ProjectSortService projectSortService,
            ProjectSortState projectSortState,
            TextInputController textInputController) {

        this.projectFilterService =
                projectFilterService;

        this.projectSortService =
                projectSortService;

        this.projectSortState =
                projectSortState;

        this.textInputController =
                textInputController;
    }

    public List<SpringProject> visibleProjects(
            List<SpringProject> projects) {

        List<SpringProject> filteredProjects =
                projects;

        if (textInputController.active(
                TextInputPurpose.PROJECT_SEARCH)) {

            filteredProjects =
                    projectFilterService.filter(
                            projects,
                            textInputController.value());
        }

        return projectSortService.sort(
                filteredProjects,
                projectSortState.mode());
    }
}