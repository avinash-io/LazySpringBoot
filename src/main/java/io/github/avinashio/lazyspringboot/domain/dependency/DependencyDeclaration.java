package io.github.avinashio.lazyspringboot.domain.dependency;

public record DependencyDeclaration(
		DependencyCoordinate coordinate,
		DependencyScope scope) {
}