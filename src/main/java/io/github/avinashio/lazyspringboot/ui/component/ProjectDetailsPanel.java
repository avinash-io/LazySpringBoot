package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ProjectDetailsPanel {

    private static final int LABEL_WIDTH = 14;

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern(
                    "HH:mm:ss");

    private final TextFormatter textFormatter;

    private final StatusFormatter statusFormatter;

    private final DurationFormatter durationFormatter;

    private final GetProjectProcessUseCase
            getProjectProcessUseCase;

    public ProjectDetailsPanel(
            TextFormatter textFormatter,
            StatusFormatter statusFormatter,
            DurationFormatter durationFormatter,
            GetProjectProcessUseCase getProjectProcessUseCase) {

        this.textFormatter = textFormatter;
        this.statusFormatter = statusFormatter;
        this.durationFormatter =
                durationFormatter;
        this.getProjectProcessUseCase =
                getProjectProcessUseCase;
    }

    public List<String> render(
            SpringProject project) {

        if (project == null) {
            return List.of();
        }

        ProjectMetadata metadata =
                project.metadata();

        List<String> lines =
                new ArrayList<>();

        lines.add(detail(
                "Artifact",
                metadata.artifactId()));

        lines.add(detail(
                "Group",
                metadata.groupId()));

        lines.add(detail(
                "Spring Boot",
                metadata.springBootVersion()));

        lines.add(detail(
                "Java",
                metadata.javaVersion()));

        lines.add(detail(
                "Build",
                metadata.buildTool().name()));

        lines.add(detail(
                "Path",
                project.path().toString()));

        lines.add("");

        addProcessDetails(
                lines,
                project);

        return List.copyOf(lines);
    }

    private void addProcessDetails(
            List<String> lines,
            SpringProject project) {

        Optional<ProjectProcess> process =
                getProjectProcessUseCase.get(
                        project);

        if (process.isEmpty()) {

            lines.add(detail(
                    "Status",
                    statusFormatter.stopped()));

            return;
        }

        ProjectProcess projectProcess =
                process.get();

        lines.add(detail(
                "Status",
                statusFormatter.format(
                        projectProcess.status())));

        if (projectProcess.hasProcessId()) {

            lines.add(detail(
                    "PID",
                    Long.toString(
                            projectProcess.pid())));
        }

        if (projectProcess.hasStartTime()) {

            Instant startedAt =
                    projectProcess.startedAt();

            lines.add(detail(
                    "Started",
                    TIME_FORMATTER.format(
                            startedAt.atZone(
                                    ZoneId.systemDefault()))));

            Instant uptimeEnd =
                    projectProcess.hasEndTime()
                            ? projectProcess.endedAt()
                            : Instant.now();

            lines.add(detail(
                    "Uptime",
                    durationFormatter.format(
                            Duration.between(
                                    startedAt,
                                    uptimeEnd))));
        }

        if (projectProcess.exitCode() != null) {

            lines.add(detail(
                    "Exit Code",
                    projectProcess.exitCode()
                            .toString()));
        }
    }

    private String detail(
            String label,
            String value) {

        return " "
                + textFormatter.fit(
                label,
                LABEL_WIDTH)
                + displayValue(value);
    }

    private String displayValue(
            String value) {

        if (value == null
                || value.isBlank()) {
            return "Unknown";
        }

        return value;
    }
}