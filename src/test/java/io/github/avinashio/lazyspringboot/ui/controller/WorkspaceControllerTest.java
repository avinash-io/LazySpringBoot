package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.service.WorkspaceService;
import io.github.avinashio.lazyspringboot.ui.service.DesktopIntegrationService;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import io.github.avinashio.lazyspringboot.ui.state.WorkspaceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WorkspaceControllerTest {

private WorkspaceState workspaceState;

private WorkspaceService workspaceService;

private DesktopIntegrationService
		desktopIntegrationService;

private UiState uiState;

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
	
	controller =
			new WorkspaceController(
					workspaceState,
					workspaceService,
					desktopIntegrationService,
					uiState);
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
void shouldReportWorkspaceDialogAsOpen() {
	
	when(workspaceState.isOpen())
			.thenReturn(
					true);
	
	boolean open =
			controller.isOpen();
	
	assertThat(open)
			.isTrue();
	
	verify(workspaceState)
			.isOpen();
}

@Test
void shouldReportWorkspaceDialogAsClosed() {
	
	when(workspaceState.isOpen())
			.thenReturn(
					false);
	
	boolean open =
			controller.isOpen();
	
	assertThat(open)
			.isFalse();
	
	verify(workspaceState)
			.isOpen();
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
	
	verify(workspaceService)
			.workspace();
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
	
	verify(desktopIntegrationService)
			.copyToClipboard(
					workspace.toString());
	
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
	
	verify(desktopIntegrationService)
			.openFolder(
					workspace);
	
	verify(uiState)
			.showErrorMessage(
					"Unable to open workspace.");
}
}