package io.github.avinashio.lazyspringboot.ui.component;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatusFormatterTest {

    private StatusFormatter formatter;

    @BeforeEach
    void setUp() {

        formatter =
                new StatusFormatter(
                        new Spinner(),
                        new TerminalStyle());
    }

    @Test
    void shouldRotateStartingIcon() {

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

        assertThat(
                formatter.format(
                        ProjectProcessStatus.STARTING))
                .contains("[|]")
                .contains("STARTING");
    }

    @Test
    void shouldFormatRunning() {

        assertThat(
                formatter.format(
                        ProjectProcessStatus.RUNNING))
                .contains("[✓]")
                .contains("RUNNING");
    }

    @Test
    void shouldFormatStopped() {

        assertThat(
                formatter.format(
                        ProjectProcessStatus.STOPPED))
                .contains("[ ]")
                .contains("STOPPED");
    }

    @Test
    void shouldFormatFailed() {

        assertThat(
                formatter.format(
                        ProjectProcessStatus.FAILED))
                .contains("[✗]")
                .contains("FAILED");
    }
}