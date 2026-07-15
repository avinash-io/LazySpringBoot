package io.github.avinashio.lazyspringboot.infrastructure.process;

import io.github.avinashio.lazyspringboot.domain.action.CommandResult;
import io.github.avinashio.lazyspringboot.domain.action.ProjectCommand;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CommandExecutor {

    public CommandResult execute(
            ProjectCommand command)
            throws IOException,
            InterruptedException {
        ProcessBuilder processBuilder =
                new ProcessBuilder(
                        command.arguments());

        processBuilder.directory(
                command
                        .workingDirectory()
                        .toFile());

        processBuilder.redirectErrorStream(true);

        Process process =
                processBuilder.start();

        List<String> output =
                readOutput(process);

        int exitCode =
                process.waitFor();

        return new CommandResult(
                exitCode,
                output);
    }

    private List<String> readOutput(
            Process process)
            throws IOException {
        List<String> output =
                new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     process.getInputStream(),
                                     StandardCharsets.UTF_8))) {
            String line;

            while ((line = reader.readLine())
                    != null) {
                output.add(line);
            }
        }

        return List.copyOf(output);
    }
}