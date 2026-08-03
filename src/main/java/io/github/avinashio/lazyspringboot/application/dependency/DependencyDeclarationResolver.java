package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyDeclaration;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyScope;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DependencyDeclarationResolver {

private final DependencyCoordinateResolver
		coordinateResolver;

private final DependencyDeclarationProvider
		declarationProvider;

public DependencyDeclarationResolver(
		DependencyCoordinateResolver coordinateResolver,
		DependencyDeclarationProvider declarationProvider) {
	
	this.coordinateResolver =
			coordinateResolver;
	
	this.declarationProvider =
			declarationProvider;
}

public List<DependencyDeclaration> resolve(
		SpringDependency dependency) {
	
	return declarationProvider
				   .resolve(
						   dependency)
				   .filter(
						   declarations ->
								   !declarations.isEmpty())
				   .orElseGet(
						   () ->
								   fallback(
										   dependency));
}

private List<DependencyDeclaration> fallback(
		SpringDependency dependency) {
	
	DependencyCoordinate coordinate =
			coordinateResolver.resolve(
					dependency);
	
	return List.of(
			new DependencyDeclaration(
					coordinate,
					DependencyScope.COMPILE));
}
}