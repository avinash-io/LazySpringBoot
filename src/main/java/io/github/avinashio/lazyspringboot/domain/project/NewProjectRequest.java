package io.github.avinashio.lazyspringboot.domain.project;

import java.util.List;

public record NewProjectRequest(
        String groupId,
        String artifactId,
        String name,
        String packageName,
        String javaVersion,
        String springBootVersion,
        BuildTool buildTool,
        List<String> dependencies) {

    public NewProjectRequest {
        dependencies = List.copyOf(dependencies);
    }
}
