package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.project.CreateSpringProjectUseCase;
import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.domain.project.NewProjectRequest;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.service.WorkspaceService;
import io.github.avinashio.lazyspringboot.ui.service.DependencyItemsService;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import io.github.avinashio.lazyspringboot.ui.state.Screen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CreateProjectControllerTest {

@AfterEach
void clearInterruptedFlag() {
	
	Thread.interrupted();
}

@Test
void shouldOpenWizard() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	DependencyItemsService dependencyItemsService =
			mock(
					DependencyItemsService.class);
	
	CreateProjectController controller =
			createController(
					state,
					dependencyItemsService);
	
	controller.open();
	
	assertThat(state.active())
			.isTrue();
}

@Test
void shouldLoadDependenciesWhenOpeningWizard() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	DependencyItemsService dependencyItemsService =
			mock(
					DependencyItemsService.class);
	
	List<SpringDependency> dependencies =
			List.of(
					new SpringDependency(
							"web",
							"Spring Web",
							"Build web applications",
							"Web"),
					new SpringDependency(
							"actuator",
							"Spring Boot Actuator",
							"Monitor and manage applications",
							"Ops"));
	
	when(dependencyItemsService.catalog())
			.thenReturn(
					dependencies);
	
	CreateProjectController controller =
			createController(
					state,
					dependencyItemsService);
	
	controller.open();
	
	assertThat(state.dependencies())
			.containsExactlyElementsOf(
					dependencies);
}

@Test
void shouldCloseWizard() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	DependencyItemsService dependencyItemsService =
			mock(
					DependencyItemsService.class);
	
	CreateProjectController controller =
			createController(
					state,
					dependencyItemsService);
	
	controller.open();
	
	controller.close();
	
	assertThat(state.active())
			.isFalse();
}

@Test
void shouldNotGenerateWhenValidationFails()
		throws Exception {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	CreateProjectRequestMapper requestMapper =
			mock(
					CreateProjectRequestMapper.class);
	
	CreateSpringProjectUseCase createUseCase =
			mock(
					CreateSpringProjectUseCase.class);
	
	DiscoverProjectsUseCase discoverUseCase =
			mock(
					DiscoverProjectsUseCase.class);
	
	UiState uiState =
			mock(
					UiState.class);
	
	CreateProjectValidator validator =
			mock(
					CreateProjectValidator.class);
	
	when(validator.validate(state))
			.thenReturn(
					List.of(
							"Artifact ID is required"));
	
	CreateProjectController controller =
			createController(
					state,
					requestMapper,
					createUseCase,
					discoverUseCase,
					uiState,
					validator);
	
	boolean generated =
			controller.generate(
					Path.of(
							"/workspace"));
	
	assertThat(generated)
			.isFalse();
	
	assertThat(state.active())
			.isTrue();
	
	assertThat(state.errorMessage())
			.isEqualTo(
					"Artifact ID is required");
	
	verify(
			requestMapper,
			never())
			.map(state);
	
	verify(
			createUseCase,
			never())
			.create(
					org.mockito.ArgumentMatchers.any(),
					org.mockito.ArgumentMatchers.any());
	
	verify(
			discoverUseCase,
			never())
			.discover();
	
	verify(
			uiState,
			never())
			.showScreen(
					Screen.DASHBOARD);
}

@Test
void shouldGenerateProjectAndReturnToDashboard()
		throws Exception {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	CreateProjectRequestMapper requestMapper =
			mock(
					CreateProjectRequestMapper.class);
	
	CreateSpringProjectUseCase createUseCase =
			mock(
					CreateSpringProjectUseCase.class);
	
	DiscoverProjectsUseCase discoverUseCase =
			mock(
					DiscoverProjectsUseCase.class);
	
	UiState uiState =
			mock(
					UiState.class);
	
	CreateProjectValidator validator =
			mock(
					CreateProjectValidator.class);
	
	Path workspace =
			Path.of(
					"/workspace");
	
	Path createdPath =
			workspace.resolve(
					"demo");
	
	NewProjectRequest request =
			mock(
					NewProjectRequest.class);
	
	SpringProject createdProject =
			project(
					createdPath);
	
	when(validator.validate(state))
			.thenReturn(
					List.of());
	
	when(requestMapper.map(state))
			.thenReturn(
					request);
	
	when(
			createUseCase.create(
					request,
					workspace))
			.thenReturn(
					createdPath);
	
	when(discoverUseCase.discover())
			.thenReturn(
					List.of(
							createdProject));
	
	CreateProjectController controller =
			createController(
					state,
					requestMapper,
					createUseCase,
					discoverUseCase,
					uiState,
					validator);
	
	boolean generated =
			controller.generate(
					workspace);
	
	assertThat(generated)
			.isTrue();
	
	assertThat(state.active())
			.isFalse();
	
	verify(createUseCase)
			.create(
					request,
					workspace);
	
	verify(discoverUseCase)
			.discover();
	
	verify(uiState)
			.setProjects(
					List.of(
							createdProject));
	
	verify(uiState)
			.selectProject(
					0);
	
	verify(uiState)
			.showScreen(
					Screen.DASHBOARD);
	
	verify(uiState)
			.showSuccessMessage(
					"Project created successfully");
}

