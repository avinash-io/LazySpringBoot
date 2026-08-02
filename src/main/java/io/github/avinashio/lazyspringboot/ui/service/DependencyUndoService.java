package io.github.avinashio.lazyspringboot.ui.service;

import io.github.avinashio.lazyspringboot.application.dependency.UndoDependenciesUseCase;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectRefreshService;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DependencyUndoService {

private final UiState uiState;

private final UndoDependenciesUseCase
		undoDependenciesUseCase;

private final ProjectRefreshService
		projectRefreshService;

public DependencyUndoService(
		UiState uiState,
		UndoDependenciesUseCase undoDependenciesUseCase,
		ProjectRefreshService projectRefreshService) {
	
	this.uiState =
			uiState;
	
	this.undoDependenciesUseCase =
			undoDependenciesUseCase;
	
	this.projectRefreshService =
			projectRefreshService;
}

public void undo() {
	
	SpringProject project =
			uiState.selectedProject();
	
	if (project == null) {
		
		uiState.showErrorMessage(
				"No project selected");
		
		return;
	}
	
	if (!undoDependenciesUseCase.canUndo(
			project)) {
		
		uiState.showErrorMessage(
				"No dependency changes to undo");
		
		return;
	}
	
	try {
		
		undoDependenciesUseCase.undo(
				project);
		
		projectRefreshService
				.refreshEverything();
		
		uiState.clearDependencySelections();
		
		uiState.showSuccessMessage(
				"Restored build file for "
						+ project.name());
		
	} catch (IOException exception) {
		
		uiState.showErrorMessage(
				buildUndoErrorMessage(
						exception));
	}
}

private String buildUndoErrorMessage(
		IOException exception) {
	
	String message =
			exception.getMessage();
	
	if (message == null
				|| message.isBlank()) {
		
		return "Failed to restore build file";
	}
	
	return "Failed to restore build file: "
				   + message;
}
}