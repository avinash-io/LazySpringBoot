package io.github.avinashio.lazyspringboot.infrastructure.maven;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MavenPomBackupRestorerTest {

    private final MavenPomBackupRestorer restorer =
            new MavenPomBackupRestorer();

    @TempDir
    Path temporaryDirectory;

    @Test
    void shouldDetectExistingBackup()
            throws Exception {
        Path pomPath =
                temporaryDirectory.resolve("pom.xml");

        Files.writeString(
                pomPath,
                "<project></project>");

        Files.writeString(
                backupPath(),
                "<project></project>");

        assertThat(
                restorer.backupExists(pomPath))
                .isTrue();
    }

    @Test
    void shouldReturnFalseWhenBackupDoesNotExist()
            throws Exception {
        Path pomPath =
                temporaryDirectory.resolve("pom.xml");

        Files.writeString(
                pomPath,
                "<project></project>");

        assertThat(
                restorer.backupExists(pomPath))
                .isFalse();
    }

    @Test
    void shouldRestorePomFromBackup()
            throws Exception {
        Path pomPath =
                temporaryDirectory.resolve("pom.xml");

        Files.writeString(
                pomPath,
                "<project>updated</project>");

        Files.writeString(
                backupPath(),
                "<project>original</project>");

        restorer.restore(pomPath);

        assertThat(
                Files.readString(pomPath))
                .isEqualTo(
                        "<project>original</project>");
    }

    @Test
    void shouldDeleteBackupAfterRestore()
            throws Exception {
        Path pomPath =
                temporaryDirectory.resolve("pom.xml");

        Files.writeString(
                pomPath,
                "<project>updated</project>");

        Files.writeString(
                backupPath(),
                "<project>original</project>");

        restorer.restore(pomPath);

        assertThat(backupPath())
                .doesNotExist();
    }

    @Test
    void shouldRejectRestoreWithoutBackup()
            throws Exception {
        Path pomPath =
                temporaryDirectory.resolve("pom.xml");

        Files.writeString(
                pomPath,
                "<project></project>");

        assertThatThrownBy(
                () -> restorer.restore(pomPath))
                .isInstanceOf(
                        java.io.IOException.class)
                .hasMessageContaining(
                        "No LazySpringBoot backup found");
    }

    private Path backupPath() {
        return temporaryDirectory.resolve(
                "pom.xml.lazyspringboot.bak");
    }
}