package io.github.avinashio.lazyspringboot.application.project;

import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;

import java.io.IOException;
import java.nio.file.Path;

public interface ProjectInspector {

boolean supports(
		Path projectDirectory);

boolean isSpringBootProject(
		Path projectDirectory)
		throws IOException;

ProjectMetadata inspect(
		Path projectDirectory)
		throws IOException;
}