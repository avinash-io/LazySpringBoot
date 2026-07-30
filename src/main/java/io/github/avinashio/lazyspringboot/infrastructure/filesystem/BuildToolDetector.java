package io.github.avinashio.lazyspringboot.infrastructure.filesystem;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class BuildToolDetector {

public BuildTool detect(
		Path projectDirectory) {
	
	if (Files.exists(
			projectDirectory.resolve(
					"pom.xml"))) {
		
		return BuildTool.MAVEN;
	}
	
	if (Files.exists(
			projectDirectory.resolve(
					"build.gradle.kts"))) {
		
		return BuildTool.GRADLE_KOTLIN;
	}
	
	if (Files.exists(
			projectDirectory.resolve(
					"build.gradle"))) {
		
		return BuildTool.GRADLE;
	}
	
	return BuildTool.UNKNOWN;
}
}