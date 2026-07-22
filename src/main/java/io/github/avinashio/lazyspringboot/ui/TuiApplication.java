package io.github.avinashio.lazyspringboot.ui;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectActionOutput;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.command.CommandPaletteController;
import io.github.avinashio.lazyspringboot.ui.controller.CreateProjectController;
import io.github.avinashio.lazyspringboot.ui.controller.ProcessController;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectActionController;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectRefreshController;
import io.github.avinashio.lazyspringboot.ui.controller.QuitController;
import io.github.avinashio.lazyspringboot.ui.controller.QuitDecision;
import io.github.avinashio.lazyspringboot.ui.controller.StartupController;
import io.github.avinashio.lazyspringboot.ui.controller.WorkspaceController;
import io.github.avinashio.lazyspringboot.ui.input.*;
import io.github.avinashio.lazyspringboot.ui.screen.*;
import io.github.avinashio.lazyspringboot.ui.state.InputMode;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.IOException;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

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

    private final QuitController quitController;

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

    private final StartupController
            startupController;

    private final CommandPaletteController
            commandPaletteController;

    private final CommandPaletteScreen
            commandPaletteScreen;

    private final WorkspaceController
            workspaceController;

    private final WorkspaceScreen
            workspaceScreen;

    private final QuitConfirmationScreen
            quitConfirmationScreen;

    private final QuitInputHandler
            quitInputHandler;

    private final ConfigurableApplicationContext
            applicationContext;

    public TuiApplication(
            QuitController quitController,
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
            ProjectRefreshController
                    projectRefreshController,
            CommandPaletteController
                    commandPaletteController,
            CommandPaletteScreen commandPaletteScreen,
            StartupController startupController,
            WorkspaceController workspaceController,
            WorkspaceScreen workspaceScreen,
            QuitConfirmationScreen quitConfirmationScreen,
            QuitInputHandler quitInputHandler, ConfigurableApplicationContext applicationContext) {

        this.quitController =
                quitController;

        this.terminal =
                terminal;

        this.keyReader =
                keyReader;

        this.mainScreen =
                mainScreen;

        this.uiState =
                uiState;

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

        this.startupController =
                startupController;

        this.commandPaletteController =
                commandPaletteController;

        this.commandPaletteScreen =
                commandPaletteScreen;

        this.projectRefreshController =
                projectRefreshController;

        this.workspaceController =
                workspaceController;

        this.workspaceScreen =
                workspaceScreen;
        this.quitConfirmationScreen = quitConfirmationScreen;
        this.quitInputHandler = quitInputHandler;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(
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

            terminal.writer()
                    .println();

            terminal.writer()
                    .flush();
        }

        System.exit(
                SpringApplication.exit(
                        applicationContext));
    }

    private void render() {

        if (commandPaletteController.active()) {

            mainScreen.render(
                    uiState);

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

            confirmationScreen.render(
                    uiState);

            return;
        }

        if (createProjectController
                .state()
                .active()) {

            mainScreen.render(
                    uiState);

            createProjectScreen.render(
                    createProjectController.state());

            return;
        }

        if (workspaceController.isOpen()) {

            mainScreen.render(
                    uiState);

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

            mainScreen.render(
                    uiState);

            projectActionOutputScreen.render(
                    uiState);

            return;
        }

        if (uiState.projectActionsActive()) {

            mainScreen.render(
                    uiState);

            projectActionsScreen.render(
                    uiState,
                    projectActionController.actions(
                            uiState.selectedProject()));

            return;
        }

        if (quitController.active()) {

            mainScreen.render(
                    uiState);

            quitConfirmationScreen.render(
                    quitController.state());

            return;
        }

        mainScreen.render(
                uiState);
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

            if (quitController.active()) {

                QuitDecision decision =
                        quitInputHandler.handle(
                                keyEvent);

                if (decision
                        == QuitDecision.QUIT) {

                    return;
                }

                render();

                continue;
            }

            if (isQuitKey(
                    keyEvent)) {

                QuitDecision decision =
                        quitController.requestQuit();

                if (decision
                        == QuitDecision.QUIT) {

                    return;
                }

                render();

                continue;
            }

            handleKey(
                    keyEvent);

            render();
        }
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
                .filter(
                        status ->
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

    private void handleKey(
            KeyEvent keyEvent) {

        inputDispatcher.handle(
                keyEvent);
    }
}