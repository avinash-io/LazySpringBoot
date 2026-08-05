package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.command.CommandPaletteController;
import io.github.avinashio.lazyspringboot.ui.dependency.DependencyNavigation;
import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;
import io.github.avinashio.lazyspringboot.ui.input.KeyType;
import io.github.avinashio.lazyspringboot.ui.project.ProjectNavigation;
import io.github.avinashio.lazyspringboot.ui.service.DependencyItemsService;
import io.github.avinashio.lazyspringboot.ui.service.DependencyUndoService;
import io.github.avinashio.lazyspringboot.ui.state.PanelFocus;
import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class NavigationControllerTest {

private UiState uiState;

private DependencyNavigation dependencyNavigation;

private ProjectNavigation projectNavigation;

private DependencyItemsService dependencyItemsService;

private ProjectActionController projectActionController;

private CreateProjectController createProjectController;

private CommandPaletteController commandPaletteController;

private DependencyUndoService dependencyUndoService;

private ProjectRefreshController projectRefreshController;

private WorkspaceController workspaceController;

private TextInputController textInputController;

private ProjectSortController projectSortController;

private ProcessController processController;

private ProjectManagerController projectManagerController;

private EnvironmentController environmentController;

private NavigationController controller;

@BeforeEach
void setUp() {
	
	uiState =
			mock(
					UiState.class);
	
	dependencyNavigation =
			mock(
					DependencyNavigation.class);
	
	projectNavigation =
			mock(
					ProjectNavigation.class);
	
	dependencyItemsService =
			mock(
					DependencyItemsService.class);
	
	projectActionController =
			mock(
					ProjectActionController.class);
	
	createProjectController =
			mock(
					CreateProjectController.class);
	
	commandPaletteController =
			mock(
					CommandPaletteController.class);
	
	dependencyUndoService =
			mock(
					DependencyUndoService.class);
	
	projectRefreshController =
			mock(
					ProjectRefreshController.class);
	
	workspaceController =
			mock(
					WorkspaceController.class);
	
	textInputController =
			mock(
					TextInputController.class);
	
	projectSortController =
			mock(
					ProjectSortController.class);
	
	processController =
			mock(
					ProcessController.class);
	
	projectManagerController =
			mock(
					ProjectManagerController.class);
	
	environmentController =
			mock(
					EnvironmentController.class);
	
	controller =
			new NavigationController(
					uiState,
					dependencyNavigation,
					projectNavigation,
					dependencyItemsService,
					projectActionController,
					createProjectController,
					dependencyUndoService,
					commandPaletteController,
					projectRefreshController,
					workspaceController,
					textInputController,
					projectSortController,
					processController,
					projectManagerController,
					environmentController);
}

@Test
void shouldClearMessageBeforeHandlingKey() {
	
	controller.handle(
			key(
					KeyType.LEFT));
	
	verify(uiState)
			.clearMessage();
}

@Test
void shouldOpenCommandPalette() {
	
	boolean handled =
			controller.handle(
					key(
							KeyType.COMMAND_PALETTE));
	
	assertThat(handled)
			.isTrue();
	
	verify(commandPaletteController)
			.open();
}

@Test
void shouldFocusPreviousPanel() {
	
	controller.handle(
			key(
					KeyType.LEFT));
	
	verify(uiState)
			.focusPreviousPanel();
}

@Test
void shouldFocusNextPanel() {
	
	controller.handle(
			key(
					KeyType.RIGHT));
	
	verify(uiState)
			.focusNextPanel();
}

@Test
void shouldUndoDependencyChange() {
	
	controller.handle(
			key(
					KeyType.UNDO));
	
	verify(dependencyUndoService)
			.undo();
}

@Test
void shouldOpenActionsForSelectedProject() {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	controller.handle(
			key(
					KeyType.ACTIONS));
	
	verify(projectActionController)
			.openActions(
					project);
}

@Test
void shouldMoveToPreviousProject() {
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.PROJECTS);
	
	when(uiState.selectedProjectIndex())
			.thenReturn(
					2,
					1);
	
	controller.handle(
			key(
					KeyType.UP));
	
	verify(projectNavigation)
			.selectPreviousVisible();
	
	verify(dependencyItemsService)
			.refresh();
}

@Test
void shouldNotRefreshDependenciesWhenProjectSelectionDoesNotChange() {
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.PROJECTS);
	
	when(uiState.selectedProjectIndex())
			.thenReturn(
					2);
	
	controller.handle(
			key(
					KeyType.UP));
	
	verify(projectNavigation)
			.selectPreviousVisible();
	
	verify(
			dependencyItemsService,
			never())
			.refresh();
}

