package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.project.RefreshSelectedProjectUseCase;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.service.DependencyItemsService;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class ProjectRefreshService {

    private final UiState uiState;

    private final RefreshSelectedProjectUseCase
            refreshSelectedProjectUseCase;

    private final DependencyItemsService
            dependencyItemsService;

    public ProjectRefreshService(
            UiState uiState,
            RefreshSelectedProjectUseCase refreshSelectedProjectUseCase,
            DependencyItemsService dependencyItemsService) {

        this.uiState = uiState;
        this.refreshSelectedProjectUseCase =
                refreshSelectedProjectUseCase;
        this.dependencyItemsService =
                dependencyItemsService;
    }

    public void refreshEverything()
            throws IOException {

        SpringProject refreshedProject =
                refreshSelectedProjectUseCase.refresh(
                        uiState.selectedProject());

        uiState.replaceSelectedProject(
                refreshedProject);

        dependencyItemsService.refresh();
    }

    public void refreshDependencies() {

        dependencyItemsService.refresh();
    }

    public void refreshSelectedProject()
            throws IOException {

        SpringProject refreshedProject =
                refreshSelectedProjectUseCase.refresh(
                        uiState.selectedProject());

        uiState.replaceSelectedProject(
                refreshedProject);
    }
}