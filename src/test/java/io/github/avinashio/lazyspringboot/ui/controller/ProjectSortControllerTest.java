package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.ui.state.ProjectSortState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class ProjectSortControllerTest {

private ProjectSortState projectSortState;

private UiState uiState;

private ProjectSortController controller;

@BeforeEach
void setUp() {
	
	projectSortState =
			mock(
					ProjectSortState.class);
	
	uiState =
			mock(
					UiState.class);
	
	controller =
			new ProjectSortController(
					projectSortState,
					uiState);
}

@Test
void shouldCycleProjectSortMode()
		throws IOException {
	
	controller.cycle();
	
	verify(projectSortState)
			.next();
	
	verify(
			uiState,
			never())
			.showErrorMessage(
					org.mockito.ArgumentMatchers
							.anyString());
}

@Test
void shouldShowErrorWhenProjectSortModeCannotBeSaved()
		throws IOException {
	
	doThrow(
			new IOException(
					"Permission denied"))
			.when(projectSortState)
			.next();
	
	controller.cycle();
	
	verify(projectSortState)
			.next();
	
	verify(uiState)
			.showErrorMessage(
					"Unable to save project sort mode: "
							+ "Permission denied");
}

@Test
void shouldHandleIOExceptionWithoutMessage()
		throws IOException {
	
	doThrow(
			new IOException())
			.when(projectSortState)
			.next();
	
	controller.cycle();
	
	verify(uiState)
			.showErrorMessage(
					"Unable to save project sort mode: null");
}
}