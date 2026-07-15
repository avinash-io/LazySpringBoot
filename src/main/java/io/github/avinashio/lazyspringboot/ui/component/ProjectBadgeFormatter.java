package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectBadgeFormatter {

    public String format(
            SpringProject project) {

        List<String> badges =
                new ArrayList<>();

        List<DependencyCoordinate> dependencies =
                project.metadata().dependencies();

        if (hasWebDependency(dependencies)) {
            badges.add("[W]");
        }

        if (hasArtifact(
                dependencies,
                "spring-boot-starter-data-jpa")) {
            badges.add("[J]");
        }

        if (hasArtifact(
                dependencies,
                "spring-boot-starter-security")) {
            badges.add("[S]");
        }

        if (hasArtifact(
                dependencies,
                "spring-boot-starter-actuator")) {
            badges.add("[A]");
        }

        return String.join(
                " ",
                badges);
    }

    private boolean hasWebDependency(
            List<DependencyCoordinate> dependencies) {

        return hasArtifact(
                dependencies,
                "spring-boot-starter-web")
                || hasArtifact(
                dependencies,
                "spring-boot-starter-webmvc")
                || hasArtifact(
                dependencies,
                "spring-boot-starter-webflux");
    }

    private boolean hasArtifact(
            List<DependencyCoordinate> dependencies,
            String artifactId) {

        return dependencies
                .stream()
                .anyMatch(
                        dependency ->
                                artifactId.equals(
                                        dependency.artifactId()));
    }
}