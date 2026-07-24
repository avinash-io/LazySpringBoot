package io.github.avinashio.lazyspringboot.ui.service;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.model.ProjectRuntimeInfo;
import org.springframework.stereotype.Component;
import io.github.avinashio.lazyspringboot.ui.runtime.StatusProvider;
import io.github.avinashio.lazyspringboot.ui.runtime.UptimeProvider;
import io.github.avinashio.lazyspringboot.ui.runtime.PortProvider;

@Component
public class ProjectRuntimeInfoFactory {

    private final GetProjectProcessUseCase
            getProjectProcessUseCase;

    private final StatusProvider
            statusProvider;

    private final UptimeProvider
            uptimeProvider;

    private final PortProvider
            portProvider;

    public ProjectRuntimeInfoFactory(
            GetProjectProcessUseCase getProjectProcessUseCase,
            StatusProvider statusProvider, UptimeProvider uptimeProvider, PortProvider portProvider) {

        this.getProjectProcessUseCase =
                getProjectProcessUseCase;
        this.statusProvider = statusProvider;
        this.uptimeProvider = uptimeProvider;
        this.portProvider = portProvider;
    }

    public ProjectRuntimeInfo create(
            SpringProject project) {

        return getProjectProcessUseCase
                .get(project)
                .map(this::create)
                .orElse(
                        new ProjectRuntimeInfo(
                                statusProvider.stopped(),
                                portProvider.unavailable(),
                                uptimeProvider.unavailable()));
    }

    private ProjectRuntimeInfo create(
            ProjectProcess process) {

        return new ProjectRuntimeInfo(
                statusProvider.status(
                        process),
                portProvider.port(
                        process),
                uptimeProvider.uptime(
                        process));
    }

}