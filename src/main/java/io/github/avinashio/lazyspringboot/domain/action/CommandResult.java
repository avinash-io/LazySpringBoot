package io.github.avinashio.lazyspringboot.domain.action;

import java.util.List;

public record CommandResult(
        int exitCode,
        List<String> output) {

    public CommandResult {
        output = List.copyOf(output);
    }

    public boolean successful() {
        return exitCode == 0;
    }
}