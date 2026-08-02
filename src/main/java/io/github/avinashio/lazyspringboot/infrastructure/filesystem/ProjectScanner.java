package io.github.avinashio.lazyspringboot.infrastructure.filesystem;

import io.github.avinashio.lazyspringboot.application.project.ProjectInspector;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Component
public class ProjectScanner {

private final List<ProjectInspector>
		projectInspectors;

private final BuildToolDetector buildToolDetector;

public ProjectScanner(
		List<ProjectInspector> projectInspectors,
		BuildToolDetector buildToolDetector) {
	this.projectInspectors = projectInspectors;
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
	
	try {
		
		ProjectInspector inspector =
				projectInspectors.stream()
						.filter(candidate ->
										candidate.supports(
												directory))
						.findFirst()
						.orElse(null);
		
		if (inspector == null) {
			return Optional.empty();
		}
		
		if (!inspector.isSpringBootProject(
				directory)) {
			
			return Optional.empty();
		}
		
		ProjectMetadata projectMetadata =
				inspector.inspect(
						directory);
		
		BuildTool buildTool =
				buildToolDetector.detect(
						directory);
		
		return Optional.of(
				new SpringProject(
						directory
								.getFileName()
								.toString(),
						directory.toAbsolutePath(),
						buildTool,
						projectMetadata));
		
	} catch (IOException exception) {
		
		return Optional.empty();
	}
}
}