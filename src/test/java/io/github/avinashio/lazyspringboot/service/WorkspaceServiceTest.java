package io.github.avinashio.lazyspringboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.avinashio.lazyspringboot.ui.state.ProjectSortMode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class WorkspaceServiceTest {

@TempDir
Path tempDirectory;

private String originalUserHome;

@BeforeEach
void setUp() {
	
	originalUserHome =
			System.getProperty(
					"user.home");
	
	System.setProperty(
			"user.home",
			tempDirectory.toString());
}

@AfterEach
void tearDown() {
	
	if (originalUserHome == null) {
		
		System.clearProperty(
				"user.home");
		
		return;
	}
	
	System.setProperty(
			"user.home",
			originalUserHome);
}

@Test
void shouldUseCurrentDirectoryAsDefaultWorkspace()
		throws Exception {
	
	WorkspaceService service =
			new WorkspaceService(
					new ObjectMapper());
	
	assertThat(service.workspace())
			.isEqualTo(
					Path.of("")
							.toAbsolutePath()
							.normalize());
}

@Test
void shouldReturnChangedWorkspace()
		throws Exception {
	
	WorkspaceService service =
			new WorkspaceService(
					new ObjectMapper());
	
	Path workspace =
			tempDirectory.resolve(
					"workspace");
	
	service.changeWorkspace(
			workspace);
	
	assertThat(service.workspace())
			.isEqualTo(
					workspace
							.toAbsolutePath()
							.normalize());
}

@Test
void shouldRestorePersistedWorkspace()
		throws Exception {
	
	ObjectMapper objectMapper =
			new ObjectMapper();
	
	WorkspaceService firstService =
			new WorkspaceService(
					objectMapper);
	
	Path workspace =
			tempDirectory.resolve(
					"projects");
	
	firstService.changeWorkspace(
			workspace);
	
	WorkspaceService restoredService =
			new WorkspaceService(
					objectMapper);
	
	assertThat(restoredService.workspace())
			.isEqualTo(
					workspace
							.toAbsolutePath()
							.normalize());
}

@Test
void shouldPreserveSortModeWhenWorkspaceChanges()
		throws Exception {
	
	WorkspaceService service =
			new WorkspaceService(
					new ObjectMapper());
	
	service.changeProjectSortMode(
			ProjectSortMode.NAME_DESC);
	
	service.changeWorkspace(
			tempDirectory.resolve(
					"workspace"));
	
	assertThat(service.projectSortMode())
			.isEqualTo(
					ProjectSortMode.NAME_DESC);
}
}