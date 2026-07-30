package io.github.avinashio.lazyspringboot.infrastructure.process;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.util.List;

@Component
public class ProjectProcessCommandFactory {

public List<String> create(
		SpringProject project) {
	BuildTool buildTool =
			project.buildTool();
	
	if (buildTool != BuildTool.MAVEN) {
		throw new IllegalArgumentException(
				"Unsupported build tool: "
						+ buildTool);
	}
	
	return createMavenCommand(project);
}

private List<String> createMavenCommand(
		SpringProject project) {
	if (Files.isRegularFile(
			project.path().resolve("mvnw"))) {
		return List.of(
				"sh",
				"./mvnw",
				"spring-boot:run");
	}
	
	return List.of(
			"mvn",
			"spring-boot:run");
}
}