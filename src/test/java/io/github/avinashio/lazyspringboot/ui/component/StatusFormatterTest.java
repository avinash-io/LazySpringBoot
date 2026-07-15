package io.github.avinashio.lazyspringboot.ui.component;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import org.junit.jupiter.api.Test;

class StatusFormatterTest {

    @Test
    void shouldRotateStartingIcon() {

        StatusFormatter formatter =
                new StatusFormatter(
                        new Spinner());

        assertThat(
                formatter.icon(
                        ProjectProcessStatus.STARTING))
                .isEqualTo("[|]");

        assertThat(
                formatter.icon(
                        ProjectProcessStatus.STARTING))
                .isEqualTo("[/]");

        assertThat(
                formatter.icon(
                        ProjectProcessStatus.STARTING))
                .isEqualTo("[-]");

        assertThat(
                formatter.icon(
                        ProjectProcessStatus.STARTING))
                .isEqualTo("[\\]");
    }

    @Test
    void shouldFormatStarting() {

        StatusFormatter formatter =
                new StatusFormatter(
                        new Spinner());

        assertThat(
                formatter.format(
                        ProjectProcessStatus.STARTING))
                .isEqualTo("[|] STARTING");
    }

    @Test
    void shouldFormatRunning() {

        StatusFormatter formatter =
                new StatusFormatter(
                        new Spinner());

        assertThat(
                formatter.format(
                        ProjectProcessStatus.RUNNING))
                .isEqualTo("[✓] RUNNING");
    }

    @Test
    void shouldFormatStopped() {

        StatusFormatter formatter =
                new StatusFormatter(
                        new Spinner());

        assertThat(
                formatter.format(
                        ProjectProcessStatus.STOPPED))
                .isEqualTo("[ ] STOPPED");
    }

    @Test
    void shouldFormatFailed() {

        StatusFormatter formatter =
                new StatusFormatter(
                        new Spinner());

        assertThat(
                formatter.format(
                        ProjectProcessStatus.FAILED))
                .isEqualTo("[✗] FAILED");
    }
}