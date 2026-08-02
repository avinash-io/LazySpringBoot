package io.github.avinashio.lazyspringboot.infrastructure.gradle;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class GradleBuildBackupRestorer {

private static final String BACKUP_SUFFIX =
		".lazyspringboot.bak";

public void createBackup(
		Path buildFile)
		throws IOException {
	
	Path backupPath =
			backupPath(
					buildFile);
	
	if (!Files.exists(
			backupPath)) {
		
		Files.copy(
				buildFile,
				backupPath);
	}
}

public boolean backupExists(
		Path buildFile) {
	
	return Files.isRegularFile(
			backupPath(
					buildFile));
}

public void restore(
		Path buildFile)
		throws IOException {
	
	Path backupPath =
			backupPath(
					buildFile);
	
	if (!Files.isRegularFile(
			backupPath)) {
		
		throw new IOException(
				"Backup does not exist for "
						+ buildFile.getFileName());
	}
	
	Files.move(
			backupPath,
			buildFile,
			StandardCopyOption.REPLACE_EXISTING);
}

private Path backupPath(
		Path buildFile) {
	
	return buildFile.resolveSibling(
			buildFile.getFileName()
					+ BACKUP_SUFFIX);
}
}