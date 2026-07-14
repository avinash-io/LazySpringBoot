package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class DependencyMatcher {

    private final DependencyCoordinateResolver
            coordinateResolver;

    public DependencyMatcher(
            DependencyCoordinateResolver coordinateResolver) {
        this.coordinateResolver =
                coordinateResolver;
    }

    public boolean isAlreadyPresent(
            SpringDependency dependency,
            Set<DependencyCoordinate> existingDependencies) {
        return coordinateResolver
                .candidates(dependency)
                .stream()
                .anyMatch(existingDependencies::contains);
    }
}