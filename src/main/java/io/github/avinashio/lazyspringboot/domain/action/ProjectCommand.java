package io.github.avinashio.lazyspringboot.domain.action;

import java.nio.file.Path;
import java.util.List;

public record ProjectCommand(
        List<String> arguments,
        Path workingDirectory) {

    public ProjectCommand {
        arguments = List.copyOf(arguments);
    }
}