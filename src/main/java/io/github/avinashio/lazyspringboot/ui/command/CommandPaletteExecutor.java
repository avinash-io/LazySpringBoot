package io.github.avinashio.lazyspringboot.ui.command;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.controller.CreateProjectController;
import io.github.avinashio.lazyspringboot.ui.controller.ProcessController;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectRefreshService;
import io.github.avinashio.lazyspringboot.ui.controller.TextInputController;
import io.github.avinashio.lazyspringboot.ui.service.DependencyUndoService;
import io.github.avinashio.lazyspringboot.ui.state.PanelFocus;
import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class CommandPaletteExecutor {

    private final CreateProjectController
            createProjectController;

    private final ProjectRefreshService
            projectRefreshService;

    private final ProcessController
            processController;

    private final DependencyUndoService
            dependencyUndoService;

    private final TextInputController
            textInputController;

    private final UiState
            uiState;

    public CommandPaletteExecutor(
            CreateProjectController createProjectController,
            ProjectRefreshService projectRefreshService,
            ProcessController processController,
            DependencyUndoService dependencyUndoService,
            TextInputController textInputController,
            UiState uiState) {

        this.createProjectController =
                createProjectController;

        this.projectRefreshService =
                projectRefreshService;

        this.processController =
                processController;

        this.dependencyUndoService =
                dependencyUndoService;

        this.textInputController =
                textInputController;

        this.uiState =
                uiState;
    }

    public void execute(
            Command command) {

        switch (command.id()) {

            case "create-project" ->
                    createProjectController.open();

            case "refresh-projects" ->
                    refreshSelectedProject();

            case "start-project" ->
                    startSelectedProject();

            case "stop-project" ->
                    stopSelectedProject();

            case "view-logs" ->
                    showSelectedProjectLogs();

            case "add-dependencies" ->
                    openDependencySearch();

            case "undo-dependencies" ->
                    dependencyUndoService.undo();

            default -> {
                // No action.
            }
        }
    }

    private void refreshSelectedProject() {

        if (!hasSelectedProject()) {
            return;
        }

        try {

            projectRefreshService
                    .refreshEverything();

        } catch (IOException exception) {

            uiState.showErrorMessage(
                    "Failed to refresh selected project");
        }
    }

    private void startSelectedProject() {

        SpringProject project =
                selectedProject();

        if (project == null) {
            return;
        }

        processController.start(
                project);
    }

    private void stopSelectedProject() {

        SpringProject project =
                selectedProject();

        if (project == null) {
            return;
        }

        processController.stop(
                project);
    }

    private void showSelectedProjectLogs() {

        SpringProject project =
                selectedProject();

        if (project == null) {
            return;
        }

        processController.showLogs(
                project);
    }

    private void openDependencySearch() {

        if (!hasSelectedProject()) {
            return;
        }

        uiState.focusPanel(
                PanelFocus.DEPENDENCIES);

        textInputController.start(
                TextInputPurpose.DEPENDENCY_SEARCH);
    }

    private boolean hasSelectedProject() {

        if (selectedProject() != null) {
            return true;
        }

        uiState.showErrorMessage(
                "No project selected");

        return false;
    }

    private SpringProject selectedProject() {

        return uiState.selectedProject();
    }
}