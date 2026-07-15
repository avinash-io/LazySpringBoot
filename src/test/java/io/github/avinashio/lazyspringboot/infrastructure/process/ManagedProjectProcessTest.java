package io.github.avinashio.lazyspringboot.infrastructure.process;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import org.junit.jupiter.api.Test;

class ManagedProjectProcessTest {

    private final Process process =
            mock(Process.class);

    @Test
    void shouldStartInStartingState() {
        ManagedProjectProcess managedProcess =
                managedProcess();

        ProjectProcess snapshot =
                managedProcess.snapshot();

        assertThat(snapshot.status())
                .isEqualTo(
                        ProjectProcessStatus.STARTING);

        assertThat(snapshot.exitCode())
                .isNull();

        assertThat(
                managedProcess.stopRequested())
                .isFalse();
    }

    @Test
    void shouldMarkProcessRunning() {
        ManagedProjectProcess managedProcess =
                managedProcess();

        managedProcess.markRunning();

        assertThat(
                managedProcess
                        .snapshot()
                        .status())
                .isEqualTo(
                        ProjectProcessStatus.RUNNING);
    }

    @Test
    void shouldRemainStartingForNormalOutput() {
        ManagedProjectProcess managedProcess =
                managedProcess();

        managedProcess.addOutput(
                "Starting DemoApplication");

        managedProcess.addOutput(
                "Tomcat initialized with port 8080");

        assertThat(
                managedProcess
                        .snapshot()
                        .status())
                .isEqualTo(
                        ProjectProcessStatus.STARTING);
    }

    @Test
    void shouldMarkRunningWhenSpringBootStarts() {
        ManagedProjectProcess managedProcess =
                managedProcess();

        managedProcess.addOutput(
                "Started DemoApplication in 1.284 seconds");

        assertThat(
                managedProcess
                        .snapshot()
                        .status())
                .isEqualTo(
                        ProjectProcessStatus.RUNNING);
    }

    @Test
    void shouldNotMarkRunningForUnrelatedStartedOutput() {
        ManagedProjectProcess managedProcess =
                managedProcess();

        managedProcess.addOutput(
                "Started background worker");

        assertThat(
                managedProcess
                        .snapshot()
                        .status())
                .isEqualTo(
                        ProjectProcessStatus.STARTING);
    }

    @Test
    void shouldRememberStopRequest() {
        ManagedProjectProcess managedProcess =
                managedProcess();

        managedProcess.requestStop();

        assertThat(
                managedProcess.stopRequested())
                .isTrue();
    }

    @Test
    void shouldMarkRequestedStopAsStopped() {
        ManagedProjectProcess managedProcess =
                managedProcess();

        managedProcess.markRunning();

        managedProcess.requestStop();

        managedProcess.markStopped(143);

        ProjectProcess snapshot =
                managedProcess.snapshot();

        assertThat(snapshot.status())
                .isEqualTo(
                        ProjectProcessStatus.STOPPED);

        assertThat(snapshot.exitCode())
                .isEqualTo(143);
    }

    @Test
    void shouldMarkUnexpectedFailureAsFailed() {
        ManagedProjectProcess managedProcess =
                managedProcess();

        managedProcess.markRunning();

        managedProcess.markFailed(1);

        ProjectProcess snapshot =
                managedProcess.snapshot();

        assertThat(snapshot.status())
                .isEqualTo(
                        ProjectProcessStatus.FAILED);

        assertThat(snapshot.exitCode())
                .isEqualTo(1);
    }

    @Test
    void shouldCopyProcessOutputIntoSnapshot() {
        ManagedProjectProcess managedProcess =
                managedProcess();

        managedProcess.addOutput(
                "Starting application");

        managedProcess.addOutput(
                "Application started");

        assertThat(
                managedProcess
                        .snapshot()
                        .output())
                .containsExactly(
                        "Starting application",
                        "Application started");
    }

    private ManagedProjectProcess
    managedProcess() {
        return new ManagedProjectProcess(
                "demo",
                process);
    }
}