package io.github.avinashio.lazyspringboot.ui.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.model.ProjectRuntimeInfo;
import io.github.avinashio.lazyspringboot.ui.runtime.PortProvider;
import io.github.avinashio.lazyspringboot.ui.runtime.StatusProvider;
import io.github.avinashio.lazyspringboot.ui.runtime.UptimeProvider;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ProjectRuntimeInfoFactoryTest {

    @Mock
    private GetProjectProcessUseCase
            getProjectProcessUseCase;

    @Mock
    private StatusProvider
            statusProvider;

    @Mock
    private UptimeProvider
            uptimeProvider;

    @Mock
    private PortProvider
            portProvider;

    private ProjectRuntimeInfoFactory
            factory;

    @BeforeEach
    void setUp() {

        factory =
                new ProjectRuntimeInfoFactory(
                        getProjectProcessUseCase,
                        statusProvider,
                        uptimeProvider,
                        portProvider);
    }

    @Test
    void shouldAssembleRuntimeInfo() {

        SpringProject project =
                project();

        ProjectProcess process =
                process();

        when(getProjectProcessUseCase.get(project))
                .thenReturn(Optional.of(process));

        when(statusProvider.status(process))
                .thenReturn(ProjectProcessStatus.RUNNING);

        when(portProvider.port(process))
                .thenReturn("8080");

        when(uptimeProvider.uptime(process))
                .thenReturn("00:10:15");

        ProjectRuntimeInfo runtime =
                factory.create(project);

        assertThat(runtime.status())
                .isEqualTo(ProjectProcessStatus.RUNNING);

        assertThat(runtime.port())
                .isEqualTo("8080");

        assertThat(runtime.uptime())
                .isEqualTo("00:10:15");

        verify(statusProvider)
                .status(process);

        verify(portProvider)
                .port(process);

        verify(uptimeProvider)
                .uptime(process);
    }

    @Test
    void shouldReturnUnavailableWhenProcessMissing() {

        SpringProject project =
                project();

        when(getProjectProcessUseCase.get(project))
                .thenReturn(Optional.empty());

        when(statusProvider.stopped())
                .thenReturn(ProjectProcessStatus.STOPPED);

        when(portProvider.unavailable())
                .thenReturn("-");

        when(uptimeProvider.unavailable())
                .thenReturn("-");

        ProjectRuntimeInfo runtime =
                factory.create(project);

        assertThat(runtime.status())
                .isEqualTo(ProjectProcessStatus.STOPPED);

        assertThat(runtime.port())
                .isEqualTo("-");

        assertThat(runtime.uptime())
                .isEqualTo("-");
    }

    private SpringProject project() {

        return new SpringProject(
                "demo",
                Path.of("/workspace/demo"),
                new ProjectMetadata(
                        "com.example",
                        "demo",
                        "4.1.0",
                        "26",
                        BuildTool.MAVEN,
                        List.of()));
    }

    private ProjectProcess process() {

        return new ProjectProcess(
                "demo",
                ProjectProcessStatus.RUNNING,
                List.of(),
                null,
                12345L,
                Instant.now(),
                null);
    }
}