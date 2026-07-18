package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.dependency.GetDependenciesUseCase;
import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;
import io.github.avinashio.lazyspringboot.ui.service.DependencyItemsService;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.IOException;
import java.nio.file.Path;
import org.springframework.stereotype.Component;

@Component
public class StartupController {

    private final UiState uiState;

    private final DiscoverProjectsUseCase
            discoverProjectsUseCase;

    private final GetDependenciesUseCase
            getDependenciesUseCase;

    private final DependencyItemsService
            dependencyItemsService;

    public StartupController(
            UiState uiState,
            DiscoverProjectsUseCase discoverProjectsUseCase,
            GetDependenciesUseCase getDependenciesUseCase,
            DependencyItemsService dependencyItemsService) {

        this.uiState = uiState;
        this.discoverProjectsUseCase =
                discoverProjectsUseCase;
        this.getDependenciesUseCase =
                getDependenciesUseCase;
        this.dependencyItemsService =
                dependencyItemsService;
    }

    public void initialize()
            throws IOException,
            InterruptedException {

        Path currentDirectory =
                Path.of("")
                        .toAbsolutePath();

        uiState.setProjects(
                discoverProjectsUseCase.discover(
                        currentDirectory));

        dependencyItemsService.initialize(
                getDependenciesUseCase.getDependencies());

        dependencyItemsService.refresh();
    }
}