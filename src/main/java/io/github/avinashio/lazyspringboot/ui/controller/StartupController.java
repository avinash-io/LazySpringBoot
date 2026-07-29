package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.initializr.GetInitializrConfigurationUseCase;
import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;
import io.github.avinashio.lazyspringboot.domain.initializr.InitializrConfiguration;
import io.github.avinashio.lazyspringboot.service.WorkspaceService;
import io.github.avinashio.lazyspringboot.ui.service.DependencyItemsService;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import io.github.avinashio.lazyspringboot.ui.state.Screen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import io.github.avinashio.lazyspringboot.ui.service.InstalledToolsService;

@Component
public class StartupController {

    private final UiState uiState;

    private final DiscoverProjectsUseCase
            discoverProjectsUseCase;

    private final GetInitializrConfigurationUseCase
            getInitializrConfigurationUseCase;

    private final CreateProjectState
            createProjectState;

    private final DependencyItemsService
            dependencyItemsService;

    private final InstalledToolsService
            installedToolsService;

    private final WorkspaceService workspaceService;

    public StartupController(
            UiState uiState,
            DiscoverProjectsUseCase discoverProjectsUseCase,
            GetInitializrConfigurationUseCase getInitializrConfigurationUseCase,
            CreateProjectState createProjectState,
            DependencyItemsService dependencyItemsService, InstalledToolsService installedToolsService,
            WorkspaceService workspaceService) {

        this.uiState = uiState;
        this.discoverProjectsUseCase =
                discoverProjectsUseCase;
        this.dependencyItemsService =
                dependencyItemsService;
        this.getInitializrConfigurationUseCase =
                getInitializrConfigurationUseCase;
        this.createProjectState =
                createProjectState;
        this.installedToolsService = installedToolsService;
        this.workspaceService = workspaceService;
    }

    public void initialize()
            throws IOException,
            InterruptedException {

        uiState.setProjects(
                discoverProjectsUseCase.discover());

        installedToolsService.refresh();

        initializeScreen();

        InitializrConfiguration configuration =
                getInitializrConfigurationUseCase
                        .getConfiguration();

        dependencyItemsService.initialize(
                configuration.dependencies());

        createProjectState.setAvailableJavaVersions(
                configuration.javaVersions());

        createProjectState.setJavaVersion(
                configuration.defaultJavaVersion());

        createProjectState
                .setAvailableSpringBootVersions(
                        configuration
                                .springBootVersions());

        createProjectState.setSpringBootVersion(
                configuration
                        .defaultSpringBootVersion());

        dependencyItemsService.refresh();
    }

    private void initializeScreen() {

        if (uiState.projects().isEmpty()) {

            uiState.showScreen(
                    Screen.WORKSPACE_WELCOME);

            return;
        }

        uiState.showScreen(
                Screen.DASHBOARD);
    }
}