package io.github.avinashio.lazyspringboot.infrastructure.filesystem;

import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenProjectInspector;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Component
public class ProjectScanner {

private final MavenProjectInspector mavenProjectInspector;

private final BuildToolDetector buildToolDetector;

public ProjectScanner(
		MavenProjectInspector mavenProjectInspector,
		BuildToolDetector buildToolDetector) {
	
	this.mavenProjectInspector = mavenProjectInspector;
	this.buildToolDetector = buildToolDetector;
}

public List<SpringProject> scan(
		Path directory) throws IOException {
	
	try (var paths = Files.list(directory)) {
		
		return paths
					   .filter(Files::isDirectory)
					   .map(this::inspectProject)
					   .flatMap(Optional::stream)
					   .toList();
	}
}

private Optional<SpringProject> inspectProject(
		Path directory) {
	
	Path pomFile =
			directory.resolve("pom.xml");
	
	if (!Files.isRegularFile(pomFile)) {
		
		return Optional.empty();
	}
	
	try {
		
		if (!mavenProjectInspector.isSpringBootProject(
				directory)) {
			
			return Optional.empty();
		}
		
		ProjectMetadata projectMetadata =
				mavenProjectInspector.inspect(
						directory);
		
		var buildTool =
				buildToolDetector.detect(
						directory);
		
		return Optional.of(
				new SpringProject(
						directory.getFileName().toString(),
						directory.toAbsolutePath(),
						buildTool,
						projectMetadata));
		
	} catch (IOException exception) {
		
		return Optional.empty();
	}
}
}