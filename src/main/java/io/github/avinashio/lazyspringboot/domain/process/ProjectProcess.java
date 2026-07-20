package io.github.avinashio.lazyspringboot.domain.process;

import java.time.Instant;
import java.util.List;

public record ProjectProcess(
        String projectName,
        ProjectProcessStatus status,
        List<String> output,
        Integer exitCode,
        Long pid,
        Instant startedAt,
        Instant endedAt) {

    public ProjectProcess {
        output = List.copyOf(output);
    }

    public boolean running() {
        return status == ProjectProcessStatus.STARTING
                || status == ProjectProcessStatus.RUNNING;
    }

    public boolean hasProcessId() {
        return pid != null;
    }

    public boolean hasStartTime() {
        return startedAt != null;
    }

    public boolean hasEndTime() {
        return endedAt != null;
    }
}