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

public DependencyDeclarationResolver(
		DependencyCoordinateResolver coordinateResolver) {
	
	this.coordinateResolver =
			coordinateResolver;
}

public List<DependencyDeclaration> resolve(
		SpringDependency dependency) {
	
	DependencyCoordinate coordinate =
			coordinateResolver.resolve(
					dependency);
	
	return switch (dependency.id()) {
		
		case "postgresql" -> List.of(
				new DependencyDeclaration(
						coordinate,
						DependencyScope.RUNTIME));
		
		case "lombok" -> List.of(
				new DependencyDeclaration(
						coordinate,
						DependencyScope.COMPILE_ONLY),
				new DependencyDeclaration(
						coordinate,
						DependencyScope.ANNOTATION_PROCESSOR));
		
		default -> List.of(
				new DependencyDeclaration(
						coordinate,
						DependencyScope.COMPILE));
	};
}
}