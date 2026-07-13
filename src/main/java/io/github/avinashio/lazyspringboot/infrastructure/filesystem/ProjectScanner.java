package io.github.avinashio.lazyspringboot.infrastructure.filesystem;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenProjectInspector;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Component;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import java.util.Optional;

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
                    .map(this::inspectProject)
                    .flatMap(Optional::stream)
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

    private Optional<SpringProject> inspectProject(Path directory) {
        Path pomFile = directory.resolve("pom.xml");

        if (!Files.isRegularFile(pomFile)) {
            return java.util.Optional.empty();
        }

        try {
            if (!mavenProjectInspector.isSpringBootProject(pomFile)) {
                return java.util.Optional.empty();
            }

            var metadata = mavenProjectInspector.inspect(pomFile);

            ProjectMetadata projectMetadata =
                    new ProjectMetadata(
                            metadata.groupId(),
                            metadata.artifactId(),
                            metadata.springBootVersion(),
                            metadata.javaVersion(),
                            BuildTool.MAVEN,
                            metadata.dependencies());

            return java.util.Optional.of(
                    new SpringProject(
                            directory.getFileName().toString(),
                            directory.toAbsolutePath(),
                            projectMetadata));

        } catch (IOException exception) {
            return java.util.Optional.empty();
        }
    }

}