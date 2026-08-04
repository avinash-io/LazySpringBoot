package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.project.CreateSpringProjectUseCase;
import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;
import io.github.avinashio.lazyspringboot.domain.project.NewProjectRequest;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.service.WorkspaceService;
import io.github.avinashio.lazyspringboot.ui.service.DependencyItemsService;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import io.github.avinashio.lazyspringboot.ui.state.Screen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class CreateProjectController {

private final CreateProjectState
		createProjectState;

private final CreateProjectRequestMapper
		requestMapper;

private final CreateSpringProjectUseCase
		createSpringProjectUseCase;

private final UiState uiState;

private final CreateProjectValidator
		validator;

private final DiscoverProjectsUseCase
		discoverProjectsUseCase;

private final DependencyItemsService
		dependencyItemsService;

private final WorkspaceService workspaceService;

public CreateProjectController(
		CreateProjectState createProjectState,
		CreateProjectRequestMapper requestMapper,
		CreateSpringProjectUseCase createSpringProjectUseCase,
		DiscoverProjectsUseCase discoverProjectsUseCase,
		UiState uiState,
		CreateProjectValidator validator,
		DependencyItemsService dependencyItemsService,
		WorkspaceService workspaceService) {
	
	this.createProjectState =
			createProjectState;
	this.requestMapper =
			requestMapper;
	this.createSpringProjectUseCase =
			createSpringProjectUseCase;
	this.discoverProjectsUseCase =
			discoverProjectsUseCase;
	this.uiState =
			uiState;
	this.validator =
			validator;
	this.dependencyItemsService =
			dependencyItemsService;
	this.workspaceService =
			workspaceService;
}

public void open() {
	
	createProjectState.setDependencies(
			dependencyItemsService.catalog());
	
	createProjectState.open();
}

public void close() {
	createProjectState.close();
}

public CreateProjectState state() {
	return createProjectState;
}


public boolean generate(
		Path destination) {
	
	createProjectState.clearErrorMessage();
	
	List<String> errors =
			validator.validate(
					createProjectState);
	
	if (!errors.isEmpty()) {
		
		createProjectState.showErrorMessage(
				errors.getFirst());
		
		return false;
	}
	
	try {
		
		NewProjectRequest request =
				requestMapper.map(
						createProjectState);
		
		Path createdProject =
				createSpringProjectUseCase.create(
						request,
						destination);
		
		List<SpringProject> projects =
				discoverProjectsUseCase.discover();
		
		uiState.setProjects(
				projects);
		
		selectCreatedProject(
				projects,
				createdProject);
		
		uiState.showScreen(
				Screen.DASHBOARD);
		
		createProjectState.close();
		
		uiState.showSuccessMessage(
				"Project created successfully");
		
		return true;
		
	} catch (IOException
			 | InterruptedException exception) {
		
		if (exception
					instanceof InterruptedException) {
			Thread.currentThread().interrupt();
		}
		
		createProjectState.showErrorMessage(
				"Failed to create project: "
						+ exception.getMessage());
		
		return false;
	}
}

public boolean continueToDependencies() {
	
	createProjectState.clearErrorMessage();
	
	List<String> errors =
			validator.validate(
					createProjectState);
	
	if (!errors.isEmpty()) {
		
		createProjectState.showErrorMessage(
				errors.getFirst());
		
		return false;
	}
	
	createProjectState.showDependencyStage();
	
	return true;
}

private void selectCreatedProject(
		List<SpringProject> projects,
		Path destination) {
	
	for (int index = 0;
		 index < projects.size();
		 index++) {
		
		SpringProject project =
				projects.get(index);
		
		if (project.path()
					.equals(destination)) {
			
			uiState.selectProject(
					index);
			
			return;
		}
	}
	
	if (!projects.isEmpty()) {
		
		uiState.selectProject(
				0);
	}
}
}