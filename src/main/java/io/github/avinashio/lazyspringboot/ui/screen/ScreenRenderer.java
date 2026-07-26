package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.command.CommandPaletteController;
import io.github.avinashio.lazyspringboot.ui.controller.CreateProjectController;
import io.github.avinashio.lazyspringboot.ui.controller.ProcessController;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectActionController;
import io.github.avinashio.lazyspringboot.ui.controller.QuitController;
import io.github.avinashio.lazyspringboot.ui.controller.WorkspaceController;
import io.github.avinashio.lazyspringboot.ui.state.Screen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.springframework.stereotype.Component;

@Component
public class ScreenRenderer {

    private final DashboardScreen dashboardScreen;

    private final WorkspaceWelcomeScreen workspaceWelcomeScreen;

    private final ConfirmationScreen confirmationScreen;

    private final ProjectActionsScreen projectActionsScreen;

    private final ProjectActionOutputScreen projectActionOutputScreen;

    private final CreateProjectScreen createProjectScreen;

    private final CommandPaletteScreen commandPaletteScreen;

    private final WorkspaceScreen workspaceScreen;

    private final QuitConfirmationScreen quitConfirmationScreen;

    private final CommandPaletteController commandPaletteController;

    private final CreateProjectController createProjectController;

    private final WorkspaceController workspaceController;

    private final QuitController quitController;

    private final ProcessController processController;

    private final ProjectActionController projectActionController;

    public ScreenRenderer(
            DashboardScreen dashboardScreen,
            WorkspaceWelcomeScreen workspaceWelcomeScreen,
            ConfirmationScreen confirmationScreen,
            ProjectActionsScreen projectActionsScreen,
            ProjectActionOutputScreen projectActionOutputScreen,
            CreateProjectScreen createProjectScreen,
            CommandPaletteScreen commandPaletteScreen,
            WorkspaceScreen workspaceScreen,
            QuitConfirmationScreen quitConfirmationScreen,
            CommandPaletteController commandPaletteController,
            CreateProjectController createProjectController,
            WorkspaceController workspaceController,
            QuitController quitController,
            ProcessController processController,
            ProjectActionController projectActionController) {

        this.dashboardScreen =
                dashboardScreen;

        this.workspaceWelcomeScreen =
                workspaceWelcomeScreen;

        this.confirmationScreen =
                confirmationScreen;

        this.projectActionsScreen =
                projectActionsScreen;

        this.projectActionOutputScreen =
                projectActionOutputScreen;

        this.createProjectScreen =
                createProjectScreen;

        this.commandPaletteScreen =
                commandPaletteScreen;

        this.workspaceScreen =
                workspaceScreen;

        this.quitConfirmationScreen =
                quitConfirmationScreen;

        this.commandPaletteController =
                commandPaletteController;

        this.createProjectController =
                createProjectController;

        this.workspaceController =
                workspaceController;

        this.quitController =
                quitController;

        this.processController =
                processController;

        this.projectActionController =
                projectActionController;
    }

    public void render(
            UiState uiState) {

        if (commandPaletteController.active()) {

            renderBaseScreen(
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

            renderBaseScreen(
                    uiState);

            confirmationScreen.render(
                    uiState);

            return;
        }

        if (createProjectController
                .state()
                .active()) {

            renderBaseScreen(
                    uiState);

            createProjectScreen.render(
                    createProjectController.state());

            return;
        }

        if (workspaceController.isOpen()) {

            renderBaseScreen(
                    uiState);

            workspaceScreen.render();

            return;
        }

        if (uiState.projectActionOutputActive()) {

            renderBaseScreen(
                    uiState);

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

            renderBaseScreen(
                    uiState);

            projectActionsScreen.render(
                    uiState,
                    projectActionController.actions(
                            uiState.selectedProject()));

            return;
        }

        if (quitController.active()) {

            renderBaseScreen(
                    uiState);

            quitConfirmationScreen.render(
                    quitController.state());

            return;
        }

        renderBaseScreen(
                uiState);
    }

    private void renderBaseScreen(
            UiState uiState) {

        if (uiState.screen()
                == Screen.WORKSPACE_WELCOME) {

            workspaceWelcomeScreen.render(
                    uiState);

            return;
        }

        dashboardScreen.render(
                uiState);
    }
}