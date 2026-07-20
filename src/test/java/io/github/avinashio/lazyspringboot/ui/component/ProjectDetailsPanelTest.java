package io.github.avinashio.lazyspringboot.ui.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ProjectDetailsPanelTest {

    @Test
    void shouldUseEndTimeForStoppedProcessUptime() {

        GetProjectProcessUseCase getProjectProcessUseCase =
                mock(
                        GetProjectProcessUseCase.class);

        StatusFormatter statusFormatter =
                mock(
                        StatusFormatter.class);

        SpringProject project =
                project();

        Instant startedAt =
                Instant.parse(
                        "2026-07-20T10:00:00Z");

        Instant endedAt =
                Instant.parse(
                        "2026-07-20T10:15:30Z");

        ProjectProcess process =
                new ProjectProcess(
                        "test-app",
                        ProjectProcessStatus.STOPPED,
                        List.of(),
                        0,
                        12345L,
                        startedAt,
                        endedAt);

        when(getProjectProcessUseCase.get(
                project))
                .thenReturn(
                        Optional.of(
                                process));

        when(statusFormatter.format(
                ProjectProcessStatus.STOPPED))
                .thenReturn(
                        "[ ] STOPPED");

        ProjectDetailsPanel panel =
                createPanel(
                        statusFormatter,
                        getProjectProcessUseCase);

        List<String> lines =
                panel.render(
                        project);

        assertThat(lines)
                .anySatisfy(line ->
                        assertThat(line)
                                .contains(
                                        "Status",
                                        "[ ] STOPPED"));

        assertThat(lines)
                .anySatisfy(line ->
                        assertThat(line)
                                .contains(
                                        "PID",
                                        "12345"));

        assertThat(lines)
                .anySatisfy(line ->
                        assertThat(line)
                                .contains(
                                        "Uptime",
                                        "00:15:30"));

        assertThat(lines)
                .anySatisfy(line ->
                        assertThat(line)
                                .contains(
                                        "Exit Code",
                                        "0"));
    }

    @Test
    void shouldUseEndTimeForFailedProcessUptime() {

        GetProjectProcessUseCase getProjectProcessUseCase =
                mock(
                        GetProjectProcessUseCase.class);

        StatusFormatter statusFormatter =
                mock(
                        StatusFormatter.class);

        SpringProject project =
                project();

        Instant startedAt =
                Instant.parse(
                        "2026-07-20T10:00:00Z");

        Instant endedAt =
                Instant.parse(
                        "2026-07-20T10:02:05Z");

        ProjectProcess process =
                new ProjectProcess(
                        "test-app",
                        ProjectProcessStatus.FAILED,
                        List.of(),
                        1,
                        12345L,
                        startedAt,
                        endedAt);

        when(getProjectProcessUseCase.get(
                project))
                .thenReturn(
                        Optional.of(
                                process));

        when(statusFormatter.format(
                ProjectProcessStatus.FAILED))
                .thenReturn(
                        "[✗] FAILED");

        ProjectDetailsPanel panel =
                createPanel(
                        statusFormatter,
                        getProjectProcessUseCase);

        List<String> lines =
                panel.render(
                        project);

        assertThat(lines)
                .anySatisfy(line ->
                        assertThat(line)
                                .contains(
                                        "Status",
                                        "[✗] FAILED"));

        assertThat(lines)
                .anySatisfy(line ->
                        assertThat(line)
                                .contains(
                                        "PID",
                                        "12345"));

        assertThat(lines)
                .anySatisfy(line ->
                        assertThat(line)
                                .contains(
                                        "Uptime",
                                        "00:02:05"));

        assertThat(lines)
                .anySatisfy(line ->
                        assertThat(line)
                                .contains(
                                        "Exit Code",
                                        "1"));
    }

    private SpringProject project() {

        SpringProject project =
                mock(
                        SpringProject.class);

        ProjectMetadata metadata =
                new ProjectMetadata(
                        "com.example",
                        "test-app",
                        "4.1.0",
                        "21",
                        BuildTool.MAVEN,
                        List.of());

        when(project.metadata())
                .thenReturn(
                        metadata);

        when(project.path())
                .thenReturn(
                        Path.of(
                                "/tmp/test-app"));

        return project;
    }

    private ProjectDetailsPanel createPanel(
            StatusFormatter statusFormatter,
            GetProjectProcessUseCase getProjectProcessUseCase) {

        return new ProjectDetailsPanel(
                new TextFormatter(),
                statusFormatter,
                new DurationFormatter(),
                getProjectProcessUseCase);
    }
}