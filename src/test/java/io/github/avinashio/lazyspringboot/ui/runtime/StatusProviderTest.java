package io.github.avinashio.lazyspringboot.ui.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatusProviderTest {

    private StatusProvider
            statusProvider;

    @BeforeEach
    void setUp() {

        statusProvider =
                new StatusProvider();
    }

    @Test
    void shouldReturnRunningStatus() {

        assertThat(
                statusProvider.status(
                        process(
                                ProjectProcessStatus.RUNNING)))
                .isEqualTo(
                        ProjectProcessStatus.RUNNING);
    }

    @Test
    void shouldReturnStartingStatus() {

        assertThat(
                statusProvider.status(
                        process(
                                ProjectProcessStatus.STARTING)))
                .isEqualTo(
                        ProjectProcessStatus.STARTING);
    }

    @Test
    void shouldReturnStoppedStatus() {

        assertThat(
                statusProvider.status(
                        process(
                                ProjectProcessStatus.STOPPED)))
                .isEqualTo(
                        ProjectProcessStatus.STOPPED);
    }

    @Test
    void shouldReturnFailedStatus() {

        assertThat(
                statusProvider.status(
                        process(
                                ProjectProcessStatus.FAILED)))
                .isEqualTo(
                        ProjectProcessStatus.FAILED);
    }

    @Test
    void shouldReturnStoppedAsDefault() {

        assertThat(
                statusProvider.stopped())
                .isEqualTo(
                        ProjectProcessStatus.STOPPED);
    }

    private ProjectProcess process(
            ProjectProcessStatus status) {

        return new ProjectProcess(
                "demo",
                status,
                List.of(),
                null,
                null,
                null,
                null);
    }
}