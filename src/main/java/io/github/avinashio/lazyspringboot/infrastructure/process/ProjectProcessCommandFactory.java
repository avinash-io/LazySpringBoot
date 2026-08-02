package io.github.avinashio.lazyspringboot.infrastructure.process;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.util.List;

@Component
public class ProjectProcessCommandFactory {

public List<String> create(
		SpringProject project) {
	
	return switch (project.buildTool()) {
		case MAVEN -> createMavenCommand(
				project);
		
		case GRADLE, GRADLE_KOTLIN -> createGradleCommand(
				project);
		
		case UNKNOWN -> throw new IllegalArgumentException(
				"Unsupported build tool: "
						+ project.buildTool());
	};
}

private List<String> createMavenCommand(
		SpringProject project) {
	
	if (Files.isRegularFile(
			project.path()
					.resolve("mvnw"))) {
		
		return List.of(
				"sh",
				"./mvnw",
				"spring-boot:run");
	}
	
	return List.of(
			"mvn",
			"spring-boot:run");
}

private List<String> createGradleCommand(
		SpringProject project) {
	
	if (Files.isRegularFile(
			project.path()
					.resolve("gradlew"))) {
		
		return List.of(
				"sh",
				"./gradlew",
				"bootRun");
	}
	
	return List.of(
			"gradle",
			"bootRun");
}
}