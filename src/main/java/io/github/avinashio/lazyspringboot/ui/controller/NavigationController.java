package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.ui.command.CommandPaletteController;
import io.github.avinashio.lazyspringboot.ui.dependency.DependencyNavigation;
import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;
import io.github.avinashio.lazyspringboot.ui.service.DependencyItemsService;
import io.github.avinashio.lazyspringboot.ui.service.DependencyUndoService;
import io.github.avinashio.lazyspringboot.ui.state.PanelFocus;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.springframework.stereotype.Component;

import io.github.avinashio.lazyspringboot.ui.controller.TextInputController;
import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;

@Component
public class NavigationController {

    private final UiState uiState;

    private final DependencyNavigation dependencyNavigation;

    private final DependencyItemsService dependencyItemsService;

    private final ProjectActionController projectActionController;

    private final CreateProjectController createProjectController;

    private final CommandPaletteController commandPaletteController;

    private final DependencyUndoService dependencyUndoService;

    private final ProjectRefreshController projectRefreshController;

    private final WorkspaceController workspaceController;

    private final TextInputController textInputController;

    public NavigationController(
            UiState uiState,
            DependencyNavigation dependencyNavigation,
            DependencyItemsService dependencyItemsService,
            ProjectActionController projectActionController,
            CreateProjectController createProjectController,
            DependencyUndoService dependencyUndoService,
            CommandPaletteController commandPaletteController,
            ProjectRefreshController projectRefreshController,
            WorkspaceController workspaceController, TextInputController textInputController) {

        this.uiState = uiState;
        this.dependencyNavigation =
                dependencyNavigation;
        this.dependencyItemsService =
                dependencyItemsService;
        this.projectActionController =
                projectActionController;
        this.createProjectController =
                createProjectController;
        this.commandPaletteController =
                commandPaletteController;
        this.dependencyUndoService =
                dependencyUndoService;
        this.projectRefreshController =
                projectRefreshController;
        this.workspaceController = workspaceController;

        this.textInputController = textInputController;
    }

    public boolean handle(
            KeyEvent keyEvent) {

        uiState.clearMessage();

        switch (keyEvent.type()) {

            case COMMAND_PALETTE ->
                    commandPaletteController.open();

            case LEFT ->
                    uiState.focusPreviousPanel();

            case RIGHT ->
                    uiState.focusNextPanel();

            case UP ->
                    handleUp();

            case DOWN ->
                    handleDown();

            case SPACE ->
                    handleSpace();

            case SEARCH ->
                    handleSearch();

            case ENTER ->
                    handleEnter();

            case UNDO ->
                    dependencyUndoService.undo();

            case ACTIONS ->
                    projectActionController.openActions(
                            uiState.selectedProject());

            case CHARACTER -> {

                if (!keyEvent.hasCharacter()) {
                    break;
                }

                handleCharacter(
                        keyEvent.character());
            }

            default -> {
                return false;
            }
        }

        return true;
    }

    private void handleCharacter(
            char character) {

        switch (character) {

            case 'a' ->
                    projectActionController.openActions(
                            uiState.selectedProject());

            case 'u' ->
                    dependencyUndoService.undo();

            case 'n' ->
                    createProjectController.open();

            case 'r' ->
                    handleRefresh();

            case 'w' ->
                    workspaceController.open();

            default -> {
                // No action.
            }
        }
    }

    private void handleRefresh() {

        try {

            projectRefreshController.refresh();

            dependencyItemsService.refresh();

            uiState.showSuccessMessage(
                    "Projects refreshed");

        } catch (Exception exception) {

            uiState.showErrorMessage(
                    "Failed to refresh projects: "
                            + exception.getMessage());
        }
    }

    private void handleUp() {

        switch (uiState.panelFocus()) {

            case PROJECTS -> {

                int previousIndex =
                        uiState.selectedProjectIndex();

                uiState.selectPreviousProject();

                if (previousIndex
                        != uiState.selectedProjectIndex()) {

                    dependencyItemsService.refresh();
                }
            }

            case DEPENDENCIES ->
                    dependencyNavigation
                            .selectPreviousVisible();

            case PROJECT_DETAILS -> {
                // No action.
            }
        }
    }

    private void handleDown() {

        switch (uiState.panelFocus()) {

            case PROJECTS -> {

                int previousIndex =
                        uiState.selectedProjectIndex();

                uiState.selectNextProject();

                if (previousIndex
                        != uiState.selectedProjectIndex()) {

                    dependencyItemsService.refresh();
                }
            }

            case DEPENDENCIES ->
                    dependencyNavigation
                            .selectNextVisible();

            case PROJECT_DETAILS -> {
                // No action.
            }
        }
    }

    private void handleSpace() {

        if (uiState.panelFocus()
                == PanelFocus.DEPENDENCIES) {

            uiState.toggleSelectedDependency();
        }
    }

    private void handleSearch() {

        switch (uiState.panelFocus()) {

            case PROJECTS ->
                    textInputController.start(
                            TextInputPurpose.PROJECT_SEARCH);

            case DEPENDENCIES -> {

                textInputController.start(
                        TextInputPurpose.DEPENDENCY_SEARCH);

                dependencyNavigation
                        .selectFirstVisible();
            }

            case PROJECT_DETAILS -> {
                // No action.
            }
        }
    }

    private void handleEnter() {

        if (uiState.panelFocus()
                != PanelFocus.DEPENDENCIES) {

            return;
        }

        uiState.startDependencyConfirmation();
    }

}