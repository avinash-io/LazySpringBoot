package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.StartProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.StopProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectActionOutput;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.screen.ProjectActionOutputScreen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.IOException;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ProcessController {

    private final UiState uiState;

    private final StartProjectProcessUseCase
            startProjectProcessUseCase;

    private final StopProjectProcessUseCase
            stopProjectProcessUseCase;

    private final GetProjectProcessUseCase
            getProjectProcessUseCase;

    private final ProjectActionOutputScreen
            projectActionOutputScreen;

    public ProcessController(
            UiState uiState,
            StartProjectProcessUseCase startProjectProcessUseCase,
            StopProjectProcessUseCase stopProjectProcessUseCase,
            GetProjectProcessUseCase getProjectProcessUseCase,
            ProjectActionOutputScreen projectActionOutputScreen) {

        this.uiState = uiState;
        this.startProjectProcessUseCase =
                startProjectProcessUseCase;
        this.stopProjectProcessUseCase =
                stopProjectProcessUseCase;
        this.getProjectProcessUseCase =
                getProjectProcessUseCase;
        this.projectActionOutputScreen =
                projectActionOutputScreen;
    }

    public void start(
            SpringProject project) {

        try {

            startProjectProcessUseCase.start(
                    project);

            uiState.stopProjectActions();

            uiState.showSuccessMessage(
                    "Started " + project.name());

        } catch (IOException exception) {

            uiState.stopProjectActions();

            uiState.showErrorMessage(
                    buildProcessErrorMessage(
                            "Failed to start " + project.name(),
                            exception));
        }
    }

    public void stop(
            SpringProject project) {

        boolean stopped =
                stopProjectProcessUseCase.stop(
                        project);

        uiState.stopProjectActions();

        if (stopped) {

            uiState.showSuccessMessage(
                    "Stopping " + project.name());

            return;
        }

        uiState.showErrorMessage(
                "Project is not running: "
                        + project.name());
    }

    public void showLogs(
            SpringProject project) {

        Optional<ProjectProcess> process =
                getProjectProcessUseCase.get(
                        project);

        if (process.isEmpty()) {

            uiState.stopProjectActions();

            uiState.showErrorMessage(
                    "No process found for "
                            + project.name());

            return;
        }

        uiState.stopProjectActions();

        showProcessOutput(
                process.get());
    }

    public void refreshLogs(
            SpringProject project) {

        ProjectActionOutput output =
                uiState.projectActionOutput();

        if (output == null
                || output.action()
                != ProjectAction.VIEW_LOGS) {
            return;
        }

        getProjectProcessUseCase
                .get(project)
                .ifPresent(
                        this::replaceProcessOutput);
    }

    private void showProcessOutput(
            ProjectProcess process) {

        int exitCode =
                process.exitCode() == null
                        ? -1
                        : process.exitCode();

        uiState.showProjectActionOutput(
                new ProjectActionOutput(
                        process.projectName(),
                        ProjectAction.VIEW_LOGS,
                        exitCode,
                        process.output()),
                projectActionOutputScreen
                        .visibleHeight());
    }

    private void replaceProcessOutput(
            ProjectProcess process) {

        int visibleHeight =
                projectActionOutputScreen
                        .visibleHeight();

        int previousSize =
                uiState.projectActionOutput()
                        .lines()
                        .size();

        boolean followTail =
                uiState.outputViewport().offset()
                        >= Math.max(
                        0,
                        previousSize
                                - visibleHeight);

        ProjectActionOutput output =
                new ProjectActionOutput(
                        process.projectName(),
                        ProjectAction.VIEW_LOGS,
                        process.exitCode() == null
                                ? -1
                                : process.exitCode(),
                        process.output());

        uiState.replaceProjectActionOutput(
                output);

        if (followTail) {

            uiState.outputViewport()
                    .moveToBottom(
                            output.lines().size(),
                            visibleHeight);
        }
    }

    private String buildProcessErrorMessage(
            String prefix,
            IOException exception) {

        String message =
                exception.getMessage();

        if (message == null
                || message.isBlank()) {
            return prefix;
        }

        return prefix + ": " + message;
    }
}