package io.github.avinashio.lazyspringboot.ui;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.action.ActionItem;
import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectActionOutput;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.command.CommandPaletteController;
import io.github.avinashio.lazyspringboot.ui.controller.*;
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

    private static final long
            PROJECT_REFRESH_INTERVAL_MILLIS = 2_000;

    private boolean quitConfirmationPending;

    private int quitActiveProjectCount;



    private final ProjectRefreshController
            projectRefreshController;

    private long lastProjectRefreshTime;

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

    private final CommandPaletteController
            commandPaletteController;

    private final CommandPaletteScreen
            commandPaletteScreen;

    private final WorkspaceController
            workspaceController;

    private final WorkspaceScreen
            workspaceScreen;


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
            ProjectRefreshController projectRefreshController,
            CommandPaletteController commandPaletteController,
            CommandPaletteScreen commandPaletteScreen,
            StartupController startupController,
            WorkspaceController workspaceController,
            WorkspaceScreen workspaceScreen) {

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

        this.commandPaletteController =
                commandPaletteController;

        this.commandPaletteScreen =
                commandPaletteScreen;

        this.projectRefreshController =
                projectRefreshController;

        this.workspaceController = workspaceController;
        this.workspaceScreen =
                workspaceScreen;
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

        if (commandPaletteController.active()) {

            mainScreen.render(uiState);

            commandPaletteScreen.render(
                    commandPaletteController.commands(),
                    commandPaletteController
                            .state()
                            .selectedCommandIndex(),
                    commandPaletteController
                            .searchQuery());

            return;
        }

        if (uiState
                .dependencyConfirmationActive()) {
            confirmationScreen.render(uiState);
            return;
        }


        if (createProjectController
                .state()
                .active()) {

            mainScreen.render(uiState);

            createProjectScreen.render(
                    createProjectController.state());

            return;
        }

        if (workspaceController.isOpen()) {

            mainScreen.render(uiState);

            workspaceScreen.render();

            return;
        }

        if (uiState.projectActionOutputActive()) {

            SpringProject project =
                    uiState.selectedProject();

            if (project != null) {
                processController.refreshLogs(
                        project);
            }

            mainScreen.render(uiState);

            projectActionOutputScreen.render(
                    uiState);

            return;
        }

        if (uiState.projectActionsActive()) {

            mainScreen.render(uiState);

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

            if (quitConfirmationPending) {

                if (handleQuitConfirmation(
                        keyEvent)) {

                    return;
                }

                render();

                continue;
            }

            if (isQuitKey(
                    keyEvent)) {

                int activeProjectCount =
                        activeProjectCount();

                if (activeProjectCount == 0) {
                    return;
                }

                quitActiveProjectCount =
                        activeProjectCount;

                quitConfirmationPending =
                        true;

                uiState.showWarningMessage(
                        buildQuitWarningMessage(
                                activeProjectCount));

                render();

                continue;
            }

            handleKey(keyEvent);

            render();
        }
    }

    private boolean handleQuitConfirmation(
            KeyEvent keyEvent) {

        if (keyEvent.type()
                == KeyType.ESCAPE) {

            cancelQuitConfirmation();

            return false;
        }

        if (keyEvent.type()
                == KeyType.CHARACTER
                && keyEvent.hasCharacter()) {

            char character =
                    keyEvent.character();

            if (character == 'q'
                    || character == 'y') {

                return true;
            }

            if (character == 'n') {

                cancelQuitConfirmation();

                return false;
            }
        }

        return false;
    }

    private void cancelQuitConfirmation() {

        quitConfirmationPending =
                false;

        quitActiveProjectCount = 0;

        uiState.clearMessage();
    }

    private KeyEvent readNextKeyEvent()
            throws IOException {

        if (shouldUseRefreshTimeout()) {
            return keyReader.read(
                    UI_REFRESH_INTERVAL_MILLIS);
        }

        return keyReader.read(
                PROJECT_REFRESH_INTERVAL_MILLIS);
    }

    private boolean shouldUseRefreshTimeout() {

        return isLiveProcessOutputVisible()
                || isSelectedProjectActive();
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

    private boolean isSelectedProjectActive() {

        SpringProject project =
                uiState.selectedProject();

        if (project == null) {
            return false;
        }

        return getProjectProcessUseCase
                .get(project)
                .map(ProjectProcess::running)
                .orElse(false);
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

        refreshProjectsIfNeeded();

        render();
    }

    private void refreshProjectsIfNeeded() {

        if (!isMainScreenActive()) {
            return;
        }

        long currentTime =
                System.currentTimeMillis();

        if (currentTime
                - lastProjectRefreshTime
                < PROJECT_REFRESH_INTERVAL_MILLIS) {
            return;
        }

        try {

            projectRefreshController.refresh();

            lastProjectRefreshTime =
                    currentTime;

        } catch (IOException exception) {

            uiState.showErrorMessage(
                    "Failed to refresh projects: "
                            + exception.getMessage());

            lastProjectRefreshTime =
                    currentTime;
        }
    }

    private boolean isMainScreenActive() {

        return !commandPaletteController.active()
                && !createProjectController
                .state()
                .active()
                && !workspaceController.isOpen()
                && !uiState
                .dependencyConfirmationActive()
                && !uiState
                .projectActionsActive()
                && !uiState
                .projectActionOutputActive();
    }


    private boolean isQuitKey(
            KeyEvent keyEvent) {

        if (createProjectController
                .state()
                .active()) {

            return false;
        }

        return uiState.inputMode()
                == InputMode.NAVIGATION
                && !uiState.projectActionsActive()
                && !uiState.projectActionOutputActive()
                && keyEvent.type()
                == KeyType.CHARACTER
                && keyEvent.hasCharacter()
                && keyEvent.character() == 'q';
    }

    private void handleQuitCancellation(
            KeyEvent keyEvent) {

        if (!quitConfirmationPending) {
            return;
        }

        if (keyEvent.type()
                == KeyType.ESCAPE) {

            quitConfirmationPending = false;

            uiState.clearMessage();
        }
    }

    private int activeProjectCount() {

        int activeProjectCount = 0;

        for (SpringProject project :
                uiState.projects()) {

            boolean active =
                    getProjectProcessUseCase
                            .get(project)
                            .map(ProjectProcess::running)
                            .orElse(false);

            if (active) {
                activeProjectCount++;
            }
        }

        return activeProjectCount;
    }

    private String buildQuitWarningMessage(
            int activeProjectCount) {

        String projectLabel =
                activeProjectCount == 1
                        ? "project is"
                        : "projects are";

        return activeProjectCount
                + " "
                + projectLabel
                + " still running. "
                + "Press q again to quit anyway "
                + "or Esc to cancel.";
    }

    private void handleKey(
            KeyEvent keyEvent) {

        inputDispatcher.handle(keyEvent);
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