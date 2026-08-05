package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.service.DesktopIntegrationService;
import io.github.avinashio.lazyspringboot.ui.state.ProjectDetailsState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProjectDetailsControllerTest {

private ProjectDetailsState state;

private UiState uiState;

private DesktopIntegrationService
		desktopIntegrationService;

private ProjectDetailsController controller;

@BeforeEach
void setUp() {
	
	state =
			mock(
					ProjectDetailsState.class);
	
	uiState =
			mock(
					UiState.class);
	
	desktopIntegrationService =
			mock(
					DesktopIntegrationService.class);
	
	controller =
			new ProjectDetailsController(
					state,
					uiState,
					desktopIntegrationService);
}

@Test
void shouldOpenProjectDetails() {
	
	controller.open();
	
	verify(state)
			.open();
}

@Test
void shouldCloseProjectDetails() {
	
	controller.close();
	
	verify(state)
			.close();
}

@Test
void shouldReportProjectDetailsAsOpen() {
	
	when(state.isOpen())
			.thenReturn(
					true);
	
	assertThat(controller.isOpen())
			.isTrue();
}

@Test
void shouldReportProjectDetailsAsClosed() {
	
	when(state.isOpen())
			.thenReturn(
					false);
	
	assertThat(controller.isOpen())
			.isFalse();
}

@Test
void shouldReturnSelectedProject() {
	
	SpringProject project =
			project();
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	assertThat(controller.selectedProject())
			.isSameAs(
					project);
}

@Test
void shouldCopyProjectPath() {
	
	SpringProject project =
			project();
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	when(
			desktopIntegrationService
					.copyToClipboard(
							project.path()
									.toString()))
			.thenReturn(
					true);
	
	controller.copyProjectPath();
	
	verify(desktopIntegrationService)
			.copyToClipboard(
					project.path()
							.toString());
	
	verify(uiState)
			.showSuccessMessage(
					"Copied path for test-app");
}

@Test
void shouldShowErrorWhenProjectPathCannotBeCopied() {
	
	SpringProject project =
			project();
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	when(
			desktopIntegrationService
					.copyToClipboard(
							project.path()
									.toString()))
			.thenReturn(
					false);
	
	controller.copyProjectPath();
	
	verify(uiState)
			.showErrorMessage(
					"Unable to copy project path.");
}

@Test
void shouldNotCopyPathWithoutSelectedProject() {
	
	when(uiState.selectedProject())
			.thenReturn(
					null);
	
	controller.copyProjectPath();
	
	verify(uiState)
			.showErrorMessage(
					"No project selected.");
	
	verify(
			desktopIntegrationService,
			never())
			.copyToClipboard(
					org.mockito.ArgumentMatchers
							.anyString());
}

@Test
void shouldOpenProjectFolder() {
	
	SpringProject project =
			project();
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	when(
			desktopIntegrationService
					.openFolder(
							project.path()))
			.thenReturn(
					true);
	
	controller.openProjectFolder();
	
	verify(desktopIntegrationService)
			.openFolder(
					project.path());
	
	verify(uiState)
			.showSuccessMessage(
					"Opened test-app");
}

@Test
void shouldShowErrorWhenProjectFolderCannotBeOpened() {
	
	SpringProject project =
			project();
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	when(
			desktopIntegrationService
					.openFolder(
							project.path()))
			.thenReturn(
					false);
	
	controller.openProjectFolder();
	
	verify(uiState)
			.showErrorMessage(
					"Unable to open test-app");
}

@Test
void shouldNotOpenFolderWithoutSelectedProject() {
	
	when(uiState.selectedProject())
			.thenReturn(
					null);
	
	controller.openProjectFolder();
	
	verify(uiState)
			.showErrorMessage(
					"No project selected.");
	
	verify(
			desktopIntegrationService,
			never())
			.openFolder(
					org.mockito.ArgumentMatchers
							.any(Path.class));
}

@Test
void shouldOpenProjectInIntelliJ() {
	
	SpringProject project =
			project();
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	when(
			desktopIntegrationService
					.openIntelliJ(
							project.path()))
			.thenReturn(
					true);
	
	controller.openIntelliJ();
	
	verify(desktopIntegrationService)
			.openIntelliJ(
					project.path());
	
	verify(uiState)
			.showSuccessMessage(
					"Opened test-app in IntelliJ.");
}

@Test
void shouldShowErrorWhenIntelliJCannotBeOpened() {
	
	SpringProject project =
			project();
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	when(
			desktopIntegrationService
					.openIntelliJ(
							project.path()))
			.thenReturn(
					false);
	
	controller.openIntelliJ();
	
	verify(uiState)
			.showErrorMessage(
					"Unable to open IntelliJ.");
}

@Test
void shouldNotOpenIntelliJWithoutSelectedProject() {
	
	when(uiState.selectedProject())
			.thenReturn(
					null);
	
	controller.openIntelliJ();
	
	verify(uiState)
			.showErrorMessage(
					"No project selected.");
	
	verify(
			desktopIntegrationService,
			never())
			.openIntelliJ(
					org.mockito.ArgumentMatchers
							.any(Path.class));
}

@Test
void shouldOpenProjectInVSCode() {
	
	SpringProject project =
			project();
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	when(
			desktopIntegrationService
					.openVSCode(
							project.path()))
			.thenReturn(
					true);
	
	controller.openVSCode();
	
	verify(desktopIntegrationService)
			.openVSCode(
					project.path());
	
	verify(uiState)
			.showSuccessMessage(
					"Opened test-app in VS Code.");
}

@Test
void shouldShowErrorWhenVSCodeCannotBeOpened() {
	
	SpringProject project =
			project();
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	when(
			desktopIntegrationService
					.openVSCode(
							project.path()))
			.thenReturn(
					false);
	
	controller.openVSCode();
	
	verify(uiState)
			.showErrorMessage(
					"Unable to open VS Code.");
}

@Test
void shouldNotOpenVSCodeWithoutSelectedProject() {
	
	when(uiState.selectedProject())
			.thenReturn(
					null);
	
	controller.openVSCode();
	
	verify(uiState)
			.showErrorMessage(
					"No project selected.");
	
	verify(
			desktopIntegrationService,
			never())
			.openVSCode(
					org.mockito.ArgumentMatchers
							.any(Path.class));
}

private SpringProject project() {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	when(project.name())
			.thenReturn(
					"test-app");
	
	when(project.path())
			.thenReturn(
					Path.of(
							"/workspace/test-app"));
	
	return project;
}
}