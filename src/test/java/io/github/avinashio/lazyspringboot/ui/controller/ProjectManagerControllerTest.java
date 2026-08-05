package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.state.ProjectManagerState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProjectManagerControllerTest {

private ProjectManagerState state;

private UiState uiState;

private ProjectManagerController controller;

@BeforeEach
void setUp() {
	
	state =
			mock(
					ProjectManagerState.class);
	
	uiState =
			mock(
					UiState.class);
	
	controller =
			new ProjectManagerController(
					state,
					uiState);
}

@Test
void shouldOpenProjectManager() {
	
	controller.open();
	
	verify(state)
			.open();
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