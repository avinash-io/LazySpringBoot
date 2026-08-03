package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectDetailsPanelTest {

@Test
void shouldShowRuntimeDetailsForRunningProcess() {
	
	GetProjectProcessUseCase getProjectProcessUseCase =
			mock(
					GetProjectProcessUseCase.class);
	
	StatusFormatter statusFormatter =
			mock(
					StatusFormatter.class);
	
	SpringProject project =
			project();
	
	ProjectProcess process =
			new ProjectProcess(
					"test-app",
					ProjectProcessStatus.RUNNING,
					List.of(),
					null,
					12345L,
					Instant.now()
							.minusSeconds(30),
					null);
	
	when(getProjectProcessUseCase.get(
			project))
			.thenReturn(
					Optional.of(
							process));
	
	when(statusFormatter.format(
			ProjectProcessStatus.RUNNING))
			.thenReturn(
					"[✓] RUNNING");
	
	ProjectDetailsPanel panel =
			createPanel(
					statusFormatter,
					getProjectProcessUseCase);
	
	List<String> lines =
			panel.render(
					project);
	
	assertThat(lines)
			.anySatisfy(line ->
								assertThat(line)
										.contains(
												"Status",
												"[✓] RUNNING"));
	
	assertThat(lines)
			.anySatisfy(line ->
								assertThat(line)
										.contains(
												"PID",
												"12345"));
	
	assertThat(lines)
			.anySatisfy(line ->
								assertThat(line)
										.contains(
												"Started"));
	
	assertThat(lines)
			.anySatisfy(line ->
								assertThat(line)
										.contains(
												"Uptime"));
	
	assertThat(lines)
			.noneSatisfy(line ->
								 assertThat(line)
										 .contains(
												 "Exit Code"));
}

@Test
void shouldHideRuntimeDetailsForStoppedProcess() {
	
	GetProjectProcessUseCase getProjectProcessUseCase =
			mock(
					GetProjectProcessUseCase.class);
	
	StatusFormatter statusFormatter =
			mock(
					StatusFormatter.class);
	
	SpringProject project =
			project();
	
	ProjectProcess process =
			new ProjectProcess(
					"test-app",
					ProjectProcessStatus.STOPPED,
					List.of(),
					143,
					12345L,
					Instant.parse(
							"2026-07-20T10:00:00Z"),
					Instant.parse(
							"2026-07-20T10:15:30Z"));
	
	when(getProjectProcessUseCase.get(
			project))
			.thenReturn(
					Optional.of(
							process));
	
	when(statusFormatter.format(
			ProjectProcessStatus.STOPPED))
			.thenReturn(
					"[ ] STOPPED");
	
	ProjectDetailsPanel panel =
			createPanel(
					statusFormatter,
					getProjectProcessUseCase);
	
	List<String> lines =
			panel.render(
					project);
	
	assertThat(lines)
			.anySatisfy(line ->
								assertThat(line)
										.contains(
												"Status",
												"[ ] STOPPED"));
	
	assertThat(lines)
			.noneSatisfy(line ->
								 assertThat(line)
										 .contains(
												 "PID"));
	
	assertThat(lines)
			.noneSatisfy(line ->
								 assertThat(line)
										 .contains(
												 "Started"));
	
	assertThat(lines)
			.noneSatisfy(line ->
								 assertThat(line)
										 .contains(
												 "Uptime"));
	
	assertThat(lines)
			.anySatisfy(line ->
								assertThat(line)
										.contains(
												"Exit Code",
												"143"));
}

@Test
void shouldHideRuntimeDetailsForFailedProcess() {
	
	GetProjectProcessUseCase getProjectProcessUseCase =
			mock(
					GetProjectProcessUseCase.class);
	
	StatusFormatter statusFormatter =
			mock(
					StatusFormatter.class);
	
	SpringProject project =
			project();
	
	ProjectProcess process =
			new ProjectProcess(
					"test-app",
					ProjectProcessStatus.FAILED,
					List.of(),
					1,
					12345L,
					Instant.parse(
							"2026-07-20T10:00:00Z"),
					Instant.parse(
							"2026-07-20T10:02:05Z"));
	
	when(getProjectProcessUseCase.get(
			project))
			.thenReturn(
					Optional.of(
							process));
	
	when(statusFormatter.format(
			ProjectProcessStatus.FAILED))
			.thenReturn(
					"[✗] FAILED");
	
	ProjectDetailsPanel panel =
			createPanel(
					statusFormatter,
					getProjectProcessUseCase);
	
	List<String> lines =
			panel.render(
					project);
	
	assertThat(lines)
			.anySatisfy(line ->
								assertThat(line)
										.contains(
												"Status",
												"[✗] FAILED"));
	
	assertThat(lines)
			.noneSatisfy(line ->
								 assertThat(line)
										 .contains(
												 "PID"));
	
	assertThat(lines)
			.noneSatisfy(line ->
								 assertThat(line)
										 .contains(
												 "Started"));
	
	assertThat(lines)
			.noneSatisfy(line ->
								 assertThat(line)
										 .contains(
												 "Uptime"));
	
	assertThat(lines)
			.anySatisfy(line ->
								assertThat(line)
										.contains(
												"Exit Code",
												"1"));
}

private SpringProject project() {
	
	SpringProject project =
			mock(
					SpringProject.class);
	
	ProjectMetadata metadata =
			new ProjectMetadata(
					"com.example",
					"test-app",
					"4.1.0",
					"21",
					List.of());
	
	when(project.metadata())
			.thenReturn(
					metadata);
	
	when(project.buildTool())
			.thenReturn(
					BuildTool.MAVEN);
	
	when(project.path())
			.thenReturn(
					Path.of(
							"/tmp/test-app"));
	
	return project;
}

private ProjectDetailsPanel createPanel(
		StatusFormatter statusFormatter,
		GetProjectProcessUseCase getProjectProcessUseCase) {
	
	return new ProjectDetailsPanel(
			new TextFormatter(),
			statusFormatter,
			new DurationFormatter(),
			getProjectProcessUseCase);
}
}