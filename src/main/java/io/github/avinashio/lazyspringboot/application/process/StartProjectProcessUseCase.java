package io.github.avinashio.lazyspringboot.application.process;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.process.ProjectProcessManager;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class StartProjectProcessUseCase {

    private final ProjectProcessManager
            projectProcessManager;

    public StartProjectProcessUseCase(
            ProjectProcessManager projectProcessManager) {
        this.projectProcessManager =
                projectProcessManager;
    }

    public ProjectProcess start(
            SpringProject project)
            throws IOException {
        return projectProcessManager.start(
                project);
    }
}