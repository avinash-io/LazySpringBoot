package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenPomDependencyWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ApplyDependenciesUseCase {

    private static final String POM_FILE_NAME =
            "pom.xml";

    private final DependencyCoordinateResolver
            coordinateResolver;

    private final MavenPomDependencyWriter
            pomDependencyWriter;

    public ApplyDependenciesUseCase(
            DependencyCoordinateResolver coordinateResolver,
            MavenPomDependencyWriter pomDependencyWriter) {
        this.coordinateResolver =
                coordinateResolver;
        this.pomDependencyWriter =
                pomDependencyWriter;
    }

    public void apply(
            SpringProject project,
            List<DependencyItem> dependencyItems)
            throws IOException {
        if (project == null
                || dependencyItems.isEmpty()) {
            return;
        }

        List<DependencyCoordinate> coordinates =
                dependencyItems.stream()
                        .filter(DependencyItem::selectable)
                        .filter(DependencyItem::selected)
                        .map(DependencyItem::dependency)
                        .map(coordinateResolver::resolve)
                        .distinct()
                        .toList();

        if (coordinates.isEmpty()) {
            return;
        }

        Path pomPath =
                project.path()
                        .resolve(POM_FILE_NAME);

        pomDependencyWriter.addDependencies(
                pomPath,
                coordinates);
    }
}