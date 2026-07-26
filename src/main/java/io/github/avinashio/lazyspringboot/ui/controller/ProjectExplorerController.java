package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.state.ProjectExplorerState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectExplorerController {

    private final ProjectExplorerState
            state;

    private final UiState
            uiState;

    public ProjectExplorerController(
            ProjectExplorerState state,
            UiState uiState) {

        this.state =
                state;

        this.uiState =
                uiState;
    }

    public void open() {

        state.openExplorer();
    }

    public void close() {

        state.close();
    }

    public boolean isOpen() {

        return state.open();
    }

    public List<SpringProject> projects() {

        return uiState.projects();
    }

    public SpringProject selectedProject() {

        return uiState.selectedProject();
    }
}