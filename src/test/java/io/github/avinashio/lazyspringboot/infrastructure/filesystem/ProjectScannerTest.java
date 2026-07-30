package io.github.avinashio.lazyspringboot.infrastructure.filesystem;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenDependencyParser;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenProjectInspector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectScannerTest {

private final MavenProjectInspector
		mavenProjectInspector =
		new MavenProjectInspector(
				new MavenDependencyParser());

private final BuildToolDetector
		buildToolDetector =
		new BuildToolDetector();

private final ProjectScanner
		projectScanner =
		new ProjectScanner(
				mavenProjectInspector,
				buildToolDetector);

@TempDir
Path temporaryDirectory;

@Test
void shouldDiscoverSpringBootProject() throws IOException {
	createSpringBootProject("demo");
	
	List<SpringProject> projects = projectScanner.scan(temporaryDirectory);
	
	assertThat(projects)
			.extracting(SpringProject::name)
			.containsExactly("demo");
}

@Test
void shouldIgnoreDirectoryWithoutPomFile() throws IOException {
	Files.createDirectory(temporaryDirectory.resolve("notes"));
	
	List<SpringProject> projects = projectScanner.scan(temporaryDirectory);
	
	assertThat(projects).isEmpty();
}

@Test
void shouldIgnoreNonSpringBootMavenProject() throws IOException {
	createMavenProject("plain-maven");
	
	List<SpringProject> projects = projectScanner.scan(temporaryDirectory);
	
	assertThat(projects).isEmpty();
}

@Test
void shouldDiscoverMultipleSpringBootProjects() throws IOException {
	createSpringBootProject("cv-api");
	createSpringBootProject("payment-service");
	
	List<SpringProject> projects = projectScanner.scan(temporaryDirectory);
	
	assertThat(projects)
			.extracting(SpringProject::name)
			.containsExactlyInAnyOrder("cv-api", "payment-service");
}

private void createSpringBootProject(String name) throws IOException {
	Path projectDirectory =
			Files.createDirectory(temporaryDirectory.resolve(name));
	
	String pom =
			"""
					<?xml version="1.0" encoding="UTF-8"?>
					<project>
					  <modelVersion>4.0.0</modelVersion>
					  <parent>
					    <groupId>org.springframework.boot</groupId>
					    <artifactId>spring-boot-starter-parent</artifactId>
					    <version>4.1.0</version>
					  </parent>
					  <groupId>com.example</groupId>
					  <artifactId>%s</artifactId>
					</project>
					"""
					.formatted(name);
	
	Files.writeString(projectDirectory.resolve("pom.xml"), pom);
}

private void createMavenProject(String name) throws IOException {
	Path projectDirectory =
			Files.createDirectory(temporaryDirectory.resolve(name));
	
	String pom =
			"""
					<?xml version="1.0" encoding="UTF-8"?>
					<project>
					  <modelVersion>4.0.0</modelVersion>
					  <groupId>com.example</groupId>
					  <artifactId>%s</artifactId>
					  <version>1.0.0</version>
					</project>
					"""
					.formatted(name);
	
	Files.writeString(projectDirectory.resolve("pom.xml"), pom);
}
}