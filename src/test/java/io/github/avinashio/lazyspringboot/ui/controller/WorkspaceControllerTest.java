package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.service.WorkspaceService;
import io.github.avinashio.lazyspringboot.ui.service.DesktopIntegrationService;
import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import io.github.avinashio.lazyspringboot.ui.state.WorkspaceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WorkspaceControllerTest {

@TempDir
Path tempDirectory;

private WorkspaceState workspaceState;

private WorkspaceService workspaceService;

private DesktopIntegrationService
		desktopIntegrationService;

private UiState uiState;

private TextInputController
		textInputController;

private DiscoverProjectsUseCase
		discoverProjectsUseCase;

private WorkspaceController controller;

@BeforeEach
void setUp() {
	
	workspaceState =
			mock(
					WorkspaceState.class);
	
	workspaceService =
			mock(
					WorkspaceService.class);
	
	desktopIntegrationService =
			mock(
					DesktopIntegrationService.class);
	
	uiState =
			mock(
					UiState.class);
	
	textInputController =
			mock(
					TextInputController.class);
	
	discoverProjectsUseCase =
			mock(
					DiscoverProjectsUseCase.class);
	
	controller =
			new WorkspaceController(
					workspaceState,
					workspaceService,
					desktopIntegrationService,
					uiState,
					textInputController,
					discoverProjectsUseCase);
}

@Test
void shouldOpenWorkspaceDialog() {
	
	Path workspace =
			Path.of(
					"/workspace");
	
	when(workspaceService.workspace())
			.thenReturn(
					workspace);
	
	controller.open();
	
	verify(workspaceState)
			.open();
	
	verify(workspaceState)
			.clearErrorMessage();
	
	verify(workspaceState)
			.setWorkspace(
					workspace.toString());
}

@Test
void shouldCloseWorkspaceDialog() {
	
	controller.close();
	
	verify(workspaceState)
			.close();
}

@Test
void shouldStopWorkspaceInputWhenClosingDialog() {
	
	when(
			textInputController.active(
					TextInputPurpose.WORKSPACE_PATH))
			.thenReturn(
					true);
	
	controller.close();
	
	verify(textInputController)
			.stop();
	
	verify(workspaceState)
			.clearErrorMessage();
	
	verify(workspaceState)
			.close();
}

@Test
void shouldReportWorkspaceDialogAsOpen() {
	
	when(workspaceState.isOpen())
			.thenReturn(
					true);
	
	assertThat(controller.isOpen())
			.isTrue();
}

@Test
void shouldReportWorkspaceDialogAsClosed() {
	
	when(workspaceState.isOpen())
			.thenReturn(
					false);
	
	assertThat(controller.isOpen())
			.isFalse();
}

@Test
void shouldReturnWorkspace() {
	
	Path workspace =
			Path.of(
					"/workspace");
	
	when(workspaceService.workspace())
			.thenReturn(
					workspace);
	
	assertThat(controller.workspace())
			.isEqualTo(
					workspace);
}

@Test
void shouldStartWorkspaceChange() {
	
	controller.startWorkspaceChange();
	
	verify(workspaceState)
			.clearErrorMessage();
	
	verify(textInputController)
			.start(
					TextInputPurpose.WORKSPACE_PATH);
}

@Test
void shouldReportWorkspaceChangeAsActive() {
	
	when(
			textInputController.active(
					TextInputPurpose.WORKSPACE_PATH))
			.thenReturn(
					true);
	
	assertThat(controller.changingWorkspace())
			.isTrue();
}

@Test
void shouldReturnWorkspaceInput() {
	
	when(textInputController.value())
			.thenReturn(
					"/new/workspace");
	
	assertThat(controller.workspaceInput())
			.isEqualTo(
					"/new/workspace");
}

@Test
void shouldAppendWorkspaceCharacter() {
	
	controller.appendWorkspaceCharacter(
			'a');
	
	verify(textInputController)
			.append(
					'a');
}

@Test
void shouldBackspaceWorkspaceInput() {
	
	controller.backspaceWorkspaceInput();
	
	verify(textInputController)
			.backspace();
}

@Test
void shouldCancelWorkspaceChange() {
	
	when(
			textInputController.active(
					TextInputPurpose.WORKSPACE_PATH))
			.thenReturn(
					true);
	
	controller.cancelWorkspaceChange();
	
	verify(textInputController)
			.stop();
	
	verify(workspaceState)
			.clearErrorMessage();
}

@Test
void shouldNotStopTextInputWhenWorkspaceChangeIsInactive() {
	
	when(
			textInputController.active(
					TextInputPurpose.WORKSPACE_PATH))
			.thenReturn(
					false);
	
	controller.cancelWorkspaceChange();
	
	verify(
			textInputController,
			never())
			.stop();
}

@Test
void shouldRejectEmptyWorkspacePath()
		throws IOException {
	
	when(
			textInputController.active(
					TextInputPurpose.WORKSPACE_PATH))
			.thenReturn(
					true);
	
	when(textInputController.value())
			.thenReturn(
					"   ");
	
	controller.submitWorkspaceChange();
	
	verify(workspaceState)
			.showErrorMessage(
					"Workspace path cannot be empty.");
	
	verify(
			workspaceService,
			never())
			.changeWorkspace(
					org.mockito.ArgumentMatchers.any());
}

@Test
void shouldRejectMissingWorkspaceDirectory()
		throws IOException {
	
	Path missingDirectory =
			tempDirectory.resolve(
					"missing");
	
	when(
			textInputController.active(
					TextInputPurpose.WORKSPACE_PATH))
			.thenReturn(
					true);
	
	when(textInputController.value())
			.thenReturn(
					missingDirectory.toString());
	
	controller.submitWorkspaceChange();
	
	verify(workspaceState)
			.showErrorMessage(
					"Workspace directory does not exist.");
	
	verify(
			workspaceService,
			never())
			.changeWorkspace(
					org.mockito.ArgumentMatchers.any());
}

@Test
void shouldChangeWorkspaceAndRefreshProjects()
		throws Exception {
	
	Path workspace =
			Files.createDirectory(
					tempDirectory.resolve(
							"workspace"));
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	when(
			textInputController.active(
					TextInputPurpose.WORKSPACE_PATH))
			.thenReturn(
					true);
	
	when(textInputController.value())
			.thenReturn(
					workspace.toString());
	
	when(discoverProjectsUseCase.discover())
			.thenReturn(
					List.of(
							project));
	
	controller.submitWorkspaceChange();
	
	verify(workspaceService)
			.changeWorkspace(
					workspace
							.toAbsolutePath()
							.normalize());
	
	verify(discoverProjectsUseCase)
			.discover();
	
	verify(uiState)
			.setProjects(
					List.of(
							project));
	
	verify(workspaceState)
			.setWorkspace(
					workspace
							.toAbsolutePath()
							.normalize()
							.toString());
	
	verify(workspaceState)
			.clearErrorMessage();
	
	verify(textInputController)
			.stop();
	
	verify(uiState)
			.showSuccessMessage(
					"Workspace changed.");
}

@Test
void shouldShowErrorWhenWorkspaceCannotBeChanged()
		throws Exception {
	
	Path workspace =
			Files.createDirectory(
					tempDirectory.resolve(
							"workspace"));
	
	when(
			textInputController.active(
					TextInputPurpose.WORKSPACE_PATH))
			.thenReturn(
					true);
	
	when(textInputController.value())
			.thenReturn(
					workspace.toString());
	
	org.mockito.Mockito.doThrow(
					new IOException(
							"Unable to save"))
			.when(workspaceService)
			.changeWorkspace(
					workspace
							.toAbsolutePath()
							.normalize());
	
	controller.submitWorkspaceChange();
	
	verify(workspaceState)
			.showErrorMessage(
					"Unable to change workspace.");
	
	verify(
			textInputController,
			never())
			.stop();
}

@Test
void shouldCopyWorkspacePath() {
	
	Path workspace =
			Path.of(
					"/workspace");
	
	when(workspaceService.workspace())
			.thenReturn(
					workspace);
	
	when(
			desktopIntegrationService
					.copyToClipboard(
							workspace.toString()))
			.thenReturn(
					true);
	
	controller.copyWorkspacePath();
	
	verify(desktopIntegrationService)
			.copyToClipboard(
					workspace.toString());
	
	verify(uiState)
			.showSuccessMessage(
					"Workspace path copied.");
}

@Test
void shouldShowErrorWhenWorkspacePathCannotBeCopied() {
	
	Path workspace =
			Path.of(
					"/workspace");
	
	when(workspaceService.workspace())
			.thenReturn(
					workspace);
	
	when(
			desktopIntegrationService
					.copyToClipboard(
							workspace.toString()))
			.thenReturn(
					false);
	
	controller.copyWorkspacePath();
	
	verify(uiState)
			.showErrorMessage(
					"Unable to copy workspace path.");
}

@Test
void shouldOpenWorkspaceFolder() {
	
	Path workspace =
			Path.of(
					"/workspace");
	
	when(workspaceService.workspace())
			.thenReturn(
					workspace);
	
	when(
			desktopIntegrationService
					.openFolder(
							workspace))
			.thenReturn(
					true);
	
	controller.openWorkspace();
	
	verify(desktopIntegrationService)
			.openFolder(
					workspace);
	
	verify(uiState)
			.showSuccessMessage(
					"Workspace opened.");
}

@Test
void shouldShowErrorWhenWorkspaceFolderCannotBeOpened() {
	
	Path workspace =
			Path.of(
					"/workspace");
	
	when(workspaceService.workspace())
			.thenReturn(
					workspace);
	
	when(
			desktopIntegrationService
					.openFolder(
							workspace))
			.thenReturn(
					false);
	
	controller.openWorkspace();
	
	verify(uiState)
			.showErrorMessage(
					"Unable to open workspace.");
}
}