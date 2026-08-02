package io.github.avinashio.lazyspringboot.infrastructure.maven;

import io.github.avinashio.lazyspringboot.application.dependency.ProjectDependencyWriter;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class MavenProjectDependencyWriter
		implements ProjectDependencyWriter {

private static final String POM_FILE_NAME =
		"pom.xml";

private final MavenPomDependencyWriter
		dependencyWriter;

private final MavenPomBackupRestorer
		backupRestorer;

public MavenProjectDependencyWriter(
		MavenPomDependencyWriter dependencyWriter,
		MavenPomBackupRestorer backupRestorer) {
	
	this.dependencyWriter =
			dependencyWriter;
	
	this.backupRestorer =
			backupRestorer;
}

@Override
public boolean supports(
		SpringProject project) {
	
	return project.buildTool()
				   == BuildTool.MAVEN;
}

@Override
public void addDependencies(
		SpringProject project,
		List<DependencyCoordinate> dependencies)
		throws IOException {
	
	dependencyWriter.addDependencies(
			pomPath(project),
			dependencies);
}

@Override
public boolean canUndo(
		SpringProject project) {
	
	return backupRestorer.backupExists(
			pomPath(project));
}

@Override
public void undo(
		SpringProject project)
		throws IOException {
	
	backupRestorer.restore(
			pomPath(project));
}

private Path pomPath(
		SpringProject project) {
	
	return project.path()
				   .resolve(POM_FILE_NAME);
}
}