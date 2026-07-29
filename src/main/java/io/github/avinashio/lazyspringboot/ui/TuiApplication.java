package io.github.avinashio.lazyspringboot.ui;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectActionOutput;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.command.CommandPaletteController;
import io.github.avinashio.lazyspringboot.ui.controller.*;
import io.github.avinashio.lazyspringboot.ui.input.*;
import io.github.avinashio.lazyspringboot.ui.screen.EnvironmentScreen;
import io.github.avinashio.lazyspringboot.ui.screen.ScreenRenderer;
import io.github.avinashio.lazyspringboot.ui.state.InputMode;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@ConditionalOnProperty(
		name = "lazyspringboot.tui.enabled",
		havingValue = "true",
		matchIfMissing = true)
public class TuiApplication
		implements ApplicationRunner {

private static final long
		UI_REFRESH_INTERVAL_MILLIS = 150;

private static final long
		PROJECT_REFRESH_INTERVAL_MILLIS = 2_000;

private final ProjectRefreshController
		projectRefreshController;
private final Terminal terminal;
private final KeyReader keyReader;
private final UiState uiState;
private final GetProjectProcessUseCase
		getProjectProcessUseCase;
private final InputDispatcher inputDispatcher;
private final StartupController
		startupController;
private final QuitInputHandler
		quitInputHandler;
private final ConfigurableApplicationContext
		applicationContext;
private final QuitController
		quitController;
private final ScreenRenderer
		screenRenderer;
private final CommandPaletteController
		commandPaletteController;
private final EnvironmentScreen
		environmentScreen;
private final CreateProjectController createProjectController;
private final WorkspaceController workspaceController;
private long lastProjectRefreshTime;

public TuiApplication(
		Terminal terminal,
		KeyReader keyReader,
		UiState uiState,
		InputDispatcher inputDispatcher,
		GetProjectProcessUseCase
				getProjectProcessUseCase,
		ProjectRefreshController
				projectRefreshController,
		StartupController startupController,
		QuitInputHandler quitInputHandler,
		ConfigurableApplicationContext
				applicationContext, QuitController quitController,
		ScreenRenderer screenRenderer, CommandPaletteController commandPaletteController, EnvironmentScreen environmentScreen, CreateProjectController createProjectController, WorkspaceController workspaceController) {
	
	this.terminal =
			terminal;
	
	this.keyReader =
			keyReader;
	
	this.uiState =
			uiState;
	
	
	this.getProjectProcessUseCase =
			getProjectProcessUseCase;
	
	
	this.inputDispatcher =
			inputDispatcher;
	
	this.startupController =
			startupController;
	
	
	this.projectRefreshController =
			projectRefreshController;
	
	
	this.quitInputHandler = quitInputHandler;
	this.applicationContext = applicationContext;
	this.quitController = quitController;
	this.screenRenderer = screenRenderer;
	this.commandPaletteController = commandPaletteController;
	this.environmentScreen = environmentScreen;
	this.createProjectController = createProjectController;
	this.workspaceController = workspaceController;
}

@Override
public void run(
		ApplicationArguments args)
		throws Exception {
	
	var originalAttributes =
			terminal.getAttributes();
	
	try {
		
		terminal.enterRawMode();
		
		terminal.puts(
				InfoCmp.Capability.enter_ca_mode);
		
		terminal.puts(
				InfoCmp.Capability.cursor_invisible);
		
		terminal.flush();
		
		startupController.initialize();
		
		render();
		
		runEventLoop();
		
	} finally {
		
		terminal.puts(
				InfoCmp.Capability.cursor_visible);
		
		terminal.puts(
				InfoCmp.Capability.exit_ca_mode);
		
		terminal.setAttributes(
				originalAttributes);
		
		terminal.flush();
		
		terminal.writer()
				.println();
		
		terminal.writer()
				.flush();
	}
	
	System.exit(
			SpringApplication.exit(
					applicationContext));
}

private void render() {
	
	screenRenderer.render(
			uiState);
}

private void runEventLoop()
		throws Exception {
	
	while (true) {
		
		KeyEvent keyEvent =
				readNextKeyEvent();
		
		if (keyEvent.type()
					== KeyType.TIMEOUT) {
			
			handleTimeout();
			
			continue;
		}
		
		if (quitController.active()) {
			
			QuitDecision decision =
					quitInputHandler.handle(
							keyEvent);
			
			if (decision
						== QuitDecision.QUIT) {
				
				return;
			}
			
			render();
			
			continue;
		}
		
		if (isQuitKey(
				keyEvent)) {
			
			QuitDecision decision =
					quitController.requestQuit();
			
			if (decision
						== QuitDecision.QUIT) {
				
				return;
			}
			
			render();
			
			continue;
		}
		
		handleKey(
				keyEvent);
		
		render();
	}
}

private KeyEvent readNextKeyEvent()
		throws IOException {
	
	if (shouldUseRefreshTimeout()) {
		
		return keyReader.read(
				UI_REFRESH_INTERVAL_MILLIS);
	}
	
	return keyReader.read(
			PROJECT_REFRESH_INTERVAL_MILLIS);
}

private boolean shouldUseRefreshTimeout() {
	
	return isLiveProcessOutputVisible()
				   || isSelectedProjectActive();
}

private boolean isProjectStarting() {
	
	SpringProject project =
			uiState.selectedProject();
	
	if (project == null) {
		return false;
	}
	
	return getProjectProcessUseCase
				   .get(project)
				   .map(ProjectProcess::status)
				   .filter(
						   status ->
								   status
										   == ProjectProcessStatus.STARTING)
				   .isPresent();
}

private boolean isSelectedProjectActive() {
	
	SpringProject project =
			uiState.selectedProject();
	
	if (project == null) {
		return false;
	}
	
	return getProjectProcessUseCase
				   .get(project)
				   .map(ProjectProcess::running)
				   .orElse(false);
}

private boolean isLiveProcessOutputVisible() {
	
	ProjectActionOutput output =
			uiState.projectActionOutput();
	
	return uiState.projectActionOutputActive()
				   && output != null
				   && output.action()
							  == ProjectAction.VIEW_LOGS;
}

private void handleTimeout() {
	
	refreshProjectsIfNeeded();
	
	render();
}

private void refreshProjectsIfNeeded() {
	
	if (!isMainScreenActive()) {
		return;
	}
	
	long currentTime =
			System.currentTimeMillis();
	
	if (currentTime
				- lastProjectRefreshTime
				< PROJECT_REFRESH_INTERVAL_MILLIS) {
		
		return;
	}
	
	try {
		
		projectRefreshController.refresh();
		
		lastProjectRefreshTime =
				currentTime;
		
	} catch (IOException exception) {
		
		uiState.showErrorMessage(
				"Failed to refresh projects: "
						+ exception.getMessage());
		
		lastProjectRefreshTime =
				currentTime;
	}
}

private boolean isMainScreenActive() {
	
	return !commandPaletteController.active()
				   && !createProjectController
							   .state()
							   .active()
				   && !workspaceController.isOpen()
				   && !uiState
							   .dependencyConfirmationActive()
				   && !uiState
							   .projectActionsActive()
				   && !uiState
							   .projectActionOutputActive();
}

private boolean isQuitKey(
		KeyEvent keyEvent) {
	
	if (createProjectController
				.state()
				.active()) {
		
		return false;
	}
	
	return uiState.inputMode()
				   == InputMode.NAVIGATION
				   && !uiState.projectActionsActive()
				   && !uiState.projectActionOutputActive()
				   && keyEvent.type()
							  == KeyType.CHARACTER
				   && keyEvent.hasCharacter()
				   && keyEvent.character() == 'q';
}

private void handleKey(
		KeyEvent keyEvent) {
	
	inputDispatcher.handle(
			keyEvent);
}
}