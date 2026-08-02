package io.github.avinashio.lazyspringboot.application.action;

import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectCommand;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectCommandResolver {

public ProjectCommand resolve(
		SpringProject project,
		ProjectAction action) {
	
	List<String> command =
			switch (project.buildTool()) {
				case MAVEN -> resolveMavenCommand(
						project,
						action);
				
				case GRADLE, GRADLE_KOTLIN -> resolveGradleCommand(
						project,
						action);
				
				case UNKNOWN -> throw new IllegalArgumentException(
						"Unsupported build tool: "
								+ project.buildTool());
			};
	
	return new ProjectCommand(
			command,
			project.path());
}

private List<String> resolveMavenCommand(
		SpringProject project,
		ProjectAction action) {
	
	List<String> command =
			new ArrayList<>(
					resolveMavenCommandPrefix(
							project));
	
	switch (action) {
		case BUILD -> {
			command.add("clean");
			command.add("package");
		}
		
		case INSTALL -> {
			command.add("clean");
			command.add("install");
		}
		
		case TEST -> command.add("test");
		
		case RUN, VIEW_LOGS, RESTART, STOP -> throw new IllegalArgumentException(
				"Action is not a command action: "
						+ action);
	}
	
	return List.copyOf(
			command);
}

private List<String> resolveGradleCommand(
		SpringProject project,
		ProjectAction action) {
	
	List<String> command =
			new ArrayList<>(
					resolveGradleCommandPrefix(
							project));
	
	switch (action) {
		case BUILD -> {
			command.add("clean");
			command.add("build");
		}
		
		case INSTALL -> command.add("build");
		
		case TEST -> command.add("test");
		
		case RUN, VIEW_LOGS, RESTART, STOP -> throw new IllegalArgumentException(
				"Action is not a command action: "
						+ action);
	}
	
	return List.copyOf(
			command);
}

private List<String> resolveMavenCommandPrefix(
		SpringProject project) {
	
	Path mavenWrapper =
			project.path()
					.resolve("mvnw");
	
	if (Files.isRegularFile(
			mavenWrapper)) {
		
		return List.of(
				"sh",
				"./mvnw");
	}
	
	return List.of(
			"mvn");
}

private List<String> resolveGradleCommandPrefix(
		SpringProject project) {
	
	Path gradleWrapper =
			project.path()
					.resolve("gradlew");
	
	if (Files.isRegularFile(
			gradleWrapper)) {
		
		return List.of(
				"sh",
				"./gradlew");
	}
	
	return List.of(
			"gradle");
}
}