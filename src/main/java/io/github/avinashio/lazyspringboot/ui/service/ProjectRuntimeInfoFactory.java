package io.github.avinashio.lazyspringboot.ui.service;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.model.ProjectRuntimeInfo;
import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Component;
import io.github.avinashio.lazyspringboot.ui.runtime.StatusProvider;
import io.github.avinashio.lazyspringboot.ui.runtime.UptimeProvider;

@Component
public class ProjectRuntimeInfoFactory {

    private final GetProjectProcessUseCase
            getProjectProcessUseCase;

    private final StatusProvider
            statusProvider;

    private final UptimeProvider
            uptimeProvider;

    public ProjectRuntimeInfoFactory(
            GetProjectProcessUseCase getProjectProcessUseCase,
            StatusProvider statusProvider, UptimeProvider uptimeProvider) {

        this.getProjectProcessUseCase =
                getProjectProcessUseCase;
        this.statusProvider = statusProvider;
        this.uptimeProvider = uptimeProvider;
    }

    public ProjectRuntimeInfo create(
            SpringProject project) {

        return getProjectProcessUseCase
                .get(project)
                .map(this::create)
                .orElse(
                        new ProjectRuntimeInfo(
                                statusProvider.stopped(),
                                "-",
                                "-"));
    }

    private ProjectRuntimeInfo create(
            ProjectProcess process) {

        return new ProjectRuntimeInfo(
                statusProvider.status(process),
                uptimeProvider.unavailable(),
                uptimeProvider.uptime(process));
    }

}