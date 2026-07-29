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

private final ProjectSearchInputHandler
		projectSearchInputHandler;

private final NavigationController
		navigationController;

private final CommandPaletteController
		commandPaletteController;

private final WorkspaceInputHandler
		workspaceInputHandler;

private final ProjectManagerInputHandler
		projectManagerInputHandler;

private final ProjectDetailsInputHandler
		projectDetailsInputHandler;

private final EnvironmentInputHandler
		environmentInputHandler;

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
		ProjectSearchInputHandler
				projectSearchInputHandler,
		CommandPaletteController commandPaletteController,
		NavigationController navigationController,
		WorkspaceInputHandler workspaceInputHandler, ProjectManagerInputHandler projectManagerInputHandler, ProjectDetailsInputHandler projectDetailsInputHandler, EnvironmentInputHandler environmentInputHandler) {
	
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
	
	this.projectSearchInputHandler =
			projectSearchInputHandler;
	
	this.navigationController =
			navigationController;
	
	this.commandPaletteController =
			commandPaletteController;
	
	this.workspaceInputHandler =
			workspaceInputHandler;
	this.projectManagerInputHandler = projectManagerInputHandler;
	this.projectDetailsInputHandler = projectDetailsInputHandler;
	this.environmentInputHandler = environmentInputHandler;
}

public void handle(
		KeyEvent keyEvent) {
	
	if (environmentInputHandler.handle(
			keyEvent)) {
		
		return;
	}
	
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
	
	if (workspaceInputHandler.handle(
			keyEvent)) {
		return;
	}
	
	if (projectManagerInputHandler.handle(
			keyEvent)) {
		
		return;
	}
	
	if (projectDetailsInputHandler.handle(
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
	
	if (projectSearchInputHandler.handle(
			keyEvent)) {
		return;
	}
	
	navigationController.handle(
			keyEvent);
}
}