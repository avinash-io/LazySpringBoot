package io.github.avinashio.lazyspringboot.domain.action;

import java.util.List;

public record ProjectActionOutput(
        String projectName,
        ProjectAction action,
        int exitCode,
        List<String> lines) {

    public ProjectActionOutput {
        lines = List.copyOf(lines);
    }

    public boolean successful() {
        return exitCode == 0;
    }
}
