package io.github.avinashio.lazyspringboot.application.action;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ProjectActionCatalogTest {

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
        ProjectProcess process =
                new ProjectProcess(
                        "testboot",
                        ProjectProcessStatus.RUNNING,
                        List.of(),
                        null);

        var actions =
                catalog.actions(
                        Optional.of(process));

        assertThat(actions)
                .extracting(item -> item.action())
                .containsExactly(
                        ProjectAction.BUILD,
                        ProjectAction.TEST,
                        ProjectAction.VIEW_LOGS,
                        ProjectAction.STOP);
    }

    @Test
    void shouldShowProcessActionsWhenProjectIsStarting() {
        ProjectProcess process =
                new ProjectProcess(
                        "testboot",
                        ProjectProcessStatus.STARTING,
                        List.of(),
                        null);

        var actions =
                catalog.actions(
                        Optional.of(process));

        assertThat(actions)
                .extracting(item -> item.action())
                .containsExactly(
                        ProjectAction.BUILD,
                        ProjectAction.TEST,
                        ProjectAction.VIEW_LOGS,
                        ProjectAction.STOP);
    }

    @Test
    void shouldShowRunWhenProjectHasStopped() {
        ProjectProcess process =
                new ProjectProcess(
                        "testboot",
                        ProjectProcessStatus.STOPPED,
                        List.of(),
                        0);

        var actions =
                catalog.actions(
                        Optional.of(process));

        assertThat(actions)
                .extracting(item -> item.action())
                .containsExactly(
                        ProjectAction.BUILD,
                        ProjectAction.TEST,
                        ProjectAction.RUN);
    }

    @Test
    void shouldShowRunWhenProjectHasFailed() {
        ProjectProcess process =
                new ProjectProcess(
                        "testboot",
                        ProjectProcessStatus.FAILED,
                        List.of("Application failed"),
                        1);

        var actions =
                catalog.actions(
                        Optional.of(process));

        assertThat(actions)
                .extracting(item -> item.action())
                .containsExactly(
                        ProjectAction.BUILD,
                        ProjectAction.TEST,
                        ProjectAction.RUN);
    }
}