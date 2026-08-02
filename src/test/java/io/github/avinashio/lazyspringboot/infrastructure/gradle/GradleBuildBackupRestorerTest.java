package io.github.avinashio.lazyspringboot.infrastructure.gradle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class GradleBuildBackupRestorerTest {

@TempDir
Path temporaryDirectory;

@Test
void shouldReportBackupExists()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle");
	
	Files.writeString(
			buildFile,
			"updated");
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle.lazyspringboot.bak"),
			"original");
	
	GradleBuildBackupRestorer restorer =
			new GradleBuildBackupRestorer();
	
	assertThat(
			restorer.backupExists(
					buildFile))
			.isTrue();
}

@Test
void shouldRestoreBackup()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle");
	
	Files.writeString(
			buildFile,
			"updated");
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle.lazyspringboot.bak"),
			"original");
	
	GradleBuildBackupRestorer restorer =
			new GradleBuildBackupRestorer();
	
	restorer.restore(
			buildFile);
	
	assertThat(
			Files.readString(
					buildFile))
			.isEqualTo(
					"original");
	
	assertThat(
			temporaryDirectory.resolve(
					"build.gradle.lazyspringboot.bak"))
			.doesNotExist();
}

@Test
void shouldNotOverwriteExistingBackup()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle");
	
	Path backupFile =
			temporaryDirectory.resolve(
					"build.gradle.lazyspringboot.bak");
	
	Files.writeString(
			buildFile,
			"first version");
	
	GradleBuildBackupRestorer restorer =
			new GradleBuildBackupRestorer();
	
	restorer.createBackup(
			buildFile);
	
	Files.writeString(
			buildFile,
			"second version");
	
	restorer.createBackup(
			buildFile);
	
	assertThat(
			Files.readString(
					backupFile))
			.isEqualTo(
					"first version");
}
}