package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.action.ExecuteProjectActionUseCase;
import io.github.avinashio.lazyspringboot.application.action.ProjectActionCatalog;
import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.action.ActionItem;
import io.github.avinashio.lazyspringboot.domain.action.CommandResult;
import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectActionOutput;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;
import io.github.avinashio.lazyspringboot.ui.input.KeyType;
import io.github.avinashio.lazyspringboot.ui.screen.ProjectActionOutputScreen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProjectActionControllerTest {

private UiState uiState;

private ExecuteProjectActionUseCase
		executeProjectActionUseCase;

private ProjectActionOutputScreen
		projectActionOutputScreen;

private ProjectActionCatalog
		projectActionCatalog;

private GetProjectProcessUseCase
		getProjectProcessUseCase;

private ProjectActionController controller;

@BeforeEach
void setUp() {
	
	uiState =
			mock(
					UiState.class);
	
	executeProjectActionUseCase =
			mock(
					ExecuteProjectActionUseCase.class);
	
	projectActionOutputScreen =
			mock(
					ProjectActionOutputScreen.class);
	
	projectActionCatalog =
			mock(
					ProjectActionCatalog.class);
	
	getProjectProcessUseCase =
			mock(
					GetProjectProcessUseCase.class);
	
	controller =
			new ProjectActionController(
					uiState,
					executeProjectActionUseCase,
					projectActionOutputScreen,
					projectActionCatalog,
					getProjectProcessUseCase);
}

@AfterEach
void clearInterruptedFlag() {
	
	Thread.interrupted();
}

@Test
void shouldOpenProjectActions() {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	controller.openActions(
			project);
	
	verify(uiState)
			.startProjectActions();
	
	verify(
			uiState,
			never())
			.showErrorMessage(
					org.mockito.ArgumentMatchers
							.anyString());
}

@Test
void shouldShowErrorWhenOpeningActionsWithoutProject() {
	
	controller.openActions(
			null);
	
	verify(uiState)
			.showErrorMessage(
					"No project selected");
	
	verify(
			uiState,
			never())
			.startProjectActions();
}

@Test
void shouldReturnActionsForProject() {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	ProjectProcess process =
			mock(
					ProjectProcess.class);
	
	List<ActionItem> expected =
			actions();
	
	when(
			getProjectProcessUseCase.get(
					project))
			.thenReturn(
					Optional.of(
							process));
	
	when(
			projectActionCatalog.actions(
					Optional.of(
							process)))
			.thenReturn(
					expected);
	
	List<ActionItem> actual =
			controller.actions(
					project);
	
	assertThat(actual)
			.containsExactlyElementsOf(
					expected);
	
	verify(getProjectProcessUseCase)
			.get(
					project);
	
	verify(projectActionCatalog)
			.actions(
					Optional.of(
							process));
}

@Test
void shouldReturnActionsWhenProjectHasNoProcess() {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	List<ActionItem> expected =
			actions();
	
	when(
			getProjectProcessUseCase.get(
					project))
			.thenReturn(
					Optional.empty());
	
	when(
			projectActionCatalog.actions(
					Optional.empty()))
			.thenReturn(
					expected);
	
	List<ActionItem> actual =
			controller.actions(
					project);
	
	assertThat(actual)
			.containsExactlyElementsOf(
					expected);
	
	verify(projectActionCatalog)
			.actions(
					Optional.empty());
}

@Test
void shouldReturnNoActionsWithoutProject() {
	
	List<ActionItem> actual =
			controller.actions(
					null);
	
	assertThat(actual)
			.isEmpty();
	
	verifyNoInteractions(
			projectActionCatalog,
			getProjectProcessUseCase);
}

@Test
void shouldHandleEscape() {
	
	boolean handled =
			controller.handleKey(
					new KeyEvent(
							KeyType.ESCAPE,
							null),
					actions());
	
	assertThat(handled)
			.isTrue();
	
	verify(uiState)
			.stopProjectActions();
}

@Test
void shouldHandleUp() {
	
	boolean handled =
			controller.handleKey(
					new KeyEvent(
							KeyType.UP,
							null),
					actions());
	
	assertThat(handled)
			.isTrue();
	
	verify(uiState)
			.selectPreviousProjectAction();
}

@Test
void shouldHandleDown() {
	
	boolean handled =
			controller.handleKey(
					new KeyEvent(
							KeyType.DOWN,
							null),
					actions());
	
	assertThat(handled)
			.isTrue();
	
	verify(uiState)
			.selectNextProjectAction(
					2);
}

@Test
void shouldIgnoreUnhandledKey() {
	
	boolean handled =
			controller.handleKey(
					new KeyEvent(
							KeyType.ENTER,
							null),
					actions());
	
	assertThat(handled)
			.isFalse();
	
	verifyNoInteractions(
			uiState);
}

@Test
void shouldExecuteBlockingAction()
		throws Exception {
	
	SpringProject project =
			project(
					"test-app");
	
	ActionItem action =
			new ActionItem(
					ProjectAction.BUILD,
					true);
	
	CommandResult result =
			mock(
					CommandResult.class);
	
	when(result.exitCode())
			.thenReturn(
					0);
	
	when(result.output())
			.thenReturn(
					List.of(
							"Building project",
							"BUILD SUCCESS"));
	
	when(
			executeProjectActionUseCase.execute(
					project,
					ProjectAction.BUILD))
			.thenReturn(
					result);
	
	when(
			projectActionOutputScreen
					.visibleHeight())
			.thenReturn(
					20);
	
	boolean handled =
			controller.executeBlockingAction(
					project,
					action);
	
	assertThat(handled)
			.isTrue();
	
	verify(executeProjectActionUseCase)
			.execute(
					project,
					ProjectAction.BUILD);
	
	verify(uiState)
			.stopProjectActions();
	
	ArgumentCaptor<ProjectActionOutput> outputCaptor =
			ArgumentCaptor.forClass(
					ProjectActionOutput.class);
	
	verify(uiState)
			.showProjectActionOutput(
					outputCaptor.capture(),
					org.mockito.ArgumentMatchers
							.eq(20));
	
	ProjectActionOutput output =
			outputCaptor.getValue();
	
	assertThat(output.projectName())
			.isEqualTo(
					"test-app");
	
	assertThat(output.action())
			.isEqualTo(
					ProjectAction.BUILD);
	
	assertThat(output.exitCode())
			.isZero();
	
	assertThat(output.lines())
			.containsExactly(
					"Building project",
					"BUILD SUCCESS");
	
	verify(
			uiState,
			never())
			.showErrorMessage(
					org.mockito.ArgumentMatchers
							.anyString());
}

@Test
void shouldShowOutputEvenWhenCommandExitsWithFailure()
		throws Exception {
	
	SpringProject project =
			project(
					"test-app");
	
	ActionItem action =
			new ActionItem(
					ProjectAction.TEST,
					true);
	
	CommandResult result =
			mock(
					CommandResult.class);
	
	when(result.exitCode())
			.thenReturn(
					1);
	
	when(result.output())
			.thenReturn(
					List.of(
							"Tests run: 10",
							"Failures: 1"));
	
	when(
			executeProjectActionUseCase.execute(
					project,
					ProjectAction.TEST))
			.thenReturn(
					result);
	
	when(
			projectActionOutputScreen
					.visibleHeight())
			.thenReturn(
					15);
	
	boolean handled =
			controller.executeBlockingAction(
					project,
					action);
	
	assertThat(handled)
			.isTrue();
	
	ArgumentCaptor<ProjectActionOutput> outputCaptor =
			ArgumentCaptor.forClass(
					ProjectActionOutput.class);
	
	verify(uiState)
			.showProjectActionOutput(
					outputCaptor.capture(),
					org.mockito.ArgumentMatchers
							.eq(15));
	
	assertThat(
			outputCaptor.getValue()
					.exitCode())
			.isEqualTo(
					1);
	
	assertThat(
			outputCaptor.getValue()
					.lines())
			.containsExactly(
					"Tests run: 10",
					"Failures: 1");
}

@Test
void shouldShowErrorWhenBlockingActionFails()
		throws Exception {
	
	SpringProject project =
			project(
					"test-app");
	
	ActionItem action =
			new ActionItem(
					ProjectAction.BUILD,
					true);
	
	doThrow(
			new IOException(
					"Maven executable not found"))
			.when(
					executeProjectActionUseCase)
			.execute(
					project,
					ProjectAction.BUILD);
	
	boolean handled =
			controller.executeBlockingAction(
					project,
					action);
	
	assertThat(handled)
			.isTrue();
	
	verify(uiState)
			.stopProjectActions();
	
	verify(uiState)
			.showErrorMessage(
					ProjectAction.BUILD
							.displayName()
							+ " failed: "
							+ "Maven executable not found");
	
	verify(
			uiState,
			never())
			.showProjectActionOutput(
					org.mockito.ArgumentMatchers.any(),
					org.mockito.ArgumentMatchers.anyInt());
}

@Test
void shouldHandleBlockingActionFailureWithoutMessage()
		throws Exception {
	
	SpringProject project =
			project(
					"test-app");
	
	ActionItem action =
			new ActionItem(
					ProjectAction.TEST,
					true);
	
	doThrow(
			new IOException())
			.when(
					executeProjectActionUseCase)
			.execute(
					project,
					ProjectAction.TEST);
	
	boolean handled =
			controller.executeBlockingAction(
					project,
					action);
	
	assertThat(handled)
			.isTrue();
	
	verify(uiState)
			.stopProjectActions();
	
	verify(uiState)
			.showErrorMessage(
					ProjectAction.TEST
							.displayName()
							+ " failed");
}

@Test
void shouldHandleBlockingActionFailureWithBlankMessage()
		throws Exception {
	
	SpringProject project =
			project(
					"test-app");
	
	ActionItem action =
			new ActionItem(
					ProjectAction.BUILD,
					true);
	
	doThrow(
			new IOException(
					"   "))
			.when(
					executeProjectActionUseCase)
			.execute(
					project,
					ProjectAction.BUILD);
	
	controller.executeBlockingAction(
			project,
			action);
	
	verify(uiState)
			.showErrorMessage(
					ProjectAction.BUILD
							.displayName()
							+ " failed");
}

@Test
void shouldRestoreInterruptFlagWhenBlockingActionIsInterrupted()
		throws Exception {
	
	SpringProject project =
			project(
					"test-app");
	
	ActionItem action =
			new ActionItem(
					ProjectAction.TEST,
					true);
	
	doThrow(
			new InterruptedException(
					"Interrupted"))
			.when(
					executeProjectActionUseCase)
			.execute(
					project,
					ProjectAction.TEST);
	
	boolean handled =
			controller.executeBlockingAction(
					project,
					action);
	
	assertThat(handled)
			.isTrue();
	
	assertThat(
			Thread.currentThread()
					.isInterrupted())
			.isTrue();
	
	verify(uiState)
			.stopProjectActions();
	
	verify(uiState)
			.showErrorMessage(
					ProjectAction.TEST
							.displayName()
							+ " interrupted for "
							+ "test-app");
	
	verify(
			uiState,
			never())
			.showProjectActionOutput(
					org.mockito.ArgumentMatchers.any(),
					org.mockito.ArgumentMatchers.anyInt());
}

private SpringProject project(
		String name) {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	when(project.name())
			.thenReturn(
					name);
	
	return project;
}

private List<ActionItem> actions() {
	
	return List.of(
			new ActionItem(
					ProjectAction.BUILD,
					true),
			new ActionItem(
					ProjectAction.TEST,
					true));
}
}