@Test
void shouldMoveToNextProject() {
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.PROJECTS);
	
	when(uiState.selectedProjectIndex())
			.thenReturn(
					1,
					2);
	
	controller.handle(
			key(
					KeyType.DOWN));
	
	verify(projectNavigation)
			.selectNextVisible();
	
	verify(dependencyItemsService)
			.refresh();
}

@Test
void shouldMoveToPreviousDependency() {
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.DEPENDENCIES);
	
	controller.handle(
			key(
					KeyType.UP));
	
	verify(dependencyNavigation)
			.selectPreviousVisible();
}

@Test
void shouldMoveToNextDependency() {
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.DEPENDENCIES);
	
	controller.handle(
			key(
					KeyType.DOWN));
	
	verify(dependencyNavigation)
			.selectNextVisible();
}

@Test
void shouldToggleSelectedDependency() {
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.DEPENDENCIES);
	
	controller.handle(
			key(
					KeyType.SPACE));
	
	verify(uiState)
			.toggleSelectedDependency();
}

@Test
void shouldNotToggleDependencyFromProjectsPanel() {
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.PROJECTS);
	
	controller.handle(
			key(
					KeyType.SPACE));
	
	verify(
			uiState,
			never())
			.toggleSelectedDependency();
}

@Test
void shouldStartProjectSearch() {
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.PROJECTS);
	
	controller.handle(
			key(
					KeyType.SEARCH));
	
	verify(textInputController)
			.start(
					TextInputPurpose.PROJECT_SEARCH);
	
	verify(projectNavigation)
			.selectFirstVisible();
}

@Test
void shouldStartDependencySearch() {
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.DEPENDENCIES);
	
	controller.handle(
			key(
					KeyType.SEARCH));
	
	verify(textInputController)
			.start(
					TextInputPurpose.DEPENDENCY_SEARCH);
	
	verify(dependencyNavigation)
			.selectFirstVisible();
}

@Test
void shouldStartSelectedProject() {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.PROJECTS);
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	controller.handle(
			key(
					KeyType.ENTER));
	
	verify(processController)
			.start(
					project);
}

@Test
void shouldNotStartProjectWithoutSelection() {
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.PROJECTS);
	
	when(uiState.selectedProject())
			.thenReturn(
					null);
	
	controller.handle(
			key(
					KeyType.ENTER));
	
	verify(
			processController,
			never())
			.start(
					org.mockito.ArgumentMatchers.any());
}

@Test
void shouldStartDependencyConfirmation() {
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.DEPENDENCIES);
	
	controller.handle(
			key(
					KeyType.ENTER));
	
	verify(uiState)
			.startDependencyConfirmation();
}

@Test
void shouldOpenCreateProjectWithN() {
	
	controller.handle(
			character(
					'n'));
	
	verify(createProjectController)
			.open();
}

@Test
void shouldOpenEnvironmentWithE() {
	
	controller.handle(
			character(
					'e'));
	
	verify(environmentController)
			.open();
}

@Test
void shouldOpenWorkspaceWithW() {
	
	controller.handle(
			character(
					'w'));
	
	verify(workspaceController)
			.open();
}

@Test
void shouldOpenProjectManagerWithP() {
	
	controller.handle(
			character(
					'p'));
	
	verify(projectManagerController)
			.open();
}

@Test
void shouldOpenActionsWithA() {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	controller.handle(
			character(
					'a'));
	
	verify(projectActionController)
			.openActions(
					project);
}

@Test
void shouldUndoWithU() {
	
	controller.handle(
			character(
					'u'));
	
	verify(dependencyUndoService)
			.undo();
}

@Test
void shouldStopSelectedProjectWithX() {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.PROJECTS);
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	controller.handle(
			character(
					'x'));
	
	verify(processController)
			.stop(
					project);
}

@Test
void shouldRestartSelectedProjectWithUppercaseR() {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.PROJECTS);
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	controller.handle(
			character(
					'R'));
	
	verify(processController)
			.restart(
					project);
}

@Test
void shouldShowSelectedProjectLogsWithL() {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.PROJECTS);
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	controller.handle(
			character(
					'l'));
	
	verify(processController)
			.showLogs(
					project);
}

@Test
void shouldNotRunProjectLifecycleShortcutFromDependenciesPanel() {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.DEPENDENCIES);
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	controller.handle(
			character(
					'x'));
	
	verify(
			processController,
			never())
			.stop(
					project);
}

