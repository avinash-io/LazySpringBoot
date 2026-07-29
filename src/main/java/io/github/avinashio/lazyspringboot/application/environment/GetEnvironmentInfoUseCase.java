package io.github.avinashio.lazyspringboot.application.environment;

import io.github.avinashio.lazyspringboot.domain.environment.EnvironmentInfo;
import io.github.avinashio.lazyspringboot.service.WorkspaceService;
import io.github.avinashio.lazyspringboot.ui.service.GitVersionService;
import io.github.avinashio.lazyspringboot.ui.service.InstalledToolsService;
import io.github.avinashio.lazyspringboot.ui.service.JavaVersionService;
import io.github.avinashio.lazyspringboot.ui.service.MavenVersionService;
import org.springframework.stereotype.Component;

@Component
public class GetEnvironmentInfoUseCase {

private final JavaVersionService
		javaVersionService;

private final MavenVersionService
		mavenVersionService;

private final GitVersionService
		gitVersionService;

private final InstalledToolsService
		installedToolsService;

private final WorkspaceService
		workspaceService;

public GetEnvironmentInfoUseCase(
		JavaVersionService
				javaVersionService,
		MavenVersionService mavenVersionService,
		GitVersionService gitVersionService,
		InstalledToolsService installedToolsService,
		WorkspaceService workspaceService) {
	
	this.javaVersionService =
			javaVersionService;
	this.mavenVersionService = mavenVersionService;
	this.gitVersionService = gitVersionService;
	this.installedToolsService = installedToolsService;
	this.workspaceService = workspaceService;
}

public EnvironmentInfo getEnvironmentInfo() {
	
	return new EnvironmentInfo(
			javaVersionService.version(),
			mavenVersionService.version(),
			gitVersionService.version(),
			installedToolsService.hasIntelliJ(),
			installedToolsService.hasVSCode(),
			workspaceService.workspace().toString());
}
}