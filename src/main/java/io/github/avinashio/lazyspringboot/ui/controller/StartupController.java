package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.initializr.GetInitializrConfigurationUseCase;
import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;
import io.github.avinashio.lazyspringboot.domain.initializr.InitializrConfiguration;
import io.github.avinashio.lazyspringboot.service.WorkspaceService;
import io.github.avinashio.lazyspringboot.ui.service.DependencyItemsService;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

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

    private final WorkspaceService workspaceService;

    public StartupController(
            UiState uiState,
            DiscoverProjectsUseCase discoverProjectsUseCase,
            GetInitializrConfigurationUseCase getInitializrConfigurationUseCase,
            CreateProjectState createProjectState,
            DependencyItemsService dependencyItemsService,
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
        this.workspaceService = workspaceService;
    }

    public void initialize()
            throws IOException,
            InterruptedException {

        Path workspace =
                workspaceService.workspace();

        uiState.setProjects(
                discoverProjectsUseCase.discover());

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
}