package io.github.avinashio.lazyspringboot.ui.state;

import io.github.avinashio.lazyspringboot.service.WorkspaceService;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class ProjectSortState {

    private final WorkspaceService
            workspaceService;

    private ProjectSortMode mode;

    public ProjectSortState(
            WorkspaceService workspaceService) {

        this.workspaceService =
                workspaceService;

        this.mode =
                workspaceService.projectSortMode();
    }

    public ProjectSortMode mode() {
        return mode;
    }

    public void next()
            throws IOException {

        ProjectSortMode nextMode =
                mode.next();

        workspaceService.changeProjectSortMode(
                nextMode);

        mode =
                nextMode;
    }
}