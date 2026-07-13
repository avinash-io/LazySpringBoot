package io.github.avinashio.lazyspringboot.infrastructure.maven;

public record MavenProjectMetadata(
        String groupId,
        String artifactId,
        String springBootVersion,
        String javaVersion) {}