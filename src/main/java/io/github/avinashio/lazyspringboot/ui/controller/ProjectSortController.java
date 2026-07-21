package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.ui.state.ProjectSortState;
import org.springframework.stereotype.Component;

@Component
public class ProjectSortController {

    private final ProjectSortState
            projectSortState;

    public ProjectSortController(
            ProjectSortState projectSortState) {

        this.projectSortState =
                projectSortState;
    }

    public void cycle() {

        projectSortState.next();
    }
}