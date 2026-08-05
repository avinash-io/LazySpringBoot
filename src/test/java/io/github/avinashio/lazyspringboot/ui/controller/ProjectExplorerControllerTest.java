package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.state.ProjectExplorerState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProjectExplorerControllerTest {

private ProjectExplorerState state;

private UiState uiState;

private ProjectExplorerController controller;

@BeforeEach
void setUp() {
	
	state =
			mock(
					ProjectExplorerState.class);
	
	uiState =
			mock(
					UiState.class);
	
	controller =
			new ProjectExplorerController(
					state,
					uiState);
}

@Test
void shouldOpenProjectExplorer() {
	
	controller.open();
	
	verify(state)
			.openExplorer();
}

@Test
void shouldCloseProjectExplorer() {
	
	controller.close();
	
	verify(state)
			.close();
}

@Test
void shouldReportProjectExplorerAsOpen() {
	
	when(state.open())
			.thenReturn(
					true);
	
	assertThat(controller.isOpen())
			.isTrue();
	
	verify(state)
			.open();
}

@Test
void shouldReportProjectExplorerAsClosed() {
	
	when(state.open())
			.thenReturn(
					false);
	
	assertThat(controller.isOpen())
			.isFalse();
	
	verify(state)
			.open();
}

@Test
void shouldReturnProjects() {
	
	SpringProject firstProject =
			mock(
					SpringProject.class);
	
	SpringProject secondProject =
			mock(
					SpringProject.class);
	
	List<SpringProject> projects =
			List.of(
					firstProject,
					secondProject);
	
	when(uiState.projects())
			.thenReturn(
					projects);
	
	assertThat(controller.projects())
			.containsExactly(
					firstProject,
					secondProject);
	
	verify(uiState)
			.projects();
}

@Test
void shouldReturnEmptyProjectList() {
	
	when(uiState.projects())
			.thenReturn(
					List.of());
	
	assertThat(controller.projects())
			.isEmpty();
	
	verify(uiState)
			.projects();
}

@Test
void shouldReturnSelectedProject() {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	when(uiState.selectedProject())
			.thenReturn(
					project);
	
	assertThat(controller.selectedProject())
			.isSameAs(
					project);
	
	verify(uiState)
			.selectedProject();
}

@Test
void shouldReturnNullWhenNoProjectIsSelected() {
	
	when(uiState.selectedProject())
			.thenReturn(
					null);
	
	assertThat(controller.selectedProject())
			.isNull();
	
	verify(uiState)
			.selectedProject();
}
}