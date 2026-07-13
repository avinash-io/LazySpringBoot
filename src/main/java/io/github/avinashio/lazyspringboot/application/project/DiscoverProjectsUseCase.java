package io.github.avinashio.lazyspringboot.application.project;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.filesystem.ProjectScanner;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DiscoverProjectsUseCase {

    private final ProjectScanner projectScanner;

    public DiscoverProjectsUseCase(ProjectScanner projectScanner) {
        this.projectScanner = projectScanner;
    }

    public List<SpringProject> discover(Path directory) throws IOException {
        return projectScanner.scan(directory);
    }
}