package io.github.avinashio.lazyspringboot.application.project;

import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenProjectInspector;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenProjectMetadata;
import java.io.IOException;
import java.nio.file.Path;
import org.springframework.stereotype.Service;

@Service
public class RefreshSelectedProjectUseCase {

    private static final String POM_FILE_NAME = "pom.xml";

    private final MavenProjectInspector mavenProjectInspector;

    public RefreshSelectedProjectUseCase(
            MavenProjectInspector mavenProjectInspector) {
        this.mavenProjectInspector = mavenProjectInspector;
    }

    public SpringProject refresh(SpringProject project)
            throws IOException {
        if (project == null) {
            return null;
        }

        Path pomPath =
                project.path().resolve(POM_FILE_NAME);

        MavenProjectMetadata metadata =
                mavenProjectInspector.inspect(pomPath);

        ProjectMetadata projectMetadata =
                new ProjectMetadata(
                        metadata.groupId(),
                        metadata.artifactId(),
                        metadata.springBootVersion(),
                        metadata.javaVersion(),
                        project.metadata().buildTool(),
                        metadata.dependencies());

        return new SpringProject(
                project.name(),
                project.path(),
                projectMetadata);
    }
}