@Test
void shouldSelectCreatedProjectUsingReturnedProjectPath()
		throws Exception {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	CreateProjectRequestMapper requestMapper =
			mock(
					CreateProjectRequestMapper.class);
	
	CreateSpringProjectUseCase createUseCase =
			mock(
					CreateSpringProjectUseCase.class);
	
	DiscoverProjectsUseCase discoverUseCase =
			mock(
					DiscoverProjectsUseCase.class);
	
	UiState uiState =
			mock(
					UiState.class);
	
	CreateProjectValidator validator =
			mock(
					CreateProjectValidator.class);
	
	Path workspace =
			Path.of(
					"/workspace");
	
	Path existingPath =
			workspace.resolve(
					"existing");
	
	Path createdPath =
			workspace.resolve(
					"demo");
	
	NewProjectRequest request =
			mock(
					NewProjectRequest.class);
	
	SpringProject existingProject =
			project(
					existingPath);
	
	SpringProject createdProject =
			project(
					createdPath);
	
	when(validator.validate(state))
			.thenReturn(
					List.of());
	
	when(requestMapper.map(state))
			.thenReturn(
					request);
	
	when(
			createUseCase.create(
					request,
					workspace))
			.thenReturn(
					createdPath);
	
	when(discoverUseCase.discover())
			.thenReturn(
					List.of(
							existingProject,
							createdProject));
	
	CreateProjectController controller =
			createController(
					state,
					requestMapper,
					createUseCase,
					discoverUseCase,
					uiState,
					validator);
	
	boolean generated =
			controller.generate(
					workspace);
	
	assertThat(generated)
			.isTrue();
	
	verify(uiState)
			.selectProject(
					1);
}

@Test
void shouldKeepWizardOpenWhenProjectCreationFails()
		throws Exception {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	CreateProjectRequestMapper requestMapper =
			mock(
					CreateProjectRequestMapper.class);
	
	CreateSpringProjectUseCase createUseCase =
			mock(
					CreateSpringProjectUseCase.class);
	
	DiscoverProjectsUseCase discoverUseCase =
			mock(
					DiscoverProjectsUseCase.class);
	
	UiState uiState =
			mock(
					UiState.class);
	
	CreateProjectValidator validator =
			mock(
					CreateProjectValidator.class);
	
	Path workspace =
			Path.of(
					"/workspace");
	
	NewProjectRequest request =
			mock(
					NewProjectRequest.class);
	
	when(validator.validate(state))
			.thenReturn(
					List.of());
	
	when(requestMapper.map(state))
			.thenReturn(
					request);
	
	when(
			createUseCase.create(
					request,
					workspace))
			.thenThrow(
					new IOException(
							"Spring Initializr rejected the project configuration"));
	
	CreateProjectController controller =
			createController(
					state,
					requestMapper,
					createUseCase,
					discoverUseCase,
					uiState,
					validator);
	
	boolean generated =
			controller.generate(
					workspace);
	
	assertThat(generated)
			.isFalse();
	
	assertThat(state.active())
			.isTrue();
	
	assertThat(state.errorMessage())
			.isEqualTo(
					"Failed to create project: "
							+ "Spring Initializr rejected the project configuration");
	
	verify(
			discoverUseCase,
			never())
			.discover();
	
	verify(
			uiState,
			never())
			.showScreen(
					Screen.DASHBOARD);
	
	verify(
			uiState,
			never())
			.showSuccessMessage(
					"Project created successfully");
}

