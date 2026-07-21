package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.IOException;
import java.nio.file.Path;
import org.springframework.stereotype.Component;

@Component
public class ProjectRefreshController {

    private final UiState uiState;

    private final DiscoverProjectsUseCase
            discoverProjectsUseCase;

    public ProjectRefreshController(
            UiState uiState,
            DiscoverProjectsUseCase discoverProjectsUseCase) {

        this.uiState =
                uiState;

        this.discoverProjectsUseCase =
                discoverProjectsUseCase;
    }

    public void refresh()
            throws IOException {

        Path currentDirectory =
                Path.of("")
                        .toAbsolutePath();

        uiState.refreshProjects(
                discoverProjectsUseCase.discover());
    }
}