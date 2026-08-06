package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.service.DesktopIntegrationService;
import io.github.avinashio.lazyspringboot.ui.state.ProjectManagerState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProjectManagerControllerTest {

private ProjectManagerState state;

private UiState uiState;

private DesktopIntegrationService
		desktopIntegrationService;

private ProjectManagerController controller;

@BeforeEach
void setUp() {
	
	state =
			mock(
					ProjectManagerState.class);
	
	uiState =
			mock(
					UiState.class);
	
	desktopIntegrationService =
			mock(
					DesktopIntegrationService.class);
	
	controller =
			new ProjectManagerController(
					state,
					uiState,
					desktopIntegrationService);
}

@Test
void shouldOpenProjectManagerAtDashboardSelection() {
	
	SpringProject firstProject =
			mock(
					SpringProject.class);
	
	SpringProject secondProject =
			mock(
					SpringProject.class);
	
	when(uiState.projects())
			.thenReturn(
					List.of(
							firstProject,
							secondProject));
	
	when(uiState.selectedProject())
			.thenReturn(
					secondProject);
	
	controller.open();
	
	verify(state)
			.open(
					1);
}

@Test
void shouldOpenProjectManagerAtFirstProjectWhenNothingSelected() {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	when(uiState.projects())
			.thenReturn(
					List.of(
							project));
	
	when(uiState.selectedProject())
			.thenReturn(
					null);
	
	controller.open();
	
	verify(state)
			.open(
					0);
}

@Test
void shouldOpenAtFirstProjectWhenDashboardSelectionIsNotInProjectList() {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	SpringProject unknownProject =
			mock(
					SpringProject.class);
	
	when(uiState.projects())
			.thenReturn(
					List.of(
							project));
	
	when(uiState.selectedProject())
			.thenReturn(
					unknownProject);
	
	controller.open();
	
	verify(state)
			.open(
					0);
}

@Test
void shouldCloseProjectManager() {
	
	controller.close();
	
	verify(state)
			.close();
}

@Test
void shouldReportProjectManagerAsOpen() {
	
	when(state.isOpen())
			.thenReturn(
					true);
	
	assertThat(controller.isOpen())
			.isTrue();
	
	verify(state)
			.isOpen();
}

@Test
void shouldReportProjectManagerAsClosed() {
	
	when(state.isOpen())
			.thenReturn(
					false);
	
	assertThat(controller.isOpen())
			.isFalse();
	
	verify(state)
			.isOpen();
}

@Test
void shouldReturnProjects() {
	
	SpringProject firstProject =
			mock(
					SpringProject.class);
	
	SpringProject secondProject =
			mock(
					SpringProject.class);
	
	when(uiState.projects())
			.thenReturn(
					List.of(
							firstProject,
							secondProject));
	
	assertThat(controller.projects())
			.containsExactly(
					firstProject,
					secondProject);
}

@Test
void shouldReturnEmptyProjectList() {
	
	when(uiState.projects())
			.thenReturn(
					List.of());
	
	assertThat(controller.projects())
			.isEmpty();
}

@Test
void shouldReturnPopupSelectedProject() {
	
	SpringProject firstProject =
			mock(
					SpringProject.class);
	
	SpringProject secondProject =
			mock(
					SpringProject.class);
	
	when(uiState.projects())
			.thenReturn(
					List.of(
							firstProject,
							secondProject));
	
	when(state.selectedIndex())
			.thenReturn(
					1);
	
	assertThat(controller.selectedProject())
			.isSameAs(
					secondProject);
}

@Test
void shouldReturnNullWhenProjectListIsEmpty() {
	
	when(uiState.projects())
			.thenReturn(
					List.of());
	
	assertThat(controller.selectedProject())
			.isNull();
}

@Test
void shouldClampSelectionWhenProjectListShrinks() {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	when(uiState.projects())
			.thenReturn(
					List.of(
							project));
	
	when(state.selectedIndex())
			.thenReturn(
					5);
	
	assertThat(controller.selectedProject())
			.isSameAs(
					project);
}

@Test
void shouldSelectPreviousProject() {
	
	when(uiState.projects())
			.thenReturn(
					List.of(
							mock(SpringProject.class),
							mock(SpringProject.class)));
	
	controller.selectPrevious();
	
	verify(state)
			.selectPrevious(
					2);
}

@Test
void shouldSelectNextProject() {
	
	when(uiState.projects())
			.thenReturn(
					List.of(
							mock(SpringProject.class),
							mock(SpringProject.class)));
	
	controller.selectNext();
	
	verify(state)
			.selectNext(
					2);
}

@Test
void shouldOpenPopupSelectedProjectFolder() {
	
	Path path =
			Path.of(
					"/workspace/second");
	
	SpringProject firstProject =
			project(
					"first",
					Path.of(
							"/workspace/first"));
	
	SpringProject secondProject =
			project(
					"second",
					path);
	
	when(uiState.projects())
			.thenReturn(
					List.of(
							firstProject,
							secondProject));
	
	when(state.selectedIndex())
			.thenReturn(
					1);
	
	when(desktopIntegrationService.openFolder(
			path))
			.thenReturn(
					true);
	
	controller.openProjectFolder();
	
	verify(desktopIntegrationService)
			.openFolder(
					path);
	
	verify(uiState)
			.showSuccessMessage(
					"Opened second");
}

@Test
void shouldOpenPopupSelectedProjectInIntelliJ() {
	
	Path path =
			Path.of(
					"/workspace/demo");
	
	SpringProject project =
			project(
					"demo",
					path);
	
	when(uiState.projects())
			.thenReturn(
					List.of(
							project));
	
	when(state.selectedIndex())
			.thenReturn(
					0);
	
	when(desktopIntegrationService.openIntelliJ(
			path))
			.thenReturn(
					true);
	
	controller.openIntelliJ();
	
	verify(desktopIntegrationService)
			.openIntelliJ(
					path);
	
	verify(uiState)
			.showSuccessMessage(
					"Opened demo in IntelliJ.");
}

@Test
void shouldOpenPopupSelectedProjectInVSCode() {
	
	Path path =
			Path.of(
					"/workspace/demo");
	
	SpringProject project =
			project(
					"demo",
					path);
	
	when(uiState.projects())
			.thenReturn(
					List.of(
							project));
	
	when(state.selectedIndex())
			.thenReturn(
					0);
	
	when(desktopIntegrationService.openVSCode(
			path))
			.thenReturn(
					true);
	
	controller.openVSCode();
	
	verify(desktopIntegrationService)
			.openVSCode(
					path);
	
	verify(uiState)
			.showSuccessMessage(
					"Opened demo in VS Code.");
}

@Test
void shouldCopyPopupSelectedProjectPath() {
	
	Path path =
			Path.of(
					"/workspace/demo");
	
	SpringProject project =
			project(
					"demo",
					path);
	
	when(uiState.projects())
			.thenReturn(
					List.of(
							project));
	
	when(state.selectedIndex())
			.thenReturn(
					0);
	
	when(desktopIntegrationService.copyToClipboard(
			path.toString()))
			.thenReturn(
					true);
	
	controller.copyProjectPath();
	
	verify(desktopIntegrationService)
			.copyToClipboard(
					path.toString());
	
	verify(uiState)
			.showSuccessMessage(
					"Copied path for demo");
}

private SpringProject project(
		String name,
		Path path) {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	when(project.name())
			.thenReturn(
					name);
	
	when(project.path())
			.thenReturn(
					path);
	
	return project;
}

@Test
void shouldCommitPopupSelectionToUiState() {
	
	SpringProject firstProject =
			mock(
					SpringProject.class);
	
	SpringProject secondProject =
			mock(
					SpringProject.class);
	
	when(uiState.projects())
			.thenReturn(
					List.of(
							firstProject,
							secondProject));
	
	when(state.selectedIndex())
			.thenReturn(
					1);
	
	boolean selected =
			controller.selectCurrentProject();
	
	assertThat(selected)
			.isTrue();
	
	verify(uiState)
			.selectProject(
					1);
}

@Test
void shouldNotCommitSelectionWhenNoProjectsExist() {
	
	when(uiState.projects())
			.thenReturn(
					List.of());
	
	boolean selected =
			controller.selectCurrentProject();
	
	assertThat(selected)
			.isFalse();
	
	verify(
			uiState,
			never())
			.selectProject(
					anyInt());
}

}