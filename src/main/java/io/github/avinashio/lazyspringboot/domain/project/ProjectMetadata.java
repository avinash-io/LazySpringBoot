package io.github.avinashio.lazyspringboot.domain.project;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import java.util.List;

public record ProjectMetadata(
        String groupId,
        String artifactId,
        String springBootVersion,
        String javaVersion,
        BuildTool buildTool,
        List<DependencyCoordinate> dependencies) {}