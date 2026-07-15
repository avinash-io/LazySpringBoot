package io.github.avinashio.lazyspringboot.infrastructure.process;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import java.util.ArrayList;
import java.util.List;

final class ManagedProjectProcess {

    private static final String STARTED_APPLICATION_MARKER =
            "Started ";

    private final String projectName;

    private final Process process;

    private final List<String> output =
            new ArrayList<>();

    private ProjectProcessStatus status =
            ProjectProcessStatus.STARTING;

    private Integer exitCode;

    private boolean stopRequested;

    ManagedProjectProcess(
            String projectName,
            Process process) {
        this.projectName = projectName;
        this.process = process;
    }

    synchronized void addOutput(
            String line) {
        output.add(line);

        inspectOutput(line);
    }

    synchronized void markRunning() {
        if (status
                == ProjectProcessStatus.STARTING) {
            status =
                    ProjectProcessStatus.RUNNING;
        }
    }

    synchronized void requestStop() {
        stopRequested = true;
    }

    synchronized boolean stopRequested() {
        return stopRequested;
    }

    synchronized void markStopped(
            int processExitCode) {
        exitCode = processExitCode;

        if (status
                != ProjectProcessStatus.FAILED) {
            status =
                    ProjectProcessStatus.STOPPED;
        }
    }

    synchronized void markFailed(
            int processExitCode) {
        exitCode = processExitCode;

        status =
                ProjectProcessStatus.FAILED;
    }

    synchronized ProjectProcess snapshot() {
        return new ProjectProcess(
                projectName,
                status,
                List.copyOf(output),
                exitCode);
    }

    Process process() {
        return process;
    }

    private void inspectOutput(
            String line) {
        if (status
                != ProjectProcessStatus.STARTING) {
            return;
        }

        if (isSpringBootStarted(line)) {
            status =
                    ProjectProcessStatus.RUNNING;
        }
    }

    private boolean isSpringBootStarted(
            String line) {
        return line.contains(
                STARTED_APPLICATION_MARKER)
                && line.contains(
                " in ");
    }
}