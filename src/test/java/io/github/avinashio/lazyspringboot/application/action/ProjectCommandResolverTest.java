package io.github.avinashio.lazyspringboot.application.action;

import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectCommand;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProjectCommandResolverTest {

private final ProjectCommandResolver resolver =
		new ProjectCommandResolver();
@TempDir
Path temporaryDirectory;

@Test
void shouldResolveMavenBuildCommand()
		throws Exception {
	
	Files.createFile(
			temporaryDirectory.resolve("mvnw"));
	
	ProjectCommand command =
			resolver.resolve(
					project(),
					ProjectAction.BUILD);
	
	assertThat(command.arguments())
			.containsExactly(
					"sh",
					"./mvnw",
					"clean",
					"package");
	
	assertThat(command.workingDirectory())
			.isEqualTo(
					temporaryDirectory);
}

@Test
void shouldResolveMavenTestCommand()
		throws Exception {
	
	Files.createFile(
			temporaryDirectory.resolve("mvnw"));
	
	ProjectCommand command =
			resolver.resolve(
					project(),
					ProjectAction.TEST);
	
	assertThat(command.arguments())
			.containsExactly(
					"sh",
					"./mvnw",
					"test");
	
	assertThat(command.workingDirectory())
			.isEqualTo(
					temporaryDirectory);
}

@Test
void shouldRejectRunAsCommandAction() {
	
	assertThatThrownBy(
			() ->
					resolver.resolve(
							project(),
							ProjectAction.RUN))
			.isInstanceOf(
					IllegalArgumentException.class)
			.hasMessage(
					"Action is not a command action: RUN");
}

@Test
void shouldRejectViewLogsAsCommandAction() {
	
	assertThatThrownBy(
			() ->
					resolver.resolve(
							project(),
							ProjectAction.VIEW_LOGS))
			.isInstanceOf(
					IllegalArgumentException.class)
			.hasMessage(
					"Action is not a command action: VIEW_LOGS");
}

@Test
void shouldRejectRestartAsCommandAction() {
	
	assertThatThrownBy(
			() ->
					resolver.resolve(
							project(),
							ProjectAction.RESTART))
			.isInstanceOf(
					IllegalArgumentException.class)
			.hasMessage(
					"Action is not a command action: RESTART");
}

@Test
void shouldRejectStopAsCommandAction() {
	
	assertThatThrownBy(
			() ->
					resolver.resolve(
							project(),
							ProjectAction.STOP))
			.isInstanceOf(
					IllegalArgumentException.class)
			.hasMessage(
					"Action is not a command action: STOP");
}

@Test
void shouldFallbackToInstalledMaven()
		throws Exception {
	
	ProjectCommand command =
			resolver.resolve(
					project(),
					ProjectAction.TEST);
	
	assertThat(command.arguments())
			.containsExactly(
					"mvn",
					"test");
	
	assertThat(command.workingDirectory())
			.isEqualTo(
					temporaryDirectory);
}

private SpringProject project() {
	
	return project(
			BuildTool.MAVEN);
}

private SpringProject project(
		BuildTool buildTool) {
	
	return new SpringProject(
			"demo",
			temporaryDirectory,
			buildTool,
			new ProjectMetadata(
					"com.example",
					"demo",
					"4.1.0",
					"26",
					List.of()));
}

@Test
void shouldResolveGradleBuildCommand()
		throws Exception {
	
	Files.createFile(
			temporaryDirectory.resolve(
					"gradlew"));
	
	ProjectCommand command =
			resolver.resolve(
					project(
							BuildTool.GRADLE),
					ProjectAction.BUILD);
	
	assertThat(command.arguments())
			.containsExactly(
					"sh",
					"./gradlew",
					"clean",
					"build");
	
	assertThat(command.workingDirectory())
			.isEqualTo(
					temporaryDirectory);
}

@Test
void shouldResolveGradleTestCommand()
		throws Exception {
	
	Files.createFile(
			temporaryDirectory.resolve(
					"gradlew"));
	
	ProjectCommand command =
			resolver.resolve(
					project(
							BuildTool.GRADLE),
					ProjectAction.TEST);
	
	assertThat(command.arguments())
			.containsExactly(
					"sh",
					"./gradlew",
					"test");
}

@Test
void shouldResolveGradleInstallAsBuild()
		throws Exception {
	
	Files.createFile(
			temporaryDirectory.resolve(
					"gradlew"));
	
	ProjectCommand command =
			resolver.resolve(
					project(
							BuildTool.GRADLE),
					ProjectAction.INSTALL);
	
	assertThat(command.arguments())
			.containsExactly(
					"sh",
					"./gradlew",
					"build");
}

@Test
void shouldFallbackToInstalledGradle() {
	
	ProjectCommand command =
			resolver.resolve(
					project(
							BuildTool.GRADLE),
					ProjectAction.TEST);
	
	assertThat(command.arguments())
			.containsExactly(
					"gradle",
					"test");
}

@Test
void shouldResolveGradleKotlinBuildCommand()
		throws Exception {
	
	Files.createFile(
			temporaryDirectory.resolve(
					"gradlew"));
	
	ProjectCommand command =
			resolver.resolve(
					project(
							BuildTool.GRADLE_KOTLIN),
					ProjectAction.BUILD);
	
	assertThat(command.arguments())
			.containsExactly(
					"sh",
					"./gradlew",
					"clean",
					"build");
}


}