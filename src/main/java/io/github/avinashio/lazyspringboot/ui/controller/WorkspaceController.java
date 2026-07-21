package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.workspace.RefreshWorkspaceUseCase;
import io.github.avinashio.lazyspringboot.service.WorkspaceService;
import io.github.avinashio.lazyspringboot.ui.state.WorkspaceState;
import java.io.IOException;
import java.nio.file.Path;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceController {

    private final WorkspaceState
            workspaceState;

    private final WorkspaceService
            workspaceService;

    private final RefreshWorkspaceUseCase
            refreshWorkspaceUseCase;

    public WorkspaceController(
            WorkspaceState workspaceState,
            WorkspaceService workspaceService,
            RefreshWorkspaceUseCase
                    refreshWorkspaceUseCase) {

        this.workspaceState =
                workspaceState;

        this.workspaceService =
                workspaceService;

        this.refreshWorkspaceUseCase =
                refreshWorkspaceUseCase;
    }

    public void open() {

        workspaceState.open();

        workspaceState.clearErrorMessage();

        workspaceState.startEditing(
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

    public void changeWorkspace(
            String workspace) {

        try {

            refreshWorkspaceUseCase.changeWorkspace(
                    workspace);

        } catch (IOException exception) {

            workspaceState.showErrorMessage(
                    exception.getMessage());
        }
    }
}