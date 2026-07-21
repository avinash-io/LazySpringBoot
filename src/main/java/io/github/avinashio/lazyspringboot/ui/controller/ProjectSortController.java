package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.ui.state.ProjectSortState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class ProjectSortController {

    private final ProjectSortState
            projectSortState;

    private final UiState uiState;

    public ProjectSortController(
            ProjectSortState projectSortState,
            UiState uiState) {

        this.projectSortState =
                projectSortState;

        this.uiState =
                uiState;
    }

    public void cycle() {

        try {

            projectSortState.next();

        } catch (IOException exception) {

            uiState.showErrorMessage(
                    "Unable to save project sort mode: "
                            + exception.getMessage());
        }
    }
}