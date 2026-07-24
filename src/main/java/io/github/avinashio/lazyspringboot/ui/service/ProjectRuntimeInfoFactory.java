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

@Component
public class ProjectRuntimeInfoFactory {

    private final GetProjectProcessUseCase
            getProjectProcessUseCase;

    private final StatusProvider
            statusProvider;

    public ProjectRuntimeInfoFactory(
            GetProjectProcessUseCase getProjectProcessUseCase,
            StatusProvider statusProvider) {

        this.getProjectProcessUseCase =
                getProjectProcessUseCase;
        this.statusProvider = statusProvider;
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
                "-",
                uptime(process));
    }

    private String uptime(
            ProjectProcess process) {

        if (!process.hasStartTime()) {

            return "-";
        }

        Duration duration =
                Duration.between(
                        process.startedAt(),
                        Instant.now());

        long hours =
                duration.toHours();

        long minutes =
                duration.toMinutesPart();

        long seconds =
                duration.toSecondsPart();

        return String.format(
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds);
    }
}