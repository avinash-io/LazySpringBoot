package io.github.avinashio.lazyspringboot.infrastructure.gradle;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GradleProjectDependencyWriterTest {

@TempDir
Path temporaryDirectory;

@Test
void shouldWriteToGroovyBuildFile()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle");
	
	Files.writeString(
			buildFile,
			"""
					dependencies {
					}
					""");
	
	GradleProjectDependencyWriter writer =
			createWriter();
	
	writer.addDependencies(
			project(
					BuildTool.GRADLE),
			List.of(
					new DependencyCoordinate(
							"org.postgresql",
							"postgresql")));
	
	assertThat(
			Files.readString(
					buildFile))
			.contains(
					"implementation 'org.postgresql:postgresql'");
}

@Test
void shouldWriteToKotlinBuildFile()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle.kts");
	
	Files.writeString(
			buildFile,
			"""
					dependencies {
					}
					""");
	
	GradleProjectDependencyWriter writer =
			createWriter();
	
	writer.addDependencies(
			project(
					BuildTool.GRADLE_KOTLIN),
			List.of(
					new DependencyCoordinate(
							"org.postgresql",
							"postgresql")));
	
	assertThat(
			Files.readString(
					buildFile))
			.contains(
					"implementation(\"org.postgresql:postgresql\")");
}

@Test
void shouldReportUndoAvailableForGradleProject()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle");
	
	Files.writeString(
			buildFile,
			"dependencies {\n}\n");
	
	GradleProjectDependencyWriter writer =
			createWriter();
	
	writer.addDependencies(
			project(
					BuildTool.GRADLE),
			List.of(
					new DependencyCoordinate(
							"org.postgresql",
							"postgresql")));
	
	assertThat(
			writer.canUndo(
					project(
							BuildTool.GRADLE)))
			.isTrue();
}

@Test
void shouldUndoGradleDependencyChange()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle");
	
	String original =
			"""
					dependencies {
					}
					""";
	
	Files.writeString(
			buildFile,
			original);
	
	GradleProjectDependencyWriter writer =
			createWriter();
	
	SpringProject project =
			project(
					BuildTool.GRADLE);
	
	writer.addDependencies(
			project,
			List.of(
					new DependencyCoordinate(
							"org.postgresql",
							"postgresql")));
	
	writer.undo(
			project);
	
	assertThat(
			Files.readString(
					buildFile))
			.isEqualTo(
					original);
	
	assertThat(
			writer.canUndo(
					project))
			.isFalse();
}

private GradleProjectDependencyWriter createWriter() {
	
	GradleBuildBackupRestorer backupRestorer =
			new GradleBuildBackupRestorer();
	
	GradleDependencyWriter dependencyWriter =
			new GradleDependencyWriter(
					new GradleDependencyParser(),
					backupRestorer);
	
	return new GradleProjectDependencyWriter(
			dependencyWriter,
			backupRestorer);
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