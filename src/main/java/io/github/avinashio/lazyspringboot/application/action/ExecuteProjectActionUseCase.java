package io.github.avinashio.lazyspringboot.application.action;

import io.github.avinashio.lazyspringboot.domain.action.CommandResult;
import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectCommand;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.process.CommandExecutor;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class ExecuteProjectActionUseCase {

    private final ProjectCommandResolver
            commandResolver;

    private final CommandExecutor commandExecutor;

    public ExecuteProjectActionUseCase(
            ProjectCommandResolver commandResolver,
            CommandExecutor commandExecutor) {
        this.commandResolver =
                commandResolver;
        this.commandExecutor =
                commandExecutor;
    }

    public CommandResult execute(
            SpringProject project,
            ProjectAction action)
            throws IOException,
            InterruptedException {
        ProjectCommand command =
                commandResolver.resolve(
                        project,
                        action);

        return commandExecutor.execute(
                command);
    }
}