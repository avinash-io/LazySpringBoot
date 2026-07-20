package io.github.avinashio.lazyspringboot.application.action;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ProjectActionCatalogTest {

    private static final long PID = 12345L;

    private static final Instant STARTED_AT =
            Instant.parse("2026-01-01T00:00:00Z");

    private final ProjectActionCatalog catalog =
            new ProjectActionCatalog();

    @Test
    void shouldShowRunWhenProjectIsNotRunning() {

        var actions =
                catalog.actions(
                        Optional.empty());

        assertThat(actions)
                .extracting(item -> item.action())
                .containsExactly(
                        ProjectAction.BUILD,
                        ProjectAction.TEST,
                        ProjectAction.RUN);
    }

    @Test
    void shouldShowProcessActionsWhenProjectIsRunning() {

        var actions =
                catalog.actions(
                        Optional.of(
                                process(
                                        ProjectProcessStatus.RUNNING,
                                        List.of(),
                                        null)));

        assertThat(actions)
                .extracting(item -> item.action())
                .containsExactly(
                        ProjectAction.BUILD,
                        ProjectAction.TEST,
                        ProjectAction.VIEW_LOGS,
                        ProjectAction.RESTART,
                        ProjectAction.STOP);
    }

    @Test
    void shouldShowProcessActionsWhenProjectIsStarting() {

        var actions =
                catalog.actions(
                        Optional.of(
                                process(
                                        ProjectProcessStatus.STARTING,
                                        List.of(),
                                        null)));

        assertThat(actions)
                .extracting(item -> item.action())
                .containsExactly(
                        ProjectAction.BUILD,
                        ProjectAction.TEST,
                        ProjectAction.VIEW_LOGS,
                        ProjectAction.RESTART,
                        ProjectAction.STOP);
    }

    @Test
    void shouldShowRunWhenProjectHasStopped() {

        var actions =
                catalog.actions(
                        Optional.of(
                                process(
                                        ProjectProcessStatus.STOPPED,
                                        List.of(),
                                        0)));

        assertThat(actions)
                .extracting(item -> item.action())
                .containsExactly(
                        ProjectAction.BUILD,
                        ProjectAction.TEST,
                        ProjectAction.RUN);
    }

    @Test
    void shouldShowRunWhenProjectHasFailed() {

        var actions =
                catalog.actions(
                        Optional.of(
                                process(
                                        ProjectProcessStatus.FAILED,
                                        List.of(
                                                "Application failed"),
                                        1)));

        assertThat(actions)
                .extracting(item -> item.action())
                .containsExactly(
                        ProjectAction.BUILD,
                        ProjectAction.TEST,
                        ProjectAction.RUN);
    }

    private ProjectProcess process(
            ProjectProcessStatus status,
            List<String> output,
            Integer exitCode) {

        return new ProjectProcess(
                "test-project",
                status,
                List.of(),
                null,
                12345L,
                Instant.now(),
                null);
    }
}