@Test
void shouldIgnoreCharacterEventWithoutCharacter() {
	
	KeyEvent event =
			mock(
					KeyEvent.class);
	
	when(event.type())
			.thenReturn(
					KeyType.CHARACTER);
	
	when(event.hasCharacter())
			.thenReturn(
					false);
	
	boolean handled =
			controller.handle(
					event);
	
	assertThat(handled)
			.isTrue();
	
	verify(createProjectController, never())
			.open();
	
	verify(environmentController, never())
			.open();
	
	verify(workspaceController, never())
			.open();
}

@Test
void shouldIgnoreUnknownCharacter() {
	
	boolean handled =
			controller.handle(
					character(
							'z'));
	
	assertThat(handled)
			.isTrue();
}

private KeyEvent key(
		KeyType type) {
	
	return new KeyEvent(
			type,
			null);
}

private KeyEvent character(
		char character) {
	
	return new KeyEvent(
			KeyType.CHARACTER,
			character);
}

@Test
void shouldRefreshWorkspace() throws Exception {
	
	when(uiState.projects())
			.thenReturn(
					java.util.List.of(
							mock(
									SpringProject.class)));
	
	controller.handle(
			character(
					'r'));
	
	verify(projectRefreshController)
			.refresh();
	
	verify(dependencyItemsService)
			.refresh();
	
	verify(uiState)
			.showScreen(
					io.github.avinashio.lazyspringboot.ui.state.Screen.DASHBOARD);
	
	verify(uiState)
			.showSuccessMessage(
					"Workspace refreshed");
}

@Test
void shouldShowWelcomeScreenAfterRefreshingEmptyWorkspace()
		throws Exception {
	
	when(uiState.projects())
			.thenReturn(
					java.util.List.of());
	
	controller.handle(
			character(
					'r'));
	
	verify(projectRefreshController)
			.refresh();
	
	verify(dependencyItemsService)
			.refresh();
	
	verify(uiState)
			.showScreen(
					io.github.avinashio.lazyspringboot.ui.state.Screen.WORKSPACE_WELCOME);
	
	verify(uiState)
			.showSuccessMessage(
					"Workspace refreshed");
}

@Test
void shouldShowErrorWhenWorkspaceRefreshFails()
		throws Exception {
	
	org.mockito.Mockito.doThrow(
					new java.io.IOException(
							"Unable to scan workspace"))
			.when(projectRefreshController)
			.refresh();
	
	controller.handle(
			character(
					'r'));
	
	verify(uiState)
			.showErrorMessage(
					"Failed to refresh workspace: "
							+ "Unable to scan workspace");
	
	verify(
			dependencyItemsService,
			never())
			.refresh();
	
	verify(
			uiState,
			never())
			.showSuccessMessage(
					org.mockito.ArgumentMatchers.anyString());
}

@Test
void shouldCycleProjectSort() {
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.PROJECTS);
	
	when(uiState.selectedProjectIndex())
			.thenReturn(
					2);
	
	controller.handle(
			character(
					's'));
	
	verify(projectSortController)
			.cycle();
	
	verify(projectNavigation)
			.selectFirstVisible();
}

@Test
void shouldRefreshDependenciesWhenSortingChangesSelection() {
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.PROJECTS);
	
	when(uiState.selectedProjectIndex())
			.thenReturn(
					2,
					0);
	
	controller.handle(
			character(
					's'));
	
	verify(projectSortController)
			.cycle();
	
	verify(projectNavigation)
			.selectFirstVisible();
	
	verify(dependencyItemsService)
			.refresh();
}

@Test
void shouldNotRefreshDependenciesWhenSortingPreservesSelection() {
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.PROJECTS);
	
	when(uiState.selectedProjectIndex())
			.thenReturn(
					0);
	
	controller.handle(
			character(
					's'));
	
	verify(projectSortController)
			.cycle();
	
	verify(projectNavigation)
			.selectFirstVisible();
	
	verify(
			dependencyItemsService,
			never())
			.refresh();
}

@Test
void shouldNotSortOutsideProjectsPanel() {
	
	when(uiState.panelFocus())
			.thenReturn(
					PanelFocus.DEPENDENCIES);
	
	controller.handle(
			character(
					's'));
	
	verify(
			projectSortController,
			never())
			.cycle();
	
	verify(
			projectNavigation,
			never())
			.selectFirstVisible();
}
}