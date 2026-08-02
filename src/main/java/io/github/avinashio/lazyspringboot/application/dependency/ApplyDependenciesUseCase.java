package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ApplyDependenciesUseCase {

private final DependencyCoordinateResolver
		coordinateResolver;

private final List<ProjectDependencyWriter>
		dependencyWriters;

public ApplyDependenciesUseCase(
		DependencyCoordinateResolver coordinateResolver,
		List<ProjectDependencyWriter> dependencyWriters) {
	
	this.coordinateResolver =
			coordinateResolver;
	
	this.dependencyWriters =
			dependencyWriters;
}

public void apply(
		SpringProject project,
		List<DependencyItem> dependencyItems)
		throws IOException {
	
	if (project == null
				|| dependencyItems.isEmpty()) {
		
		return;
	}
	
	List<DependencyCoordinate> coordinates =
			dependencyItems.stream()
					.filter(
							DependencyItem::selectable)
					.filter(
							DependencyItem::selected)
					.map(
							DependencyItem::dependency)
					.map(
							coordinateResolver::resolve)
					.distinct()
					.toList();
	
	if (coordinates.isEmpty()) {
		return;
	}
	
	ProjectDependencyWriter writer =
			findWriter(
					project);
	
	writer.addDependencies(
			project,
			coordinates);
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