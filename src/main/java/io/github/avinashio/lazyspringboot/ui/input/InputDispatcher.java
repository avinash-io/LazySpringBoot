package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.ui.command.CommandPaletteController;
import io.github.avinashio.lazyspringboot.ui.controller.NavigationController;
import org.springframework.stereotype.Component;

@Component
public class InputDispatcher {

    private final DependencyConfirmationInputHandler
            dependencyConfirmationInputHandler;

    private final CreateProjectInputHandler
            createProjectInputHandler;

    private final ProjectActionOutputInputHandler
            projectActionOutputInputHandler;

    private final ProjectActionsInputHandler
            projectActionsInputHandler;

    private final DependencySearchInputHandler
            dependencySearchInputHandler;

    private final NavigationController
            navigationController;

    private final CommandPaletteController
            commandPaletteController;

    public InputDispatcher(
            DependencyConfirmationInputHandler
                    dependencyConfirmationInputHandler,
            CreateProjectInputHandler
                    createProjectInputHandler,
            ProjectActionOutputInputHandler
                    projectActionOutputInputHandler,
            ProjectActionsInputHandler
                    projectActionsInputHandler,
            DependencySearchInputHandler
                    dependencySearchInputHandler,
            CommandPaletteController commandPaletteController,
            NavigationController
                    navigationController) {

        this.dependencyConfirmationInputHandler =
                dependencyConfirmationInputHandler;

        this.createProjectInputHandler =
                createProjectInputHandler;

        this.projectActionOutputInputHandler =
                projectActionOutputInputHandler;

        this.projectActionsInputHandler =
                projectActionsInputHandler;

        this.dependencySearchInputHandler =
                dependencySearchInputHandler;

        this.navigationController =
                navigationController;

        this.commandPaletteController =
                commandPaletteController;
    }

    public void handle(
            KeyEvent keyEvent) {


        if (commandPaletteController.active()) {

            commandPaletteController.handleKey(
                    keyEvent);

            return;
        }


        if (dependencyConfirmationInputHandler.handle(
                keyEvent)) {
            return;
        }

        if (createProjectInputHandler.handle(
                keyEvent)) {
            return;
        }

        if (projectActionOutputInputHandler.handle(
                keyEvent)) {
            return;
        }

        if (projectActionsInputHandler.handle(
                keyEvent)) {
            return;
        }

        if (dependencySearchInputHandler.handle(
                keyEvent)) {
            return;
        }

        navigationController.handle(
                keyEvent);
    }
}