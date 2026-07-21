package io.github.avinashio.lazyspringboot.application.workspace;

import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import io.github.avinashio.lazyspringboot.ui.state.WorkspaceState;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class RefreshWorkspaceUseCase {

    private final ChangeWorkspaceUseCase
            changeWorkspaceUseCase;

    private final DiscoverProjectsUseCase
            discoverProjectsUseCase;

    private final UiState uiState;

    private final WorkspaceState workspaceState;

    public RefreshWorkspaceUseCase(
            ChangeWorkspaceUseCase
                    changeWorkspaceUseCase,
            DiscoverProjectsUseCase
                    discoverProjectsUseCase,
            UiState uiState,
            WorkspaceState workspaceState) {

        this.changeWorkspaceUseCase =
                changeWorkspaceUseCase;

        this.discoverProjectsUseCase =
                discoverProjectsUseCase;

        this.uiState =
                uiState;

        this.workspaceState =
                workspaceState;
    }

    public void changeWorkspace(
            String workspace)
            throws IOException {

        changeWorkspaceUseCase
                .changeWorkspace(workspace);

        uiState.setProjects(
                discoverProjectsUseCase.discover());

        workspaceState.clearErrorMessage();

        workspaceState.close();
    }
}