package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyAvailability;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class BuildDependencyItemsUseCase {

    private final DependencyMatcher dependencyMatcher;

    public BuildDependencyItemsUseCase(
            DependencyMatcher dependencyMatcher) {
        this.dependencyMatcher = dependencyMatcher;
    }

    public List<DependencyItem> build(
            List<SpringDependency> dependencies,
            SpringProject project) {
        Set<DependencyCoordinate> existingDependencies =
                project == null
                        ? Set.of()
                        : new HashSet<>(
                        project.metadata().dependencies());

        return dependencies.stream()
                .map(
                        dependency ->
                                new DependencyItem(
                                        dependency,
                                        availability(
                                                dependency,
                                                existingDependencies),
                                        false))
                .toList();
    }

    private DependencyAvailability availability(
            SpringDependency dependency,
            Set<DependencyCoordinate> existingDependencies) {
        if (dependencyMatcher.isAlreadyPresent(
                dependency,
                existingDependencies)) {
            return DependencyAvailability.ALREADY_PRESENT;
        }

        return DependencyAvailability.AVAILABLE;
    }
}