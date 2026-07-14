package io.github.avinashio.lazyspringboot.infrastructure.maven;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.springframework.stereotype.Component;

@Component
public class MavenPomBackupRestorer {

    private static final String BACKUP_SUFFIX =
            ".lazyspringboot.bak";

    public boolean backupExists(
            Path pomPath) {
        return Files.exists(
                backupPath(pomPath));
    }

    public void restore(
            Path pomPath)
            throws IOException {
        Path backupPath =
                backupPath(pomPath);

        if (!Files.exists(backupPath)) {
            throw new IOException(
                    "No LazySpringBoot backup found");
        }

        Files.copy(
                backupPath,
                pomPath,
                StandardCopyOption.REPLACE_EXISTING);

        Files.delete(
                backupPath);
    }

    private Path backupPath(
            Path pomPath) {
        return pomPath.resolveSibling(
                pomPath.getFileName()
                        + BACKUP_SUFFIX);
    }
}