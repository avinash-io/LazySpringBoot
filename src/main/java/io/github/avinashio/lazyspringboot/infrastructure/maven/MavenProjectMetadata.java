package io.github.avinashio.lazyspringboot.infrastructure.maven;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import java.util.List;

public record MavenProjectMetadata(
        String groupId,
        String artifactId,
        String springBootVersion,
        String javaVersion,
        List<DependencyCoordinate> dependencies) {}