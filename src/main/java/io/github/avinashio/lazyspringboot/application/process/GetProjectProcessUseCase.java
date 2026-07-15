package io.github.avinashio.lazyspringboot.application.process;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.process.ProjectProcessManager;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GetProjectProcessUseCase {

    private final ProjectProcessManager
            projectProcessManager;

    public GetProjectProcessUseCase(
            ProjectProcessManager projectProcessManager) {
        this.projectProcessManager =
                projectProcessManager;
    }

    public Optional<ProjectProcess> get(
            SpringProject project) {
        return projectProcessManager.find(
                project);
    }
}