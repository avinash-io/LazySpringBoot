package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.ui.state.ProjectSearchState;
import org.springframework.stereotype.Controller;

@Controller
public class ProjectSearchController {

    private final ProjectSearchState
            projectSearchState;

    public ProjectSearchController(
            ProjectSearchState projectSearchState) {

        this.projectSearchState =
                projectSearchState;
    }

    public void start() {
        projectSearchState.start();
    }

    public void stop() {
        projectSearchState.stop();
    }

    public void append(char character) {
        projectSearchState.append(
                character);
    }

    public void backspace() {
        projectSearchState.backspace();
    }

    public boolean active() {
        return projectSearchState.active();
    }

    public String query() {
        return projectSearchState.query();
    }
}