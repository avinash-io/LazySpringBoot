package io.github.avinashio.lazyspringboot.application.project;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.gradle.GradleDependencyParser;
import io.github.avinashio.lazyspringboot.infrastructure.gradle.GradleProjectInspector;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenDependencyParser;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenProjectInspector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshSelectedProjectUseCaseTest {

@TempDir
Path temporaryDirectory;

@Test
void shouldRefreshMavenProject()
		throws Exception {
	
	Path pomPath =
			temporaryDirectory.resolve(
					"pom.xml");
	
	Files.writeString(
			pomPath,
			"""
					<project>
					  <modelVersion>4.0.0</modelVersion>
					  <groupId>com.example</groupId>
					  <artifactId>demo</artifactId>
					  <version>1.0.0</version>
					</project>
					""");
	
	MavenProjectInspector inspector =
			new MavenProjectInspector(
					new MavenDependencyParser());
	
	RefreshSelectedProjectUseCase useCase =
			new RefreshSelectedProjectUseCase(
					List.of(
							inspector));
	
	SpringProject original =
			new SpringProject(
					"demo",
					temporaryDirectory,
					BuildTool.MAVEN,
					new ProjectMetadata(
							"com.example",
							"demo",
							null,
							null,
							List.of()));
	
	Files.writeString(
			pomPath,
			"""
					<project>
					  <modelVersion>4.0.0</modelVersion>
					  <groupId>com.example</groupId>
					  <artifactId>demo-updated</artifactId>
					  <version>1.0.0</version>
					</project>
					""");
	
	SpringProject refreshed =
			useCase.refresh(
					original);
	
	assertThat(
			refreshed.metadata()
					.artifactId())
			.isEqualTo(
					"demo-updated");
}

@Test
void shouldRefreshGradleProject()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle");
	
	Files.writeString(
			buildFile,
			"""
					plugins {
						id 'java'
						id 'org.springframework.boot' version '4.1.0'
					}
					
					group = 'com.example'
					""");
	
	GradleProjectInspector inspector =
			new GradleProjectInspector(
					new GradleDependencyParser());
	
	RefreshSelectedProjectUseCase useCase =
			new RefreshSelectedProjectUseCase(
					List.of(
							inspector));
	
	SpringProject original =
			new SpringProject(
					"demo",
					temporaryDirectory,
					BuildTool.GRADLE,
					new ProjectMetadata(
							"com.example",
							"demo",
							"4.1.0",
							null,
							List.of()));
	
	Files.writeString(
			buildFile,
			"""
					plugins {
						id 'java'
						id 'org.springframework.boot' version '4.2.0'
					}
					
					group = 'io.github.avinashio'
					""");
	
	SpringProject refreshed =
			useCase.refresh(
					original);
	
	assertThat(
			refreshed.metadata()
					.groupId())
			.isEqualTo(
					"io.github.avinashio");
	
	assertThat(
			refreshed.metadata()
					.springBootVersion())
			.isEqualTo(
					"4.2.0");
}

@Test
void shouldReturnNullWhenProjectIsNull()
		throws Exception {
	
	RefreshSelectedProjectUseCase useCase =
			new RefreshSelectedProjectUseCase(
					List.of());
	
	assertThat(
			useCase.refresh(
					null))
			.isNull();
}
}