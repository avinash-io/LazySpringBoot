package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyDeclaration;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ApplyDependenciesUseCase {

private final DependencyDeclarationResolver
		declarationResolver;

private final List<ProjectDependencyWriter>
		dependencyWriters;

public ApplyDependenciesUseCase(
		DependencyDeclarationResolver declarationResolver,
		List<ProjectDependencyWriter> dependencyWriters) {
	
	this.declarationResolver =
			declarationResolver;
	
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
	
	List<DependencyDeclaration> declarations =
			dependencyItems.stream()
					.filter(
							DependencyItem::selectable)
					.filter(
							DependencyItem::selected)
					.map(
							DependencyItem::dependency)
					.flatMap(dependency ->
									 declarationResolver.resolve(
													 dependency)
											 .stream())
					.distinct()
					.toList();
	
	if (declarations.isEmpty()) {
		return;
	}
	
	ProjectDependencyWriter writer =
			findWriter(
					project);
	
	writer.addDependencies(
			project,
			declarations);
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