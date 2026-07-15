package io.github.avinashio.lazyspringboot.domain.process;

import java.util.List;

public record ProjectProcess(
        String projectName,
        ProjectProcessStatus status,
        List<String> output,
        Integer exitCode) {

    public ProjectProcess {
        output = List.copyOf(output);
    }

    public boolean running() {
        return status == ProjectProcessStatus.STARTING
                || status == ProjectProcessStatus.RUNNING;
    }
}