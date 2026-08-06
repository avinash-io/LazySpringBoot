package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.service.DesktopIntegrationService;
import io.github.avinashio.lazyspringboot.ui.state.ProjectManagerState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectManagerController {

private final ProjectManagerState
		state;

private final UiState
		uiState;

private final DesktopIntegrationService
		desktopIntegrationService;

public ProjectManagerController(
		ProjectManagerState state,
		UiState uiState,
		DesktopIntegrationService desktopIntegrationService) {
	
	this.state =
			state;
	
	this.uiState =
			uiState;
	
	this.desktopIntegrationService =
			desktopIntegrationService;
}

public void open() {
	
	List<SpringProject> projects =
			projects();
	
	SpringProject dashboardSelection =
			uiState.selectedProject();
	
	int initialIndex =
			dashboardSelection == null
					? 0
					: projects.indexOf(
					dashboardSelection);
	
	state.open(
			Math.max(
					0,
					initialIndex));
}

public void close() {
	
	state.close();
}

public boolean isOpen() {
	
	return state.isOpen();
}

public List<SpringProject> projects() {
	
	return uiState.projects();
}

public SpringProject selectedProject() {
	
	List<SpringProject> projects =
			projects();
	
	if (projects.isEmpty()) {
		return null;
	}
	
	int index =
			Math.min(
					state.selectedIndex(),
					projects.size() - 1);
	
	return projects.get(
			index);
}

public int selectedIndex() {
	
	return state.selectedIndex();
}

public void selectPrevious() {
	
	state.selectPrevious(
			projects().size());
}

public void selectNext() {
	
	state.selectNext(
			projects().size());
}

public void copyProjectPath() {
	
	SpringProject project =
			selectedProject();
	
	if (project == null) {
		
		uiState.showErrorMessage(
				"No project selected.");
		
		return;
	}
	
	if (desktopIntegrationService.copyToClipboard(
			project.path().toString())) {
		
		uiState.showSuccessMessage(
				"Copied path for "
						+ project.name());
		
	} else {
		
		uiState.showErrorMessage(
				"Unable to copy project path.");
	}
}

public void openProjectFolder() {
	
	SpringProject project =
			selectedProject();
	
	if (project == null) {
		
		uiState.showErrorMessage(
				"No project selected.");
		
		return;
	}
	
	if (desktopIntegrationService.openFolder(
			project.path())) {
		
		uiState.showSuccessMessage(
				"Opened "
						+ project.name());
		
	} else {
		
		uiState.showErrorMessage(
				"Unable to open "
						+ project.name());
	}
}

public void openIntelliJ() {
	
	SpringProject project =
			selectedProject();
	
	if (project == null) {
		
		uiState.showErrorMessage(
				"No project selected.");
		
		return;
	}
	
	if (desktopIntegrationService.openIntelliJ(
			project.path())) {
		
		uiState.showSuccessMessage(
				"Opened "
						+ project.name()
						+ " in IntelliJ.");
		
	} else {
		
		uiState.showErrorMessage(
				"Unable to open IntelliJ.");
	}
}

public void openVSCode() {
	
	SpringProject project =
			selectedProject();
	
	if (project == null) {
		
		uiState.showErrorMessage(
				"No project selected.");
		
		return;
	}
	
	if (desktopIntegrationService.openVSCode(
			project.path())) {
		
		uiState.showSuccessMessage(
				"Opened "
						+ project.name()
						+ " in VS Code.");
		
	} else {
		
		uiState.showErrorMessage(
				"Unable to open VS Code.");
	}
}

public boolean selectCurrentProject() {
	
	if (projects().isEmpty()) {
		return false;
	}
	
	uiState.selectProject(
			state.selectedIndex());
	
	return true;
}
}