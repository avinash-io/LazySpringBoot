package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyDeclaration;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;

import java.util.List;
import java.util.Optional;

public interface DependencyDeclarationProvider {

Optional<List<DependencyDeclaration>> resolve(
		SpringDependency dependency);
}