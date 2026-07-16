package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.action.ExecuteProjectActionUseCase;
import io.github.avinashio.lazyspringboot.application.action.ProjectActionCatalog;
import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.action.ActionItem;
import io.github.avinashio.lazyspringboot.domain.action.CommandResult;
import io.github.avinashio.lazyspringboot.domain.action.ProjectActionOutput;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;
import io.github.avinashio.lazyspringboot.ui.input.KeyType;
import io.github.avinashio.lazyspringboot.ui.screen.ProjectActionOutputScreen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectActionController {

    private final UiState uiState;

    private final ExecuteProjectActionUseCase
            executeProjectActionUseCase;

    private final ProjectActionOutputScreen
            projectActionOutputScreen;

    private final ProjectActionCatalog
            projectActionCatalog;

    private final GetProjectProcessUseCase
            getProjectProcessUseCase;

    public ProjectActionController(
            UiState uiState,
            ExecuteProjectActionUseCase executeProjectActionUseCase,
            ProjectActionOutputScreen projectActionOutputScreen,
            ProjectActionCatalog projectActionCatalog,
            GetProjectProcessUseCase getProjectProcessUseCase) {

        this.uiState = uiState;
        this.executeProjectActionUseCase =
                executeProjectActionUseCase;
        this.projectActionOutputScreen =
                projectActionOutputScreen;
        this.projectActionCatalog =
                projectActionCatalog;
        this.getProjectProcessUseCase =
                getProjectProcessUseCase;
    }

    public void openActions(
            SpringProject project) {

        if (project == null) {

            uiState.showErrorMessage(
                    "No project selected");

            return;
        }

        uiState.startProjectActions();
    }

    public List<ActionItem> actions(
            SpringProject project) {

        if (project == null) {
            return List.of();
        }

        return projectActionCatalog.actions(
                getProjectProcessUseCase.get(
                        project));
    }

    public boolean handleKey(
            KeyEvent keyEvent,
            List<ActionItem> actions) {

        switch (keyEvent.type()) {

            case ESCAPE -> {
                uiState.stopProjectActions();
                return true;
            }

            case UP -> {
                uiState.selectPreviousProjectAction();
                return true;
            }

            case DOWN -> {
                uiState.selectNextProjectAction(
                        actions.size());
                return true;
            }

            default -> {
                return false;
            }
        }
    }

    public boolean executeBlockingAction(
            SpringProject project,
            ActionItem actionItem) {

        try {

            CommandResult result =
                    executeProjectActionUseCase.execute(
                            project,
                            actionItem.action());

            uiState.stopProjectActions();

            uiState.showProjectActionOutput(
                    new ProjectActionOutput(
                            project.name(),
                            actionItem.action(),
                            result.exitCode(),
                            result.output()),
                    projectActionOutputScreen
                            .visibleHeight());

            return true;

        } catch (IOException exception) {

            uiState.stopProjectActions();

            uiState.showErrorMessage(
                    buildErrorMessage(
                            actionItem,
                            exception));

            return true;

        } catch (InterruptedException exception) {

            Thread.currentThread().interrupt();

            uiState.stopProjectActions();

            uiState.showErrorMessage(
                    actionItem.action()
                            .displayName()
                            + " interrupted for "
                            + project.name());

            return true;
        }
    }

    private String buildErrorMessage(
            ActionItem actionItem,
            IOException exception) {

        String message =
                exception.getMessage();

        if (message == null
                || message.isBlank()) {
            return actionItem.action()
                    .displayName()
                    + " failed";
        }

        return actionItem.action()
                .displayName()
                + " failed: "
                + message;
    }
}