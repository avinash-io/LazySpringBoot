package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.domain.action.ActionItem;
import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import org.springframework.stereotype.Component;

@Component
public class ProjectActionExecutor {

    private final ProcessController processController;

    private final ProjectActionController
            projectActionController;

    public ProjectActionExecutor(
            ProcessController processController,
            ProjectActionController projectActionController) {

        this.processController =
                processController;
        this.projectActionController =
                projectActionController;
    }

    public void execute(
            SpringProject project,
            ActionItem actionItem) {

        ProjectAction action =
                actionItem.action();

        switch (action) {

            case BUILD, TEST ->
                    projectActionController
                            .executeBlockingAction(
                                    project,
                                    actionItem);

            case RUN ->
                    processController.start(
                            project);

            case STOP ->
                    processController.stop(
                            project);

            case RESTART ->
                    processController.restart(
                            project);

            case VIEW_LOGS ->
                    processController.showLogs(
                            project);
        }
    }

    public boolean canExecute(
            ActionItem actionItem) {

        return actionItem.enabled();
    }
}