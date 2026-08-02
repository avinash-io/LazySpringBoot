package io.github.avinashio.lazyspringboot.application.project;

import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RefreshSelectedProjectUseCase {

private final List<ProjectInspector>
		projectInspectors;

public RefreshSelectedProjectUseCase(
		List<ProjectInspector> projectInspectors) {
	
	this.projectInspectors =
			projectInspectors;
}

public SpringProject refresh(
		SpringProject project)
		throws IOException {
	
	if (project == null) {
		return null;
	}
	
	ProjectInspector inspector =
			projectInspectors.stream()
					.filter(candidate ->
									candidate.supports(
											project.path()))
					.findFirst()
					.orElseThrow(
							() ->
									new IllegalArgumentException(
											"Unsupported project: "
													+ project.path()));
	
	ProjectMetadata projectMetadata =
			inspector.inspect(
					project.path());
	
	return new SpringProject(
			project.name(),
			project.path(),
			project.buildTool(),
			projectMetadata);
}
}