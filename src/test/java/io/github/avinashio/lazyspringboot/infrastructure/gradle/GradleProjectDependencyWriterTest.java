package io.github.avinashio.lazyspringboot.infrastructure.gradle;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyDeclaration;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyScope;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenDependencyParser;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenPomDependencyWriter;
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
					declaration(
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
					declaration(
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
					declaration(
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
					declaration(
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

private DependencyDeclaration declaration(
		String groupId,
		String artifactId) {
	
	return new DependencyDeclaration(
			new DependencyCoordinate(
					groupId,
					artifactId),
			DependencyScope.COMPILE);
}

@Test
void shouldWriteRuntimeScope()
		throws Exception {
	
	Path pom =
			temporaryDirectory.resolve(
					"pom.xml");
	
	Files.writeString(
			pom,
			"""
					<project>
						<dependencies>
						</dependencies>
					</project>
					""");
	
	MavenPomDependencyWriter writer =
			new MavenPomDependencyWriter(
					new MavenDependencyParser());
	
	writer.addDependencies(
			pom,
			List.of(
					new DependencyDeclaration(
							new DependencyCoordinate(
									"org.postgresql",
									"postgresql"),
							DependencyScope.RUNTIME)));
	
	assertThat(
			Files.readString(
					pom))
			.contains(
					"<scope>runtime</scope>");
}

@Test
void shouldWriteCompileOnlyAsOptional()
		throws Exception {
	
	Path pom =
			temporaryDirectory.resolve(
					"pom.xml");
	
	Files.writeString(
			pom,
			"""
					<project>
						<dependencies>
						</dependencies>
					</project>
					""");
	
	MavenPomDependencyWriter writer =
			new MavenPomDependencyWriter(
					new MavenDependencyParser());
	
	writer.addDependencies(
			pom,
			List.of(
					new DependencyDeclaration(
							new DependencyCoordinate(
									"org.projectlombok",
									"lombok"),
							DependencyScope.COMPILE_ONLY)));
	
	assertThat(
			Files.readString(
					pom))
			.contains(
					"<artifactId>lombok</artifactId>")
			.contains(
					"<optional>true</optional>");
}

}