package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.RestartProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.StartProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.StopProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectActionOutput;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.screen.ProjectActionOutputScreen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProcessControllerTest {

@Test
void shouldCreateController() {
	
	ProcessController controller =
			new ProcessController(
					mock(UiState.class),
					mock(StartProjectProcessUseCase.class),
					mock(StopProjectProcessUseCase.class),
					mock(RestartProjectProcessUseCase.class),
					mock(GetProjectProcessUseCase.class),
					mock(ProjectActionOutputScreen.class));
	
	assertThat(controller)
			.isNotNull();
}

@Test
void shouldStartProject()
		throws IOException {
	
	UiState uiState =
			mock(UiState.class);
	
	StartProjectProcessUseCase startUseCase =
			mock(
					StartProjectProcessUseCase.class);
	
	SpringProject project =
			project(
					"test-app");
	
	ProcessController controller =
			createController(
					uiState,
					startUseCase);
	
	controller.start(
			project);
	
	verify(startUseCase)
			.start(
					project);
	
	verify(uiState)
			.stopProjectActions();
	
	verify(uiState)
			.showSuccessMessage(
					"Started test-app");
	
	verify(
			uiState,
			never())
			.showErrorMessage(
					org.mockito.ArgumentMatchers.anyString());
}

@Test
void shouldShowErrorMessageWhenStartFails()
		throws IOException {
	
	UiState uiState =
			mock(UiState.class);
	
	StartProjectProcessUseCase startUseCase =
			mock(
					StartProjectProcessUseCase.class);
	
	SpringProject project =
			project(
					"test-app");
	
	doThrow(
			new IOException(
					"Process start failed"))
			.when(startUseCase)
			.start(
					project);
	
	ProcessController controller =
			createController(
					uiState,
					startUseCase);
	
	controller.start(
			project);
	
	verify(uiState)
			.stopProjectActions();
	
	verify(uiState)
			.showErrorMessage(
					"Failed to start test-app: "
							+ "Process start failed");
	
	verify(
			uiState,
			never())
			.showSuccessMessage(
					org.mockito.ArgumentMatchers.anyString());
}

@Test
void shouldHandleStartFailureWithoutExceptionMessage()
		throws IOException {
	
	UiState uiState =
			mock(UiState.class);
	
	StartProjectProcessUseCase startUseCase =
			mock(
					StartProjectProcessUseCase.class);
	
	SpringProject project =
			project(
					"test-app");
	
	doThrow(
			new IOException())
			.when(startUseCase)
			.start(
					project);
	
	ProcessController controller =
			createController(
					uiState,
					startUseCase);
	
	controller.start(
			project);
	
	verify(uiState)
			.stopProjectActions();
	
	verify(uiState)
			.showErrorMessage(
					"Failed to start test-app");
}

@Test
void shouldRestartProject()
		throws IOException {
	
	UiState uiState =
			mock(UiState.class);
	
	RestartProjectProcessUseCase restartUseCase =
			mock(
					RestartProjectProcessUseCase.class);
	
	SpringProject project =
			project(
					"test-app");
	
	ProcessController controller =
			createController(
					uiState,
					restartUseCase);
	
	controller.restart(
			project);
	
	verify(restartUseCase)
			.restart(
					project);
	
	verify(uiState)
			.stopProjectActions();
	
	verify(uiState)
			.showSuccessMessage(
					"Restarted test-app");
	
	verify(
			uiState,
			never())
			.showErrorMessage(
					org.mockito.ArgumentMatchers.anyString());
}

@Test
void shouldShowErrorMessageWhenRestartFails()
		throws IOException {
	
	UiState uiState =
			mock(UiState.class);
	
	RestartProjectProcessUseCase restartUseCase =
			mock(
					RestartProjectProcessUseCase.class);
	
	SpringProject project =
			project(
					"test-app");
	
	doThrow(
			new IOException(
					"Process restart failed"))
			.when(restartUseCase)
			.restart(
					project);
	
	ProcessController controller =
			createController(
					uiState,
					restartUseCase);
	
	controller.restart(
			project);
	
	verify(uiState)
			.stopProjectActions();
	
	verify(uiState)
			.showErrorMessage(
					"Failed to restart test-app: "
							+ "Process restart failed");
	
	verify(
			uiState,
			never())
			.showSuccessMessage(
					org.mockito.ArgumentMatchers.anyString());
}

@Test
void shouldHandleRestartFailureWithoutExceptionMessage()
		throws IOException {
	
	UiState uiState =
			mock(UiState.class);
	
	RestartProjectProcessUseCase restartUseCase =
			mock(
					RestartProjectProcessUseCase.class);
	
	SpringProject project =
			project(
					"test-app");
	
	doThrow(
			new IOException(
					"   "))
			.when(restartUseCase)
			.restart(
					project);
	
	ProcessController controller =
			createController(
					uiState,
					restartUseCase);
	
	controller.restart(
			project);
	
	verify(uiState)
			.showErrorMessage(
					"Failed to restart test-app");
}

@Test
void shouldStopProject() {
	
	UiState uiState =
			mock(UiState.class);
	
	StopProjectProcessUseCase stopUseCase =
			mock(
					StopProjectProcessUseCase.class);
	
	SpringProject project =
			project(
					"test-app");
	
	when(
			stopUseCase.stop(
					project))
			.thenReturn(
					true);
	
	ProcessController controller =
			createController(
					uiState,
					stopUseCase);
	
	controller.stop(
			project);
	
	verify(stopUseCase)
			.stop(
					project);
	
	verify(uiState)
			.stopProjectActions();
	
	verify(uiState)
			.showSuccessMessage(
					"Stopping test-app");
	
	verify(
			uiState,
			never())
			.showErrorMessage(
					org.mockito.ArgumentMatchers.anyString());
}

@Test
void shouldShowErrorWhenStoppingProjectThatIsNotRunning() {
	
	UiState uiState =
			mock(UiState.class);
	
	StopProjectProcessUseCase stopUseCase =
			mock(
					StopProjectProcessUseCase.class);
	
	SpringProject project =
			project(
					"test-app");
	
	when(
			stopUseCase.stop(
					project))
			.thenReturn(
					false);
	
	ProcessController controller =
			createController(
					uiState,
					stopUseCase);
	
	controller.stop(
			project);
	
	verify(stopUseCase)
			.stop(
					project);
	
	verify(uiState)
			.stopProjectActions();
	
	verify(uiState)
			.showErrorMessage(
					"Project is not running: test-app");
	
	verify(
			uiState,
			never())
			.showSuccessMessage(
					org.mockito.ArgumentMatchers.anyString());
}

@Test
void shouldShowLogsForProjectProcess() {
	
	UiState uiState =
			new UiState();
	
	GetProjectProcessUseCase getProjectProcessUseCase =
			mock(
					GetProjectProcessUseCase.class);
	
	ProjectActionOutputScreen outputScreen =
			mock(
					ProjectActionOutputScreen.class);
	
	SpringProject project =
			project(
					"test-app");
	
	when(outputScreen.visibleHeight())
			.thenReturn(
					3);
	
	when(
			getProjectProcessUseCase.get(
					project))
			.thenReturn(
					Optional.of(
							process(
									List.of(
											"line-1",
											"line-2",
											"line-3",
											"line-4",
											"line-5"))));
	
	ProcessController controller =
			createController(
					uiState,
					getProjectProcessUseCase,
					outputScreen);
	
	controller.showLogs(
			project);
	
	ProjectActionOutput output =
			uiState.projectActionOutput();
	
	assertThat(output)
			.isNotNull();
	
	assertThat(output.projectName())
			.isEqualTo(
					"test-app");
	
	assertThat(output.action())
			.isEqualTo(
					ProjectAction.VIEW_LOGS);
	
	assertThat(output.lines())
			.containsExactly(
					"line-1",
					"line-2",
					"line-3",
					"line-4",
					"line-5");
	
	assertThat(
			uiState.outputViewport()
					.offset())
			.isEqualTo(
					2);
}

@Test
void shouldShowErrorWhenProjectProcessDoesNotExist() {
	
	UiState uiState =
			mock(
					UiState.class);
	
	GetProjectProcessUseCase getProjectProcessUseCase =
			mock(
					GetProjectProcessUseCase.class);
	
	ProjectActionOutputScreen outputScreen =
			mock(
					ProjectActionOutputScreen.class);
	
	SpringProject project =
			project(
					"test-app");
	
	when(
			getProjectProcessUseCase.get(
					project))
			.thenReturn(
					Optional.empty());
	
	ProcessController controller =
			createController(
					uiState,
					getProjectProcessUseCase,
					outputScreen);
	
	controller.showLogs(
			project);
	
	verify(uiState)
			.stopProjectActions();
	
	verify(uiState)
			.showErrorMessage(
					"No process found for test-app");
	
	verify(
			outputScreen,
			never())
			.visibleHeight();
}

@Test
void shouldRefreshVisibleLogs() {
	
	UiState uiState =
			new UiState();
	
	GetProjectProcessUseCase getProjectProcessUseCase =
			mock(
					GetProjectProcessUseCase.class);
	
	ProjectActionOutputScreen outputScreen =
			mock(
					ProjectActionOutputScreen.class);
	
	SpringProject project =
			project(
					"test-app");
	
	when(outputScreen.visibleHeight())
			.thenReturn(
					3);
	
	when(
			getProjectProcessUseCase.get(
					project))
			.thenReturn(
					Optional.of(
							process(
									List.of(
											"line-1",
											"line-2",
											"line-3"))));
	
	ProcessController controller =
			createController(
					uiState,
					getProjectProcessUseCase,
					outputScreen);
	
	controller.showLogs(
			project);
	
	when(
			getProjectProcessUseCase.get(
					project))
			.thenReturn(
					Optional.of(
							process(
									List.of(
											"line-1",
											"line-2",
											"line-3",
											"line-4"))));
	
	controller.refreshLogs(
			project);
	
	assertThat(
			uiState.projectActionOutput()
					.lines())
			.containsExactly(
					"line-1",
					"line-2",
					"line-3",
					"line-4");
}

@Test
void shouldIgnoreRefreshWhenLogsAreNotVisible() {
	
	UiState uiState =
			new UiState();
	
	GetProjectProcessUseCase getProjectProcessUseCase =
			mock(
					GetProjectProcessUseCase.class);
	
	ProjectActionOutputScreen outputScreen =
			mock(
					ProjectActionOutputScreen.class);
	
	SpringProject project =
			project(
					"test-app");
	
	ProcessController controller =
			createController(
					uiState,
					getProjectProcessUseCase,
					outputScreen);
	
	controller.refreshLogs(
			project);
	
	verify(
			getProjectProcessUseCase,
			never())
			.get(
					project);
}

@Test
void shouldFollowNewLogsWhenViewportIsAtBottom() {
	
	UiState uiState =
			new UiState();
	
	GetProjectProcessUseCase getProjectProcessUseCase =
			mock(
					GetProjectProcessUseCase.class);
	
	ProjectActionOutputScreen outputScreen =
			mock(
					ProjectActionOutputScreen.class);
	
	SpringProject project =
			project(
					"test-app");
	
	when(outputScreen.visibleHeight())
			.thenReturn(
					3);
	
	when(
			getProjectProcessUseCase.get(
					project))
			.thenReturn(
					Optional.of(
							process(
									List.of(
											"line-1",
											"line-2",
											"line-3",
											"line-4",
											"line-5"))));
	
	ProcessController controller =
			createController(
					uiState,
					getProjectProcessUseCase,
					outputScreen);
	
	controller.showLogs(
			project);
	
	assertThat(
			uiState.outputViewport()
					.offset())
			.isEqualTo(
					2);
	
	when(
			getProjectProcessUseCase.get(
					project))
			.thenReturn(
					Optional.of(
							process(
									List.of(
											"line-1",
											"line-2",
											"line-3",
											"line-4",
											"line-5",
											"line-6",
											"line-7"))));
	
	controller.refreshLogs(
			project);
	
	assertThat(
			uiState.outputViewport()
					.offset())
			.isEqualTo(
					4);
	
	assertThat(
			uiState.projectActionOutput()
					.lines())
			.hasSize(
					7);
}

@Test
void shouldPreserveViewportWhenUserHasScrolledUp() {
	
	UiState uiState =
			new UiState();
	
	GetProjectProcessUseCase getProjectProcessUseCase =
			mock(
					GetProjectProcessUseCase.class);
	
	ProjectActionOutputScreen outputScreen =
			mock(
					ProjectActionOutputScreen.class);
	
	SpringProject project =
			project(
					"test-app");
	
	when(outputScreen.visibleHeight())
			.thenReturn(
					3);
	
	when(
			getProjectProcessUseCase.get(
					project))
			.thenReturn(
					Optional.of(
							process(
									List.of(
											"line-1",
											"line-2",
											"line-3",
											"line-4",
											"line-5"))));
	
	ProcessController controller =
			createController(
					uiState,
					getProjectProcessUseCase,
					outputScreen);
	
	controller.showLogs(
			project);
	
	assertThat(
			uiState.outputViewport()
					.offset())
			.isEqualTo(
					2);
	
	uiState.outputViewport()
			.scrollUp();
	
	assertThat(
			uiState.outputViewport()
					.offset())
			.isEqualTo(
					1);
	
	when(
			getProjectProcessUseCase.get(
					project))
			.thenReturn(
					Optional.of(
							process(
									List.of(
											"line-1",
											"line-2",
											"line-3",
											"line-4",
											"line-5",
											"line-6",
											"line-7"))));
	
	controller.refreshLogs(
			project);
	
	assertThat(
			uiState.outputViewport()
					.offset())
			.isEqualTo(
					1);
	
	assertThat(
			uiState.projectActionOutput()
					.lines())
			.hasSize(
					7);
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

private ProcessController createController(
		UiState uiState,
		StartProjectProcessUseCase startUseCase) {
	
	return new ProcessController(
			uiState,
			startUseCase,
			mock(
					StopProjectProcessUseCase.class),
			mock(
					RestartProjectProcessUseCase.class),
			mock(
					GetProjectProcessUseCase.class),
			mock(
					ProjectActionOutputScreen.class));
}

private ProcessController createController(
		UiState uiState,
		StopProjectProcessUseCase stopUseCase) {
	
	return new ProcessController(
			uiState,
			mock(
					StartProjectProcessUseCase.class),
			stopUseCase,
			mock(
					RestartProjectProcessUseCase.class),
			mock(
					GetProjectProcessUseCase.class),
			mock(
					ProjectActionOutputScreen.class));
}

private ProcessController createController(
		UiState uiState,
		RestartProjectProcessUseCase restartUseCase) {
	
	return new ProcessController(
			uiState,
			mock(
					StartProjectProcessUseCase.class),
			mock(
					StopProjectProcessUseCase.class),
			restartUseCase,
			mock(
					GetProjectProcessUseCase.class),
			mock(
					ProjectActionOutputScreen.class));
}

private ProcessController createController(
		UiState uiState,
		GetProjectProcessUseCase getProjectProcessUseCase,
		ProjectActionOutputScreen outputScreen) {
	
	return new ProcessController(
			uiState,
			mock(
					StartProjectProcessUseCase.class),
			mock(
					StopProjectProcessUseCase.class),
			mock(
					RestartProjectProcessUseCase.class),
			getProjectProcessUseCase,
			outputScreen);
}

private ProjectProcess process(
		List<String> output) {
	
	return new ProjectProcess(
			"test-app",
			ProjectProcessStatus.RUNNING,
			output,
			null,
			12345L,
			null,
			null);
}
}