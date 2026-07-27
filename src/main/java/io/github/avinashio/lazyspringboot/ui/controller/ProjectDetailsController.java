package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.service.DesktopIntegrationService;
import io.github.avinashio.lazyspringboot.ui.state.ProjectDetailsState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.springframework.stereotype.Component;
import java.io.IOException;;

@Component
public class ProjectDetailsController {

    private final ProjectDetailsState
            state;

    private final UiState
            uiState;

    private final DesktopIntegrationService
            desktopIntegrationService;

    public ProjectDetailsController(
            ProjectDetailsState state,
            UiState uiState,
            DesktopIntegrationService desktopIntegrationService) {

        this.state =
                state;

        this.uiState =
                uiState;
        this.desktopIntegrationService = desktopIntegrationService;
    }

    public void open() {

        state.open();
    }

    public void close() {

        state.close();
    }

    public boolean isOpen() {

        return state.isOpen();
    }

    public SpringProject selectedProject() {

        return uiState.selectedProject();
    }

    public void copyProjectPath() {

        SpringProject project =
                selectedProject();

        if (project == null) {

            uiState.showErrorMessage(
                    "No project selected.");

            return;
        }

        if (desktopIntegrationService.copyToClipboard(
                project.path().toString())) {

            uiState.showSuccessMessage(
                    "Copied path for "
                            + project.name());

        } else {

            uiState.showErrorMessage(
                    "Unable to copy project path.");
        }
    }

    public void openProjectFolder() {

        SpringProject project =
                selectedProject();

        if (project == null) {

            uiState.showErrorMessage(
                    "No project selected.");

            return;
        }

        if (desktopIntegrationService.openFolder(
                project.path())) {

            uiState.showSuccessMessage(
                    "Opened "
                            + project.name());

        } else {

            uiState.showErrorMessage(
                    "Unable to open "
                            + project.name());
        }
    }
}