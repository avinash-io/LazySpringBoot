package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.dependency.UndoDependenciesUseCase;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.dependency.DependencyNavigation;
import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;
import io.github.avinashio.lazyspringboot.ui.service.DependencyItemsService;
import io.github.avinashio.lazyspringboot.ui.state.PanelFocus;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class NavigationController {

    private final UiState uiState;

    private final DependencyNavigation dependencyNavigation;

    private final DependencyItemsService dependencyItemsService;

    private final UndoDependenciesUseCase undoDependenciesUseCase;

    private final ProjectRefreshService projectRefreshService;

    private final ProjectActionController projectActionController;

    private final CreateProjectController createProjectController;

    public NavigationController(
            UiState uiState,
            DependencyNavigation dependencyNavigation,
            DependencyItemsService dependencyItemsService,
            UndoDependenciesUseCase undoDependenciesUseCase,
            ProjectRefreshService projectRefreshService,
            ProjectActionController projectActionController,
            CreateProjectController createProjectController) {

        this.uiState = uiState;
        this.dependencyNavigation = dependencyNavigation;
        this.dependencyItemsService = dependencyItemsService;
        this.undoDependenciesUseCase =
                undoDependenciesUseCase;
        this.projectRefreshService =
                projectRefreshService;
        this.projectActionController =
                projectActionController;
        this.createProjectController =
                createProjectController;
    }

    public boolean handle(
            KeyEvent keyEvent) {

        uiState.clearMessage();

        switch (keyEvent.type()) {

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
                    handleUndo();

            case ACTIONS ->
                    projectActionController.openActions(
                            uiState.selectedProject());

            case CHARACTER -> {

                if (!keyEvent.hasCharacter()) {
                    break;
                }

                if (keyEvent.character() == 'n') {
                    createProjectController.open();
                }
            }

            default -> {
                return false;
            }
        }

        return true;
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

        if (uiState.panelFocus()
                != PanelFocus.DEPENDENCIES) {

            return;
        }

        uiState.startDependencySearch();

        dependencyNavigation.selectFirstVisible();
    }

    private void handleEnter() {

        if (uiState.panelFocus()
                != PanelFocus.DEPENDENCIES) {

            return;
        }

        uiState.startDependencyConfirmation();
    }

    private void handleUndo() {

        SpringProject project =
                uiState.selectedProject();

        if (!undoDependenciesUseCase.canUndo(
                project)) {

            uiState.showErrorMessage(
                    "No dependency changes to undo");

            return;
        }

        try {

            undoDependenciesUseCase.undo(project);

            projectRefreshService.refreshEverything();

            uiState.clearDependencySelections();

            uiState.showSuccessMessage(
                    "Restored pom.xml for "
                            + project.name());

        } catch (IOException exception) {

            uiState.showErrorMessage(
                    buildUndoErrorMessage(
                            exception));
        }
    }

    private String buildUndoErrorMessage(
            IOException exception) {

        String message =
                exception.getMessage();

        if (message == null
                || message.isBlank()) {

            return "Failed to restore pom.xml";
        }

        return "Failed to restore pom.xml: "
                + message;
    }
}