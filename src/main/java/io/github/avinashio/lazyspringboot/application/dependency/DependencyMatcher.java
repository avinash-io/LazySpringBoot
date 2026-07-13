package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class DependencyMatcher {

    private static final String SPRING_BOOT_GROUP =
            "org.springframework.boot";

    private static final Map<String, DependencyCoordinate>
            DEPENDENCY_ALIASES =
            Map.of(
                    "web",
                    new DependencyCoordinate(
                            SPRING_BOOT_GROUP,
                            "spring-boot-starter-webmvc"),
                    "lombok",
                    new DependencyCoordinate(
                            "org.projectlombok",
                            "lombok"));

    public boolean isAlreadyPresent(
            SpringDependency dependency,
            Set<DependencyCoordinate> existingDependencies) {
        return candidateCoordinates(dependency).stream()
                .anyMatch(existingDependencies::contains);
    }

    private Set<DependencyCoordinate> candidateCoordinates(
            SpringDependency dependency) {
        DependencyCoordinate alias =
                DEPENDENCY_ALIASES.get(dependency.id());

        if (alias != null) {
            return Set.of(
                    alias,
                    directCoordinate(dependency),
                    starterCoordinate(dependency),
                    springBootCoordinate(dependency));
        }

        return Set.of(
                directCoordinate(dependency),
                starterCoordinate(dependency),
                springBootCoordinate(dependency));
    }

    private DependencyCoordinate directCoordinate(
            SpringDependency dependency) {
        return new DependencyCoordinate(
                dependency.id(),
                dependency.id());
    }

    private DependencyCoordinate starterCoordinate(
            SpringDependency dependency) {
        return new DependencyCoordinate(
                SPRING_BOOT_GROUP,
                "spring-boot-starter-" + dependency.id());
    }

    private DependencyCoordinate springBootCoordinate(
            SpringDependency dependency) {
        return new DependencyCoordinate(
                SPRING_BOOT_GROUP,
                "spring-boot-" + dependency.id());
    }
}