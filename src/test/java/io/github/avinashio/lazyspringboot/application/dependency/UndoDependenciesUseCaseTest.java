package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.gradle.GradleBuildBackupRestorer;
import io.github.avinashio.lazyspringboot.infrastructure.gradle.GradleDependencyWriter;
import io.github.avinashio.lazyspringboot.infrastructure.gradle.GradleProjectDependencyWriter;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenDependencyParser;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenPomBackupRestorer;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenPomDependencyWriter;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenProjectDependencyWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UndoDependenciesUseCaseTest {

@TempDir
Path temporaryDirectory;

@Test
void shouldReportUndoAvailableWhenBackupExists()
		throws Exception {
	
	Path pomPath =
			temporaryDirectory.resolve(
					"pom.xml");
	
	Files.writeString(
			pomPath,
			"<project>updated</project>");
	
	Files.writeString(
			temporaryDirectory.resolve(
					"pom.xml.lazyspringboot.bak"),
			"<project>original</project>");
	
	UndoDependenciesUseCase useCase =
			createMavenUseCase();
	
	assertThat(
			useCase.canUndo(
					project(
							BuildTool.MAVEN)))
			.isTrue();
}

@Test
void shouldRestoreProjectPom()
		throws Exception {
	
	Path pomPath =
			temporaryDirectory.resolve(
					"pom.xml");
	
	Files.writeString(
			pomPath,
			"<project>updated</project>");
	
	Files.writeString(
			temporaryDirectory.resolve(
					"pom.xml.lazyspringboot.bak"),
			"<project>original</project>");
	
	UndoDependenciesUseCase useCase =
			createMavenUseCase();
	
	useCase.undo(
			project(
					BuildTool.MAVEN));
	
	assertThat(
			Files.readString(
					pomPath))
			.isEqualTo(
					"<project>original</project>");
}

@Test
void shouldRestoreGradleBuildFile()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle");
	
	Files.writeString(
			buildFile,
			"dependencies {\n"
					+ "    implementation 'com.example:updated'\n"
					+ "}\n");
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle.lazyspringboot.bak"),
			"dependencies {\n"
					+ "}\n");
	
	UndoDependenciesUseCase useCase =
			createGradleUseCase();
	
	SpringProject project =
			project(
					BuildTool.GRADLE);
	
	assertThat(
			useCase.canUndo(
					project))
			.isTrue();
	
	useCase.undo(
			project);
	
	assertThat(
			Files.readString(
					buildFile))
			.isEqualTo(
					"dependencies {\n"
							+ "}\n");
	
	assertThat(
			useCase.canUndo(
					project))
			.isFalse();
}

@Test
void shouldRestoreGradleKotlinBuildFile()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle.kts");
	
	Files.writeString(
			buildFile,
			"dependencies {\n"
					+ "    implementation(\"com.example:updated\")\n"
					+ "}\n");
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle.kts.lazyspringboot.bak"),
			"dependencies {\n"
					+ "}\n");
	
	UndoDependenciesUseCase useCase =
			createGradleUseCase();
	
	SpringProject project =
			project(
					BuildTool.GRADLE_KOTLIN);
	
	assertThat(
			useCase.canUndo(
					project))
			.isTrue();
	
	useCase.undo(
			project);
	
	assertThat(
			Files.readString(
					buildFile))
			.isEqualTo(
					"dependencies {\n"
							+ "}\n");
	
	assertThat(
			useCase.canUndo(
					project))
			.isFalse();
}

private UndoDependenciesUseCase createMavenUseCase() {
	
	MavenProjectDependencyWriter writer =
			new MavenProjectDependencyWriter(
					new MavenPomDependencyWriter(
							new MavenDependencyParser()),
					new MavenPomBackupRestorer());
	
	return new UndoDependenciesUseCase(
			List.of(
					writer));
}

private UndoDependenciesUseCase createGradleUseCase() {
	
	GradleBuildBackupRestorer backupRestorer =
			new GradleBuildBackupRestorer();
	
	GradleProjectDependencyWriter writer =
			new GradleProjectDependencyWriter(
					new GradleDependencyWriter(
							backupRestorer),
					backupRestorer);
	
	return new UndoDependenciesUseCase(
			List.of(
					writer));
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
}