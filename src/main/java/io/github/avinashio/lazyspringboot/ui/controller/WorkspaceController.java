package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;
import io.github.avinashio.lazyspringboot.service.WorkspaceService;
import io.github.avinashio.lazyspringboot.ui.service.DesktopIntegrationService;
import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import io.github.avinashio.lazyspringboot.ui.state.WorkspaceState;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class WorkspaceController {

private final WorkspaceState workspaceState;

private final WorkspaceService workspaceService;

private final DesktopIntegrationService
		desktopIntegrationService;

private final UiState uiState;

private final TextInputController
		textInputController;

private final DiscoverProjectsUseCase
		discoverProjectsUseCase;

public WorkspaceController(
		WorkspaceState workspaceState,
		WorkspaceService workspaceService,
		DesktopIntegrationService
				desktopIntegrationService,
		UiState uiState,
		TextInputController textInputController,
		DiscoverProjectsUseCase
				discoverProjectsUseCase) {
	
	this.workspaceState =
			workspaceState;
	
	this.workspaceService =
			workspaceService;
	
	this.desktopIntegrationService =
			desktopIntegrationService;
	
	this.uiState =
			uiState;
	
	this.textInputController =
			textInputController;
	
	this.discoverProjectsUseCase =
			discoverProjectsUseCase;
}

public void open() {
	
	workspaceState.open();
	
	workspaceState.clearErrorMessage();
	
	workspaceState.setWorkspace(
			workspaceService
					.workspace()
					.toString());
}

public void close() {
	
	cancelWorkspaceChange();
	
	workspaceState.close();
}

public boolean isOpen() {
	
	return workspaceState.isOpen();
}

public Path workspace() {
	
	return workspaceService.workspace();
}

public void startWorkspaceChange() {
	
	workspaceState.clearErrorMessage();
	
	textInputController.start(
			TextInputPurpose.WORKSPACE_PATH);
}

public boolean changingWorkspace() {
	
	return textInputController.active(
			TextInputPurpose.WORKSPACE_PATH);
}

public String workspaceInput() {
	
	return textInputController.value();
}

public void appendWorkspaceCharacter(
		char character) {
	
	textInputController.append(
			character);
}

public void backspaceWorkspaceInput() {
	
	textInputController.backspace();
}

public void cancelWorkspaceChange() {
	
	if (!changingWorkspace()) {
		return;
	}
	
	textInputController.stop();
	
	workspaceState.clearErrorMessage();
}

public void submitWorkspaceChange() {
	
	if (!changingWorkspace()) {
		return;
	}
	
	String value =
			workspaceInput().trim();
	
	if (value.isBlank()) {
		
		workspaceState.showErrorMessage(
				"Workspace path cannot be empty.");
		
		return;
	}
	
	Path workspace =
			Path.of(value)
					.toAbsolutePath()
					.normalize();
	
	if (!Files.isDirectory(workspace)) {
		
		workspaceState.showErrorMessage(
				"Workspace directory does not exist.");
		
		return;
	}
	
	try {
		
		workspaceService.changeWorkspace(
				workspace);
		
		uiState.setProjects(
				discoverProjectsUseCase
						.discover());
		
		workspaceState.setWorkspace(
				workspace.toString());
		
		workspaceState.clearErrorMessage();
		
		textInputController.stop();
		
		uiState.showSuccessMessage(
				"Workspace changed.");
		
	} catch (IOException exception) {
		
		workspaceState.showErrorMessage(
				"Unable to change workspace.");
	}
}

public void copyWorkspacePath() {
	
	if (desktopIntegrationService.copyToClipboard(
			workspace().toString())) {
		
		uiState.showSuccessMessage(
				"Workspace path copied.");
		
	} else {
		
		uiState.showErrorMessage(
				"Unable to copy workspace path.");
	}
}

public void openWorkspace() {
	
	if (desktopIntegrationService.openFolder(
			workspace())) {
		
		uiState.showSuccessMessage(
				"Workspace opened.");
		
	} else {
		
		uiState.showErrorMessage(
				"Unable to open workspace.");
	}
}
}