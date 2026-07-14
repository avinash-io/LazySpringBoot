package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class DependencyCoordinateResolver {

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
                            "lombok"),
                    "postgresql",
                    new DependencyCoordinate(
                            "org.postgresql",
                            "postgresql"));

    public DependencyCoordinate resolve(
            SpringDependency dependency) {
        DependencyCoordinate alias =
                DEPENDENCY_ALIASES.get(dependency.id());

        if (alias != null) {
            return alias;
        }

        return starterCoordinate(dependency);
    }

    public Set<DependencyCoordinate> candidates(
            SpringDependency dependency) {
        Set<DependencyCoordinate> coordinates =
                new LinkedHashSet<>();

        DependencyCoordinate alias =
                DEPENDENCY_ALIASES.get(dependency.id());

        if (alias != null) {
            coordinates.add(alias);
        }

        coordinates.add(starterCoordinate(dependency));
        coordinates.add(springBootCoordinate(dependency));

        return Set.copyOf(coordinates);
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