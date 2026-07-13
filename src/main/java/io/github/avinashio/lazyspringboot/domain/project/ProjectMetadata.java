package io.github.avinashio.lazyspringboot.domain.project;

public record ProjectMetadata(
        String groupId,
        String artifactId,
        String springBootVersion,
        String javaVersion,
        BuildTool buildTool) {}