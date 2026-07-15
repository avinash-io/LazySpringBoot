package io.github.avinashio.lazyspringboot.infrastructure.process;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.action.CommandResult;
import io.github.avinashio.lazyspringboot.domain.action.ProjectCommand;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class CommandExecutorTest {

    private final CommandExecutor commandExecutor =
            new CommandExecutor();

    @Test
    void shouldExecuteCommand()
            throws Exception {
        ProjectCommand command =
                new ProjectCommand(
                        List.of(
                                "sh",
                                "-c",
                                "printf 'build success\\n'"),
                        Path.of("")
                                .toAbsolutePath());

        CommandResult result =
                commandExecutor.execute(command);

        assertThat(result.successful())
                .isTrue();

        assertThat(result.exitCode())
                .isZero();

        assertThat(result.output())
                .containsExactly(
                        "build success");
    }

    @Test
    void shouldCaptureFailedExitCode()
            throws Exception {
        ProjectCommand command =
                new ProjectCommand(
                        List.of(
                                "sh",
                                "-c",
                                "printf 'build failed\\n'; exit 7"),
                        Path.of("")
                                .toAbsolutePath());

        CommandResult result =
                commandExecutor.execute(command);

        assertThat(result.successful())
                .isFalse();

        assertThat(result.exitCode())
                .isEqualTo(7);

        assertThat(result.output())
                .containsExactly(
                        "build failed");
    }

    @Test
    void shouldMergeStandardErrorIntoOutput()
            throws Exception {
        ProjectCommand command =
                new ProjectCommand(
                        List.of(
                                "sh",
                                "-c",
                                "printf 'error message\\n' >&2"),
                        Path.of("")
                                .toAbsolutePath());

        CommandResult result =
                commandExecutor.execute(command);

        assertThat(result.output())
                .containsExactly(
                        "error message");
    }
}