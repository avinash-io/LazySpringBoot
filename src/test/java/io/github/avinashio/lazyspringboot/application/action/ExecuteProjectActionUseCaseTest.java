package io.github.avinashio.lazyspringboot.application.action;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.avinashio.lazyspringboot.domain.action.CommandResult;
import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectCommand;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.process.CommandExecutor;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExecuteProjectActionUseCaseTest {

    @Test
    void shouldExecuteResolvedProjectCommand()
            throws Exception {
        ProjectCommandResolver resolver =
                Mockito.mock(
                        ProjectCommandResolver.class);

        CommandExecutor executor =
                Mockito.mock(
                        CommandExecutor.class);

        SpringProject project =
                Mockito.mock(
                        SpringProject.class);

        ProjectCommand command =
                Mockito.mock(
                        ProjectCommand.class);

        CommandResult expected =
                new CommandResult(
                        0,
                        List.of("BUILD SUCCESS"));

        when(
                resolver.resolve(
                        project,
                        ProjectAction.BUILD))
                .thenReturn(command);

        when(executor.execute(command))
                .thenReturn(expected);

        ExecuteProjectActionUseCase useCase =
                new ExecuteProjectActionUseCase(
                        resolver,
                        executor);

        CommandResult result =
                useCase.execute(
                        project,
                        ProjectAction.BUILD);

        assertThat(result)
                .isEqualTo(expected);

        verify(resolver)
                .resolve(
                        project,
                        ProjectAction.BUILD);

        verify(executor)
                .execute(command);
    }
}