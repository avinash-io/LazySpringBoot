package io.github.avinashio.lazyspringboot.ui.controller;


import io.github.avinashio.lazyspringboot.service.WorkspaceService;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import io.github.avinashio.lazyspringboot.ui.state.WorkspaceState;
import java.nio.file.Path;
import org.springframework.stereotype.Component;
import io.github.avinashio.lazyspringboot.ui.service.DesktopIntegrationService;

@Component
public class WorkspaceController {

    private final WorkspaceState
            workspaceState;

    private final WorkspaceService
            workspaceService;

    private final DesktopIntegrationService
            desktopIntegrationService;

    private final UiState
            uiState;

    public WorkspaceController(
            WorkspaceState workspaceState,
            WorkspaceService workspaceService,
            DesktopIntegrationService
                    desktopIntegrationService,
            UiState uiState) {

        this.workspaceState =
                workspaceState;

        this.workspaceService =
                workspaceService;

        this.desktopIntegrationService =
                desktopIntegrationService;

        this.uiState =
                uiState;
    }

    public void open() {

        workspaceState.open();

        workspaceState.clearErrorMessage();

        workspaceState.setWorkspace(
                workspaceService
                        .workspace()
                        .toString());
    }

    public void close() {

        workspaceState.close();
    }

    public boolean isOpen() {

        return workspaceState.isOpen();
    }

    public Path workspace() {

        return workspaceService.workspace();
    }

    public void copyWorkspacePath() {

        if (desktopIntegrationService.copyToClipboard(
                workspace().toString())) {

            uiState.showSuccessMessage(
                    "Workspace path copied.");

        } else {

            uiState.showErrorMessage(
                    "Unable to copy workspace path.");
        }
    }

    public void openWorkspace() {

        if (desktopIntegrationService.openFolder(
                workspace())) {

            uiState.showSuccessMessage(
                    "Workspace opened.");

        } else {

            uiState.showErrorMessage(
                    "Unable to open workspace.");
        }
    }
}