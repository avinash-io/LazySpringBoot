package io.github.avinashio.lazyspringboot.infrastructure.gradle;

import io.github.avinashio.lazyspringboot.application.dependency.ProjectDependencyWriter;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyDeclaration;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class GradleProjectDependencyWriter
		implements ProjectDependencyWriter {

private final GradleDependencyWriter
		dependencyWriter;

private final GradleBuildBackupRestorer
		backupRestorer;

public GradleProjectDependencyWriter(
		GradleDependencyWriter dependencyWriter,
		GradleBuildBackupRestorer backupRestorer) {
	
	this.dependencyWriter =
			dependencyWriter;
	
	this.backupRestorer =
			backupRestorer;
}

@Override
public boolean supports(
		SpringProject project) {
	
	return project.buildTool()
				   == BuildTool.GRADLE
				   || project.buildTool()
							  == BuildTool.GRADLE_KOTLIN;
}

@Override
public void addDependencies(
		SpringProject project,
		List<DependencyDeclaration> dependencies)
		throws IOException {
	
	dependencyWriter.addDependencies(
			buildFile(
					project),
			dependencies);
}

@Override
public boolean canUndo(
		SpringProject project) {
	
	return backupRestorer.backupExists(
			buildFile(
					project));
}

@Override
public void undo(
		SpringProject project)
		throws IOException {
	
	backupRestorer.restore(
			buildFile(
					project));
}

private Path buildFile(
		SpringProject project) {
	
	return switch (project.buildTool()) {
		
		case GRADLE -> project.path()
							   .resolve(
									   "build.gradle");
		
		case GRADLE_KOTLIN -> project.path()
									  .resolve(
											  "build.gradle.kts");
		
		case MAVEN, UNKNOWN -> throw new IllegalArgumentException(
				"Unsupported build tool: "
						+ project.buildTool());
	};
}
}