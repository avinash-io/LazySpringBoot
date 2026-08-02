package io.github.avinashio.lazyspringboot.infrastructure.process;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectProcessCommandFactoryTest {

private final ProjectProcessCommandFactory
		commandFactory =
		new ProjectProcessCommandFactory();

@TempDir
Path temporaryDirectory;

@Test
void shouldUseMavenWrapperWhenAvailable()
		throws IOException {
	
	Files.createFile(
			temporaryDirectory.resolve(
					"mvnw"));
	
	List<String> command =
			commandFactory.create(
					mockProject(
							BuildTool.MAVEN));
	
	assertThat(command)
			.containsExactly(
					"sh",
					"./mvnw",
					"spring-boot:run");
}

@Test
void shouldUseSystemMavenWhenWrapperIsMissing() {
	
	List<String> command =
			commandFactory.create(
					mockProject(
							BuildTool.MAVEN));
	
	assertThat(command)
			.containsExactly(
					"mvn",
					"spring-boot:run");
}

@Test
void shouldUseGradleWrapperWhenAvailable()
		throws IOException {
	
	Files.createFile(
			temporaryDirectory.resolve(
					"gradlew"));
	
	List<String> command =
			commandFactory.create(
					mockProject(
							BuildTool.GRADLE));
	
	assertThat(command)
			.containsExactly(
					"sh",
					"./gradlew",
					"bootRun");
}

@Test
void shouldUseSystemGradleWhenWrapperIsMissing() {
	
	List<String> command =
			commandFactory.create(
					mockProject(
							BuildTool.GRADLE));
	
	assertThat(command)
			.containsExactly(
					"gradle",
					"bootRun");
}

@Test
void shouldUseGradleWrapperForKotlinDsl()
		throws IOException {
	
	Files.createFile(
			temporaryDirectory.resolve(
					"gradlew"));
	
	List<String> command =
			commandFactory.create(
					mockProject(
							BuildTool.GRADLE_KOTLIN));
	
	assertThat(command)
			.containsExactly(
					"sh",
					"./gradlew",
					"bootRun");
}

@Test
void shouldUseSystemGradleForKotlinDslWhenWrapperIsMissing() {
	
	List<String> command =
			commandFactory.create(
					mockProject(
							BuildTool.GRADLE_KOTLIN));
	
	assertThat(command)
			.containsExactly(
					"gradle",
					"bootRun");
}

private SpringProject mockProject(
		BuildTool buildTool) {
	
	SpringProject project =
			mock(SpringProject.class);
	
	when(project.buildTool())
			.thenReturn(buildTool);
	
	when(project.path())
			.thenReturn(
					temporaryDirectory);
	
	return project;
}
}