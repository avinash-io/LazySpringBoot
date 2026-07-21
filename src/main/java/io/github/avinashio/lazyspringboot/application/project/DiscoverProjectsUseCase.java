package io.github.avinashio.lazyspringboot.application.project;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.filesystem.ProjectScanner;
import io.github.avinashio.lazyspringboot.service.WorkspaceService;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DiscoverProjectsUseCase {

    private final ProjectScanner projectScanner;

    private final WorkspaceService workspaceService;

    public DiscoverProjectsUseCase(
            ProjectScanner projectScanner,
            WorkspaceService workspaceService) {

        this.projectScanner =
                projectScanner;

        this.workspaceService =
                workspaceService;
    }

    public List<SpringProject> discover()
            throws IOException {

        return projectScanner.scan(
                workspaceService.workspace());
    }
}