@Test
void shouldRestoreInterruptFlagWhenGenerationIsInterrupted()
		throws Exception {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	CreateProjectRequestMapper requestMapper =
			mock(
					CreateProjectRequestMapper.class);
	
	CreateSpringProjectUseCase createUseCase =
			mock(
					CreateSpringProjectUseCase.class);
	
	DiscoverProjectsUseCase discoverUseCase =
			mock(
					DiscoverProjectsUseCase.class);
	
	UiState uiState =
			mock(
					UiState.class);
	
	CreateProjectValidator validator =
			mock(
					CreateProjectValidator.class);
	
	Path workspace =
			Path.of(
					"/workspace");
	
	NewProjectRequest request =
			mock(
					NewProjectRequest.class);
	
	when(validator.validate(state))
			.thenReturn(
					List.of());
	
	when(requestMapper.map(state))
			.thenReturn(
					request);
	
	when(
			createUseCase.create(
					request,
					workspace))
			.thenThrow(
					new InterruptedException(
							"Generation interrupted"));
	
	CreateProjectController controller =
			createController(
					state,
					requestMapper,
					createUseCase,
					discoverUseCase,
					uiState,
					validator);
	
	boolean generated =
			controller.generate(
					workspace);
	
	assertThat(generated)
			.isFalse();
	
	assertThat(state.active())
			.isTrue();
	
	assertThat(
			Thread.currentThread()
					.isInterrupted())
			.isTrue();
	
	assertThat(state.errorMessage())
			.isEqualTo(
					"Failed to create project: Generation interrupted");
	
	verify(
			discoverUseCase,
			never())
			.discover();
	
	verify(
			uiState,
			never())
			.showScreen(
					Screen.DASHBOARD);
}

@Test
void shouldFallBackToFirstProjectWhenCreatedProjectIsNotDiscovered()
		throws Exception {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	CreateProjectRequestMapper requestMapper =
			mock(
					CreateProjectRequestMapper.class);
	
	CreateSpringProjectUseCase createUseCase =
			mock(
					CreateSpringProjectUseCase.class);
	
	DiscoverProjectsUseCase discoverUseCase =
			mock(
					DiscoverProjectsUseCase.class);
	
	UiState uiState =
			mock(
					UiState.class);
	
	CreateProjectValidator validator =
			mock(
					CreateProjectValidator.class);
	
	Path workspace =
			Path.of(
					"/workspace");
	
	Path createdPath =
			workspace.resolve(
					"demo");
	
	SpringProject existingProject =
			project(
					workspace.resolve(
							"existing"));
	
	NewProjectRequest request =
			mock(
					NewProjectRequest.class);
	
	when(validator.validate(state))
			.thenReturn(
					List.of());
	
	when(requestMapper.map(state))
			.thenReturn(
					request);
	
	when(
			createUseCase.create(
					request,
					workspace))
			.thenReturn(
					createdPath);
	
	when(discoverUseCase.discover())
			.thenReturn(
					List.of(
							existingProject));
	
	CreateProjectController controller =
			createController(
					state,
					requestMapper,
					createUseCase,
					discoverUseCase,
					uiState,
					validator);
	
	boolean generated =
			controller.generate(
					workspace);
	
	assertThat(generated)
			.isTrue();
	
	verify(uiState)
			.selectProject(
					0);
}

private SpringProject project(
		Path path) {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	when(project.path())
			.thenReturn(
					path);
	
	return project;
}

private CreateProjectController createController(
		CreateProjectState state,
		DependencyItemsService dependencyItemsService) {
	
	return new CreateProjectController(
			state,
			mock(
					CreateProjectRequestMapper.class),
			mock(
					CreateSpringProjectUseCase.class),
			mock(
					DiscoverProjectsUseCase.class),
			mock(
					UiState.class),
			mock(
					CreateProjectValidator.class),
			dependencyItemsService,
			mock(
					WorkspaceService.class));
}

private CreateProjectController createController(
		CreateProjectState state,
		CreateProjectRequestMapper requestMapper,
		CreateSpringProjectUseCase createUseCase,
		DiscoverProjectsUseCase discoverUseCase,
		UiState uiState,
		CreateProjectValidator validator) {
	
	return new CreateProjectController(
			state,
			requestMapper,
			createUseCase,
			discoverUseCase,
			uiState,
			validator,
			mock(
					DependencyItemsService.class),
			mock(
					WorkspaceService.class));
}
}