package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.service.DesktopIntegrationService;
import io.github.avinashio.lazyspringboot.ui.state.ProjectManagerState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.List;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class ProjectManagerController {

    private final ProjectManagerState
            state;

    private final UiState
            uiState;

    public ProjectManagerController(
            ProjectManagerState state,
            UiState uiState) {

        this.state =
                state;

        this.uiState =
                uiState;
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

    public List<SpringProject> projects() {

        return uiState.projects();
    }

    public SpringProject selectedProject() {

        return uiState.selectedProject();
    }
}