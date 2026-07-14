package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenPomBackupRestorer;
import java.io.IOException;
import java.nio.file.Path;
import org.springframework.stereotype.Service;

@Service
public class UndoDependenciesUseCase {

    private static final String POM_FILE_NAME =
            "pom.xml";

    private final MavenPomBackupRestorer
            backupRestorer;

    public UndoDependenciesUseCase(
            MavenPomBackupRestorer backupRestorer) {
        this.backupRestorer =
                backupRestorer;
    }

    public boolean canUndo(
            SpringProject project) {
        if (project == null) {
            return false;
        }

        return backupRestorer.backupExists(
                pomPath(project));
    }

    public void undo(
            SpringProject project)
            throws IOException {
        if (project == null) {
            return;
        }

        backupRestorer.restore(
                pomPath(project));
    }

    private Path pomPath(
            SpringProject project) {
        return project.path()
                .resolve(POM_FILE_NAME);
    }
}