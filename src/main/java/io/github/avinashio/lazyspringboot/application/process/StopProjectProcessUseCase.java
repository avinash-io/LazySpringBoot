package io.github.avinashio.lazyspringboot.application.process;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.process.ProjectProcessManager;
import org.springframework.stereotype.Service;

@Service
public class StopProjectProcessUseCase {

    private final ProjectProcessManager
            projectProcessManager;

    public StopProjectProcessUseCase(
            ProjectProcessManager projectProcessManager) {
        this.projectProcessManager =
                projectProcessManager;
    }

    public boolean stop(
            SpringProject project) {
        return projectProcessManager.stop(
                project);
    }
}