package io.github.avinashio.lazyspringboot.ui.project;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.controller.TextInputController;
import io.github.avinashio.lazyspringboot.ui.service.ProjectFilterService;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectNavigation {

    private final UiState uiState;

    private final ProjectFilterService
            projectFilterService;

    private final TextInputController
            textInputController;

    public ProjectNavigation(
            UiState uiState,
            ProjectFilterService projectFilterService,
            TextInputController textInputController) {

        this.uiState =
                uiState;

        this.projectFilterService =
                projectFilterService;

        this.textInputController =
                textInputController;
    }

    public void selectFirstVisible() {

        List<Integer> indexes =
                visibleIndexes();

        if (indexes.isEmpty()) {
            return;
        }

        uiState.selectProject(
                indexes.getFirst());
    }

    public void selectNextVisible() {

        List<Integer> indexes =
                visibleIndexes();

        if (indexes.isEmpty()) {
            return;
        }

        int current =
                indexes.indexOf(
                        uiState.selectedProjectIndex());

        if (current < 0) {

            uiState.selectProject(
                    indexes.getFirst());

            return;
        }

        if (current < indexes.size() - 1) {

            uiState.selectProject(
                    indexes.get(current + 1));
        }
    }

    public void selectPreviousVisible() {

        List<Integer> indexes =
                visibleIndexes();

        if (indexes.isEmpty()) {
            return;
        }

        int current =
                indexes.indexOf(
                        uiState.selectedProjectIndex());

        if (current < 0) {

            uiState.selectProject(
                    indexes.getFirst());

            return;
        }

        if (current > 0) {

            uiState.selectProject(
                    indexes.get(current - 1));
        }
    }

    private List<Integer> visibleIndexes() {

        List<SpringProject> projects =
                uiState.projects();

        List<SpringProject> visibleProjects =
                projectFilterService.filter(
                        projects,
                        textInputController.value());

        return visibleProjects.stream()
                .map(projects::indexOf)
                .toList();
    }
}