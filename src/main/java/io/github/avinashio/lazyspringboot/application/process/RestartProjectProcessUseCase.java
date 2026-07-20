package io.github.avinashio.lazyspringboot.application.process;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.process.ProjectProcessManager;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class RestartProjectProcessUseCase {

    private final ProjectProcessManager
            projectProcessManager;

    public RestartProjectProcessUseCase(
            ProjectProcessManager projectProcessManager) {

        this.projectProcessManager =
                projectProcessManager;
    }

    public ProjectProcess restart(
            SpringProject project)
            throws IOException {

        return projectProcessManager.restart(
                project);
    }
}