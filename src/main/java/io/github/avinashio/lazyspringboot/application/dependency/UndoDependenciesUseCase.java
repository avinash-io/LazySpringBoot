package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UndoDependenciesUseCase {

private final List<ProjectDependencyWriter>
		dependencyWriters;

public UndoDependenciesUseCase(
		List<ProjectDependencyWriter> dependencyWriters) {
	
	this.dependencyWriters =
			dependencyWriters;
}

public boolean canUndo(
		SpringProject project) {
	
	if (project == null) {
		return false;
	}
	
	return findWriter(
			project)
				   .canUndo(
						   project);
}

public void undo(
		SpringProject project)
		throws IOException {
	
	if (project == null) {
		return;
	}
	
	findWriter(
			project)
			.undo(
					project);
}

private ProjectDependencyWriter findWriter(
		SpringProject project) {
	
	return dependencyWriters.stream()
				   .filter(writer ->
								   writer.supports(
										   project))
				   .findFirst()
				   .orElseThrow(
						   () ->
								   new IllegalArgumentException(
										   "Unsupported build tool: "
												   + project.buildTool()));
}
}