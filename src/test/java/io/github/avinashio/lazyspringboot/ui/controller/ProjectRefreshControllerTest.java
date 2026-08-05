package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ProjectRefreshControllerTest {

private UiState uiState;

private DiscoverProjectsUseCase
		discoverProjectsUseCase;

private ProjectRefreshController controller;

@BeforeEach
void setUp() {
	
	uiState =
			mock(
					UiState.class);
	
	discoverProjectsUseCase =
			mock(
					DiscoverProjectsUseCase.class);
	
	controller =
			new ProjectRefreshController(
					uiState,
					discoverProjectsUseCase);
}

@Test
void shouldRefreshProjects()
		throws IOException {
	
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
	
	when(discoverProjectsUseCase.discover())
			.thenReturn(
					projects);
	
	controller.refresh();
	
	verify(discoverProjectsUseCase)
			.discover();
	
	verify(uiState)
			.refreshProjects(
					projects);
}

@Test
void shouldRefreshWithEmptyProjectList()
		throws IOException {
	
	when(discoverProjectsUseCase.discover())
			.thenReturn(
					List.of());
	
	controller.refresh();
	
	verify(uiState)
			.refreshProjects(
					List.of());
}

@Test
void shouldPropagateDiscoveryFailure()
		throws IOException {
	
	IOException failure =
			new IOException(
					"Unable to scan workspace");
	
	when(discoverProjectsUseCase.discover())
			.thenThrow(
					failure);
	
	assertThatThrownBy(
			controller::refresh)
			.isSameAs(
					failure);
	
	verify(
			uiState,
			never())
			.refreshProjects(
					org.mockito.ArgumentMatchers
							.anyList());
}
}