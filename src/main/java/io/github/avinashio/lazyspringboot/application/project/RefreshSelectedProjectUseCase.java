package io.github.avinashio.lazyspringboot.application.project;

import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenProjectInspector;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RefreshSelectedProjectUseCase {

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
	
	ProjectMetadata projectMetadata =
			mavenProjectInspector.inspect(
					project.path());
	
	return new SpringProject(
			project.name(),
			project.path(),
			project.buildTool(),
			projectMetadata);
}
}