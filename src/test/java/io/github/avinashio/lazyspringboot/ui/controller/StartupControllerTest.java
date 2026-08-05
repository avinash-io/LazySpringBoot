package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.initializr.GetInitializrConfigurationUseCase;
import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;
import io.github.avinashio.lazyspringboot.domain.initializr.InitializrConfiguration;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.service.WorkspaceService;
import io.github.avinashio.lazyspringboot.ui.service.DependencyItemsService;
import io.github.avinashio.lazyspringboot.ui.service.InstalledToolsService;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import io.github.avinashio.lazyspringboot.ui.state.Screen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

class StartupControllerTest {

private UiState uiState;

private DiscoverProjectsUseCase
		discoverProjectsUseCase;

private GetInitializrConfigurationUseCase
		getInitializrConfigurationUseCase;

private CreateProjectState
		createProjectState;

private DependencyItemsService
		dependencyItemsService;

private InstalledToolsService
		installedToolsService;

private WorkspaceService
		workspaceService;

private StartupController controller;

@BeforeEach
void setUp() {
	
	uiState =
			mock(
					UiState.class);
	
	discoverProjectsUseCase =
			mock(
					DiscoverProjectsUseCase.class);
	
	getInitializrConfigurationUseCase =
			mock(
					GetInitializrConfigurationUseCase.class);
	
	createProjectState =
			mock(
					CreateProjectState.class);
	
	dependencyItemsService =
			mock(
					DependencyItemsService.class);
	
	installedToolsService =
			mock(
					InstalledToolsService.class);
	
	workspaceService =
			mock(
					WorkspaceService.class);
	
	controller =
			new StartupController(
					uiState,
					discoverProjectsUseCase,
					getInitializrConfigurationUseCase,
					createProjectState,
					dependencyItemsService,
					installedToolsService,
					workspaceService);
}

@Test
void shouldInitializeProjectsAndInstalledTools()
		throws Exception {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	List<SpringProject> projects =
			List.of(
					project);
	
	InitializrConfiguration configuration =
			configuration();
	
	when(discoverProjectsUseCase.discover())
			.thenReturn(
					projects);
	
	when(uiState.projects())
			.thenReturn(
					projects);
	
	when(
			getInitializrConfigurationUseCase
					.getConfiguration())
			.thenReturn(
					configuration);
	
	controller.initialize();
	
	verify(uiState)
			.setProjects(
					projects);
	
	verify(installedToolsService)
			.refresh();
}

@Test
void shouldShowWorkspaceWelcomeWhenNoProjectsExist()
		throws Exception {
	
	InitializrConfiguration configuration =
			configuration();
	
	when(discoverProjectsUseCase.discover())
			.thenReturn(
					List.of());
	
	when(uiState.projects())
			.thenReturn(
					List.of());
	
	when(
			getInitializrConfigurationUseCase
					.getConfiguration())
			.thenReturn(
					configuration);
	
	controller.initialize();
	
	verify(uiState)
			.showScreen(
					Screen.WORKSPACE_WELCOME);
}

@Test
void shouldShowDashboardWhenProjectsExist()
		throws Exception {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	List<SpringProject> projects =
			List.of(
					project);
	
	InitializrConfiguration configuration =
			configuration();
	
	when(discoverProjectsUseCase.discover())
			.thenReturn(
					projects);
	
	when(uiState.projects())
			.thenReturn(
					projects);
	
	when(
			getInitializrConfigurationUseCase
					.getConfiguration())
			.thenReturn(
					configuration);
	
	controller.initialize();
	
	verify(uiState)
			.showScreen(
					Screen.DASHBOARD);
}

@Test
void shouldInitializeDependencyMetadata()
		throws Exception {
	
	InitializrConfiguration configuration =
			configuration();
	
	when(discoverProjectsUseCase.discover())
			.thenReturn(
					List.of());
	
	when(uiState.projects())
			.thenReturn(
					List.of());
	
	when(
			getInitializrConfigurationUseCase
					.getConfiguration())
			.thenReturn(
					configuration);
	
	controller.initialize();
	
	verify(dependencyItemsService)
			.initialize(
					configuration.dependencies());
	
	verify(dependencyItemsService)
			.refresh();
}

@Test
void shouldInitializeJavaVersions()
		throws Exception {
	
	InitializrConfiguration configuration =
			configuration();
	
	when(discoverProjectsUseCase.discover())
			.thenReturn(
					List.of());
	
	when(uiState.projects())
			.thenReturn(
					List.of());
	
	when(
			getInitializrConfigurationUseCase
					.getConfiguration())
			.thenReturn(
					configuration);
	
	controller.initialize();
	
	verify(createProjectState)
			.setAvailableJavaVersions(
					configuration.javaVersions());
	
	verify(createProjectState)
			.setJavaVersion(
					configuration.defaultJavaVersion());
}

@Test
void shouldInitializeSpringBootVersions()
		throws Exception {
	
	InitializrConfiguration configuration =
			configuration();
	
	when(discoverProjectsUseCase.discover())
			.thenReturn(
					List.of());
	
	when(uiState.projects())
			.thenReturn(
					List.of());
	
	when(
			getInitializrConfigurationUseCase
					.getConfiguration())
			.thenReturn(
					configuration);
	
	controller.initialize();
	
	verify(createProjectState)
			.setAvailableSpringBootVersions(
					configuration.springBootVersions());
	
	verify(createProjectState)
			.setSpringBootVersion(
					configuration.defaultSpringBootVersion());
}

@Test
void shouldRefreshDependenciesAfterConfigurationIsApplied()
		throws Exception {
	
	InitializrConfiguration configuration =
			configuration();
	
	when(discoverProjectsUseCase.discover())
			.thenReturn(
					List.of());
	
	when(uiState.projects())
			.thenReturn(
					List.of());
	
	when(
			getInitializrConfigurationUseCase
					.getConfiguration())
			.thenReturn(
					configuration);
	
	controller.initialize();
	
	InOrder order =
			inOrder(
					dependencyItemsService,
					createProjectState);
	
	order.verify(
					dependencyItemsService)
			.initialize(
					configuration.dependencies());
	
	order.verify(
					createProjectState)
			.setAvailableJavaVersions(
					configuration.javaVersions());
	
	order.verify(
					createProjectState)
			.setJavaVersion(
					configuration.defaultJavaVersion());
	
	order.verify(
					createProjectState)
			.setAvailableSpringBootVersions(
					configuration.springBootVersions());
	
	order.verify(
					createProjectState)
			.setSpringBootVersion(
					configuration.defaultSpringBootVersion());
	
	order.verify(
					dependencyItemsService)
			.refresh();
}

@Test
void shouldPropagateProjectDiscoveryFailure()
		throws Exception {
	
	IOException failure =
			new IOException(
					"Unable to discover projects");
	
	when(discoverProjectsUseCase.discover())
			.thenThrow(
					failure);
	
	org.assertj.core.api.Assertions
			.assertThatThrownBy(
					controller::initialize)
			.isSameAs(
					failure);
}

@Test
void shouldPropagateInitializrInterruption()
		throws Exception {
	
	when(discoverProjectsUseCase.discover())
			.thenReturn(
					List.of());
	
	when(uiState.projects())
			.thenReturn(
					List.of());
	
	InterruptedException failure =
			new InterruptedException(
					"Initializr request interrupted");
	
	when(
			getInitializrConfigurationUseCase
					.getConfiguration())
			.thenThrow(
					failure);
	
	org.assertj.core.api.Assertions
			.assertThatThrownBy(
					controller::initialize)
			.isSameAs(
					failure);
}

private InitializrConfiguration configuration() {
	
	return mock(
			InitializrConfiguration.class);
}
}