package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
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
			createUseCase();
	
	assertThat(
			useCase.canUndo(
					project()))
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
			createUseCase();
	
	useCase.undo(
			project());
	
	assertThat(
			Files.readString(
					pomPath))
			.isEqualTo(
					"<project>original</project>");
}

private UndoDependenciesUseCase createUseCase() {
	
	MavenProjectDependencyWriter writer =
			new MavenProjectDependencyWriter(
					new MavenPomDependencyWriter(
							new MavenDependencyParser()),
					new MavenPomBackupRestorer());
	
	return new UndoDependenciesUseCase(
			List.of(
					writer));
}

private SpringProject project() {
	
	return new SpringProject(
			"demo",
			temporaryDirectory,
			BuildTool.MAVEN,
			new ProjectMetadata(
					"com.example",
					"demo",
					"4.1.0",
					"26",
					List.of()));
}
}