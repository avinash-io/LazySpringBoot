package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.service.WorkspaceService;
import io.github.avinashio.lazyspringboot.ui.controller.TextInputController;
import io.github.avinashio.lazyspringboot.ui.runtime.PortProvider;
import io.github.avinashio.lazyspringboot.ui.runtime.SpringBootLogParser;
import io.github.avinashio.lazyspringboot.ui.runtime.StatusProvider;
import io.github.avinashio.lazyspringboot.ui.runtime.UptimeProvider;
import io.github.avinashio.lazyspringboot.ui.service.ProjectFilterService;
import io.github.avinashio.lazyspringboot.ui.service.ProjectRuntimeInfoFactory;
import io.github.avinashio.lazyspringboot.ui.service.ProjectSortService;
import io.github.avinashio.lazyspringboot.ui.service.VisibleProjectService;
import io.github.avinashio.lazyspringboot.ui.state.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectPanelTest {

private static final int PANEL_WIDTH = 80;

private UiState uiState;

private TextInputController textInputController;

private ProjectPanel projectPanel;

@BeforeEach
void setUp() {
	
	uiState =
			new UiState();
	
	TextInputState textInputState =
			new TextInputState();
	
	textInputController =
			new TextInputController(
					textInputState);
	
	VisibleProjectService visibleProjectService =
			new VisibleProjectService(
					new ProjectFilterService(),
					new ProjectSortService(),
					projectSortState(),
					textInputController);
	
	ProjectRuntimeInfoFactory projectRuntimeInfoFactory =
			new ProjectRuntimeInfoFactory(
					mock(
							GetProjectProcessUseCase.class),
					new StatusProvider(),
					new UptimeProvider(),
					new PortProvider(
							new SpringBootLogParser()));
	
	projectPanel =
			new ProjectPanel(
					new StatusFormatter(
							mock(
									Spinner.class),
							new TerminalStyle()),
					new ProjectBadgeFormatter(),
					visibleProjectService,
					textInputController,
					projectRuntimeInfoFactory);
}

@Test
void shouldRenderProjects() {
	
	uiState.setProjects(
			projects());
	
	List<String> lines =
			projectPanel.render(
					uiState,
					10,
					PANEL_WIDTH);
	
	assertThat(lines)
			.hasSize(7);
	
	assertThat(lines.get(0))
			.contains("NAME")
			.contains("STATUS")
			.contains("PORT")
			.contains("UPTIME");
	
	assertThat(lines)
			.anyMatch(
					line ->
							line.contains(
									"project-one"));
	
	assertThat(lines)
			.anyMatch(
					line ->
							line.contains(
									"project-five"));
}

@Test
void shouldRenderSeparatorUsingAvailablePanelWidth() {
	
	uiState.setProjects(
			projects());
	
	int panelWidth = 100;
	
	List<String> lines =
			projectPanel.render(
					uiState,
					10,
					panelWidth);
	
	assertThat(lines.get(1))
			.isEqualTo(
					"─".repeat(
							panelWidth - 1));
}

@Test
void shouldRenderSelectedProject() {
	
	uiState.setProjects(
			projects());
	
	uiState.selectProject(
			2);
	
	List<String> lines =
			projectPanel.render(
					uiState,
					10,
					PANEL_WIDTH);
	
	assertThat(lines)
			.anyMatch(
					line ->
							line.contains(
									">")
									&& line.contains(
									"project-three"));
}

@Test
void shouldRenderCompactProjectStatus() {
	
	uiState.setProjects(
			List.of(
					project(
							"demo")));
	
	List<String> lines =
			projectPanel.render(
					uiState,
					10,
					PANEL_WIDTH);
	
	assertThat(lines)
			.hasSize(3);
	
	assertThat(lines.get(2))
			.contains("demo")
			.contains("STOPPED");
}

@Test
void shouldRenderMessageWhenNoProjectsExist() {
	
	List<String> lines =
			projectPanel.render(
					uiState,
					10,
					PANEL_WIDTH);
	
	assertThat(lines)
			.containsExactly(
					" No Spring Boot projects found.");
}

@Test
void shouldFilterProjectsDuringProjectSearch() {
	
	uiState.setProjects(
			projects());
	
	startProjectSearch(
			"three");
	
	List<String> lines =
			projectPanel.render(
					uiState,
					10,
					PANEL_WIDTH);
	
	assertThat(lines)
			.hasSize(3);
	
	assertThat(lines.get(2))
			.contains(
					"project-three");
}

@Test
void shouldNotFilterProjectsDuringDependencySearch() {
	
	uiState.setProjects(
			projects());
	
	textInputController.start(
			TextInputPurpose.DEPENDENCY_SEARCH);
	
	append(
			"three");
	
	List<String> lines =
			projectPanel.render(
					uiState,
					10,
					PANEL_WIDTH);
	
	assertThat(lines)
			.hasSize(7);
}

@Test
void shouldRenderMessageWhenProjectSearchHasNoMatches() {
	
	uiState.setProjects(
			projects());
	
	startProjectSearch(
			"does-not-exist");
	
	List<String> lines =
			projectPanel.render(
					uiState,
					10,
					PANEL_WIDTH);
	
	assertThat(lines)
			.containsExactly(
					" No projects match \"does-not-exist\"");
}

@Test
void shouldRenderOnlyProjectsInsideViewport() {
	
	uiState.setProjects(
			projects());
	
	List<String> lines =
			projectPanel.render(
					uiState,
					3,
					PANEL_WIDTH);
	
	assertThat(lines)
			.hasSize(5);
	
	assertThat(lines)
			.anyMatch(
					line ->
							line.contains(
									"project-five"));
	
	assertThat(lines)
			.anyMatch(
					line ->
							line.contains(
									"project-four"));
	
	assertThat(lines)
			.anyMatch(
					line ->
							line.contains(
									"project-one"));
	
	assertThat(lines)
			.noneMatch(
					line ->
							line.contains(
									"project-three"));
	
	assertThat(lines)
			.noneMatch(
					line ->
							line.contains(
									"project-two"));
}

@Test
void shouldScrollViewportToKeepSelectedProjectVisible() {
	
	uiState.setProjects(
			projects());
	
	uiState.selectProject(
			1);
	
	List<String> lines =
			projectPanel.render(
					uiState,
					3,
					PANEL_WIDTH);
	
	assertThat(lines)
			.hasSize(5);
	
	assertThat(lines)
			.anyMatch(
					line ->
							line.contains(
									">")
									&& line.contains(
									"project-two"));
	
	assertThat(lines)
			.noneMatch(
					line ->
							line.contains(
									"project-five"));
	
	assertThat(lines)
			.noneMatch(
					line ->
							line.contains(
									"project-four"));
}

@Test
void shouldScrollViewportBackWhenSelectionMovesUp() {
	
	uiState.setProjects(
			projects());
	
	uiState.selectProject(
			1);
	
	projectPanel.render(
			uiState,
			3,
			PANEL_WIDTH);
	
	uiState.selectProject(
			4);
	
	List<String> lines =
			projectPanel.render(
					uiState,
					3,
					PANEL_WIDTH);
	
	assertThat(lines)
			.hasSize(5);
	
	assertThat(lines)
			.anyMatch(
					line ->
							line.contains(
									">")
									&& line.contains(
									"project-five"));
	
	assertThat(lines)
			.anyMatch(
					line ->
							line.contains(
									"project-four"));
	
	assertThat(lines)
			.anyMatch(
					line ->
							line.contains(
									"project-one"));
	
	assertThat(lines)
			.noneMatch(
					line ->
							line.contains(
									"project-two"));
}

@Test
void shouldApplyViewportToFilteredProjects() {
	
	uiState.setProjects(
			List.of(
					project(
							"service-one"),
					project(
							"demo"),
					project(
							"service-two"),
					project(
							"other"),
					project(
							"service-three")));
	
	startProjectSearch(
			"service");
	
	uiState.selectProject(
			4);
	
	List<String> lines =
			projectPanel.render(
					uiState,
					2,
					PANEL_WIDTH);
	
	assertThat(lines)
			.hasSize(4);
	
	assertThat(lines)
			.anyMatch(
					line ->
							line.contains(
									">")
									&& line.contains(
									"service-three"));
	
	assertThat(lines)
			.noneMatch(
					line ->
							line.contains(
									"demo"));
	
	assertThat(lines)
			.noneMatch(
					line ->
							line.contains(
									"other"));
}

private void startProjectSearch(
		String query) {
	
	textInputController.start(
			TextInputPurpose.PROJECT_SEARCH);
	
	append(
			query);
}

private void append(
		String value) {
	
	for (char character :
			value.toCharArray()) {
		
		textInputController.append(
				character);
	}
}

private ProjectSortState projectSortState() {
	
	WorkspaceService workspaceService =
			mock(
					WorkspaceService.class);
	
	when(
			workspaceService.projectSortMode())
			.thenReturn(
					ProjectSortMode.NAME_ASC);
	
	return new ProjectSortState(
			workspaceService);
}

private List<SpringProject> projects() {
	
	return List.of(
			project(
					"project-one"),
			project(
					"project-two"),
			project(
					"project-three"),
			project(
					"project-four"),
			project(
					"project-five"));
}

private SpringProject project(
		String name) {
	
	return new SpringProject(
			name,
			Path.of(
					"/workspace/"
							+ name),
			BuildTool.MAVEN,
			new ProjectMetadata(
					"com.example",
					name,
					"4.1.0",
					"26",
					List.of()));
}
}