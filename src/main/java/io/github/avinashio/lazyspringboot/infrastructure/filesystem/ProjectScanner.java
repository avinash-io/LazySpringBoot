package io.github.avinashio.lazyspringboot.infrastructure.filesystem;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenProjectInspector;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectScanner {

    private final MavenProjectInspector mavenProjectInspector;

    public ProjectScanner(MavenProjectInspector mavenProjectInspector) {
        this.mavenProjectInspector = mavenProjectInspector;
    }

    public List<SpringProject> scan(Path directory) throws IOException {
        try (var paths = Files.list(directory)) {
            return paths
                    .filter(Files::isDirectory)
                    .filter(this::isSpringBootProject)
                    .map(this::toSpringProject)
                    .toList();
        }
    }

    private boolean isSpringBootProject(Path directory) {
        Path pomFile = directory.resolve("pom.xml");

        if (!Files.isRegularFile(pomFile)) {
            return false;
        }

        try {
            return mavenProjectInspector.isSpringBootProject(pomFile);
        } catch (IOException exception) {
            return false;
        }
    }

    private SpringProject toSpringProject(Path directory) {
        return new SpringProject(
                directory.getFileName().toString(), directory.toAbsolutePath());
    }
}