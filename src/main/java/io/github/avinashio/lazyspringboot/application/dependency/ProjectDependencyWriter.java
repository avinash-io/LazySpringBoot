package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;

import java.io.IOException;
import java.util.List;

public interface ProjectDependencyWriter {

boolean supports(
		SpringProject project);

void addDependencies(
		SpringProject project,
		List<DependencyCoordinate> dependencies)
		throws IOException;

boolean canUndo(
		SpringProject project);

void undo(
		SpringProject project)
		throws IOException;
}