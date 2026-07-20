package io.github.avinashio.lazyspringboot.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import io.github.avinashio.lazyspringboot.domain.action.ActionItem;
import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import org.junit.jupiter.api.Test;

class ProjectActionExecutorTest {

    @Test
    void shouldCreateExecutor() {

        ProjectActionExecutor executor =
                new ProjectActionExecutor(
                        mock(ProcessController.class),
                        mock(ProjectActionController.class));

        assertThat(executor)
                .isNotNull();
    }

    @Test
    void shouldExecuteRunAction() {

        ProcessController processController =
                mock(ProcessController.class);

        SpringProject project =
                mock(SpringProject.class);

        ProjectActionExecutor executor =
                createExecutor(
                        processController);

        executor.execute(
                project,
                actionItem(
                        ProjectAction.RUN));

        verify(processController)
                .start(
                        project);
    }

    @Test
    void shouldExecuteRestartAction() {

        ProcessController processController =
                mock(ProcessController.class);

        SpringProject project =
                mock(SpringProject.class);

        ProjectActionExecutor executor =
                createExecutor(
                        processController);

        executor.execute(
                project,
                actionItem(
                        ProjectAction.RESTART));

        verify(processController)
                .restart(
                        project);
    }

    @Test
    void shouldExecuteStopAction() {

        ProcessController processController =
                mock(ProcessController.class);

        SpringProject project =
                mock(SpringProject.class);

        ProjectActionExecutor executor =
                createExecutor(
                        processController);

        executor.execute(
                project,
                actionItem(
                        ProjectAction.STOP));

        verify(processController)
                .stop(
                        project);
    }

    @Test
    void shouldExecuteViewLogsAction() {

        ProcessController processController =
                mock(ProcessController.class);

        SpringProject project =
                mock(SpringProject.class);

        ProjectActionExecutor executor =
                createExecutor(
                        processController);

        executor.execute(
                project,
                actionItem(
                        ProjectAction.VIEW_LOGS));

        verify(processController)
                .showLogs(
                        project);
    }

    @Test
    void shouldExecuteBuildAction() {

        ProjectActionController projectActionController =
                mock(
                        ProjectActionController.class);

        SpringProject project =
                mock(SpringProject.class);

        ActionItem actionItem =
                actionItem(
                        ProjectAction.BUILD);

        ProjectActionExecutor executor =
                new ProjectActionExecutor(
                        mock(ProcessController.class),
                        projectActionController);

        executor.execute(
                project,
                actionItem);

        verify(projectActionController)
                .executeBlockingAction(
                        project,
                        actionItem);
    }

    @Test
    void shouldExecuteTestAction() {

        ProjectActionController projectActionController =
                mock(
                        ProjectActionController.class);

        SpringProject project =
                mock(SpringProject.class);

        ActionItem actionItem =
                actionItem(
                        ProjectAction.TEST);

        ProjectActionExecutor executor =
                new ProjectActionExecutor(
                        mock(ProcessController.class),
                        projectActionController);

        executor.execute(
                project,
                actionItem);

        verify(projectActionController)
                .executeBlockingAction(
                        project,
                        actionItem);
    }

    @Test
    void shouldAllowEnabledAction() {

        ProjectActionExecutor executor =
                createExecutor(
                        mock(
                                ProcessController.class));

        assertThat(
                executor.canExecute(
                        new ActionItem(
                                ProjectAction.RUN,
                                true)))
                .isTrue();
    }

    @Test
    void shouldRejectDisabledAction() {

        ProjectActionExecutor executor =
                createExecutor(
                        mock(
                                ProcessController.class));

        assertThat(
                executor.canExecute(
                        new ActionItem(
                                ProjectAction.RUN,
                                false)))
                .isFalse();
    }

    private ProjectActionExecutor createExecutor(
            ProcessController processController) {

        return new ProjectActionExecutor(
                processController,
                mock(
                        ProjectActionController.class));
    }

    private ActionItem actionItem(
            ProjectAction action) {

        return new ActionItem(
                action,
                true);
    }
}