package io.github.avinashio.lazyspringboot.infrastructure.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ProcessCommandExecutor {

    public Optional<String> execute(
            String... command) {

        ProcessBuilder processBuilder =
                new ProcessBuilder(command);

        processBuilder.redirectErrorStream(true);

        try {

            Process process =
                    processBuilder.start();

            try (BufferedReader reader =
                         new BufferedReader(
                                 new InputStreamReader(
                                         process.getInputStream(),
                                         StandardCharsets.UTF_8))) {

                String output =
                        reader.lines()
                                .collect(
                                        Collectors.joining("\n"))
                                .trim();

                int exitCode =
                        process.waitFor();

                if (exitCode != 0
                        || output.isBlank()) {

                    return Optional.empty();
                }

                return Optional.of(output);
            }

        } catch (IOException
                 | InterruptedException exception) {

            if (exception
                    instanceof InterruptedException) {

                Thread.currentThread()
                        .interrupt();
            }

            return Optional.empty();
        }
    }
}