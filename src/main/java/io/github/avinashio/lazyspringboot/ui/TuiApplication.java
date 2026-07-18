package io.github.avinashio.lazyspringboot.ui;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.action.ActionItem;
import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectActionOutput;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.controller.CreateProjectController;
import io.github.avinashio.lazyspringboot.ui.controller.ProcessController;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectActionController;
import io.github.avinashio.lazyspringboot.ui.controller.StartupController;
import io.github.avinashio.lazyspringboot.ui.input.InputDispatcher;
import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;
import io.github.avinashio.lazyspringboot.ui.input.KeyReader;
import io.github.avinashio.lazyspringboot.ui.input.KeyType;
import io.github.avinashio.lazyspringboot.ui.screen.*;
import io.github.avinashio.lazyspringboot.ui.state.InputMode;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@ConditionalOnProperty(
        name = "lazyspringboot.tui.enabled",
        havingValue = "true",
        matchIfMissing = true)
public class TuiApplication
        implements ApplicationRunner {

    private static final long
            UI_REFRESH_INTERVAL_MILLIS = 150;

    private final Terminal terminal;
    private final KeyReader keyReader;
    private final MainScreen mainScreen;
    private final UiState uiState;


    private final ConfirmationScreen
            confirmationScreen;


    private final ProjectActionsScreen
            projectActionsScreen;

    private final ProjectActionOutputScreen
            projectActionOutputScreen;

    private final GetProjectProcessUseCase
            getProjectProcessUseCase;

    private final ProcessController
            processController;

    private final ProjectActionController
            projectActionController;


    private final CreateProjectController
            createProjectController;

    private final CreateProjectScreen
            createProjectScreen;

    private final InputDispatcher inputDispatcher;

    private final StartupController startupController;


    public TuiApplication(
            Terminal terminal,
            KeyReader keyReader,
            MainScreen mainScreen,
            UiState uiState,
            ConfirmationScreen confirmationScreen,
            ProjectActionsScreen projectActionsScreen,
            ProjectActionOutputScreen
                    projectActionOutputScreen,
            ProcessController processController,
            ProjectActionController
                    projectActionController,
            CreateProjectScreen createProjectScreen,
            CreateProjectController
                    createProjectController,
            InputDispatcher inputDispatcher,
            GetProjectProcessUseCase
                    getProjectProcessUseCase,
            StartupController startupController) {

        this.terminal = terminal;
        this.keyReader = keyReader;
        this.mainScreen = mainScreen;
        this.uiState = uiState;

        this.confirmationScreen =
                confirmationScreen;

        this.projectActionsScreen =
                projectActionsScreen;
        this.projectActionOutputScreen =
                projectActionOutputScreen;

        this.getProjectProcessUseCase =
                getProjectProcessUseCase;

        this.processController =
                processController;
        this.projectActionController =
                projectActionController;

        this.createProjectScreen =
                createProjectScreen;
        this.createProjectController =
                createProjectController;

        this.inputDispatcher =
                inputDispatcher;

        this.startupController = startupController;
    }

    @Override
    public void run (
            ApplicationArguments args)
            throws Exception {

        var originalAttributes =
                terminal.getAttributes();

        try {
            terminal.enterRawMode();

            terminal.puts(
                    InfoCmp.Capability.enter_ca_mode);

            terminal.puts(
                    InfoCmp.Capability.cursor_invisible);

            terminal.flush();

            startupController.initialize();

            render();

            runEventLoop();
        } finally {
            terminal.puts(
                    InfoCmp.Capability.cursor_visible);

            terminal.puts(
                    InfoCmp.Capability.exit_ca_mode);

            terminal.setAttributes(
                    originalAttributes);

            terminal.flush();
        }
    }

    private void render() {
        if (uiState
                .dependencyConfirmationActive()) {
            confirmationScreen.render(uiState);
            return;
        }


        if (createProjectController
                .state()
                .active()) {

            createProjectScreen.render(
                    createProjectController.state());

            return;
        }

        if (uiState.projectActionOutputActive()) {

            SpringProject project =
                    uiState.selectedProject();

            if (project != null) {
                processController.refreshLogs(
                        project);
            }

            projectActionOutputScreen.render(
                    uiState);
            return;
        }

        if (uiState.projectActionsActive()) {
            projectActionsScreen.render(
                    uiState,
                    projectActionController.actions(
                            uiState.selectedProject()));
            return;
        }

        mainScreen.render(uiState);
    }

    private void runEventLoop()
            throws Exception {
        while (true) {
            KeyEvent keyEvent =
                    readNextKeyEvent();

            if (keyEvent.type()
                    == KeyType.TIMEOUT) {
                handleTimeout();
                continue;
            }

            if (shouldQuit(keyEvent)) {
                return;
            }

            handleKey(keyEvent);
            render();
        }
    }

    private KeyEvent readNextKeyEvent()
            throws IOException {

        if (shouldUseRefreshTimeout()) {
            return keyReader.read(
                    UI_REFRESH_INTERVAL_MILLIS);
        }

        return keyReader.read();
    }

    private boolean shouldUseRefreshTimeout() {

        return isLiveProcessOutputVisible()
                || isProjectStarting();
    }

    private boolean isProjectStarting() {

        SpringProject project =
                uiState.selectedProject();

        if (project == null) {
            return false;
        }

        return getProjectProcessUseCase
                .get(project)
                .map(ProjectProcess::status)
                .filter(status ->
                        status
                                == ProjectProcessStatus.STARTING)
                .isPresent();
    }

    private boolean isLiveProcessOutputVisible() {
        ProjectActionOutput output =
                uiState.projectActionOutput();

        return uiState.projectActionOutputActive()
                && output != null
                && output.action()
                == ProjectAction.VIEW_LOGS;
    }

    private void handleTimeout() {

        if (!shouldUseRefreshTimeout()) {
            return;
        }

        render();
    }

    private boolean shouldQuit(
            KeyEvent keyEvent) {
        return uiState.inputMode()
                == InputMode.NAVIGATION
                && !uiState.projectActionsActive()
                && !uiState.projectActionOutputActive()
                && keyEvent.type()
                == KeyType.QUIT;
    }

    private void handleKey(
            KeyEvent keyEvent) {

        inputDispatcher.handle(keyEvent);
    }

    private void refreshProcessOutput() {
        ProjectActionOutput output =
                uiState.projectActionOutput();

        SpringProject project =
                uiState.selectedProject();

        if (output == null
                || project == null
                || output.action()
                != ProjectAction.VIEW_LOGS) {
            return;
        }

        getProjectProcessUseCase
                .get(project)
                .ifPresent(
                        this::replaceProcessOutput);
    }

    private void replaceProcessOutput(
            ProjectProcess process) {
        int visibleHeight =
                projectActionOutputScreen
                        .visibleHeight();

        int previousContentSize =
                uiState
                        .projectActionOutput()
                        .lines()
                        .size();

        int maximumPreviousOffset =
                Math.max(
                        0,
                        previousContentSize
                                - visibleHeight);

        boolean followingOutput =
                uiState
                        .outputViewport()
                        .offset()
                        >= maximumPreviousOffset;

        int exitCode =
                process.exitCode() == null
                        ? -1
                        : process.exitCode();

        ProjectActionOutput output =
                new ProjectActionOutput(
                        process.projectName(),
                        ProjectAction.VIEW_LOGS,
                        exitCode,
                        process.output());

        uiState.replaceProjectActionOutput(
                output);

        if (followingOutput) {
            uiState
                    .outputViewport()
                    .moveToBottom(
                            output.lines().size(),
                            visibleHeight);
        }
    }

    private String buildProjectActionErrorMessage(
            ActionItem actionItem,
            IOException exception) {
        String actionName =
                actionItem
                        .action()
                        .displayName();

        String message =
                exception.getMessage();

        if (message == null
                || message.isBlank()) {
            return actionName + " failed";
        }

        return actionName
                + " failed: "
                + message;
    }

}