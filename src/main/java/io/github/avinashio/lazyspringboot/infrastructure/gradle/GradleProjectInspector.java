package io.github.avinashio.lazyspringboot.infrastructure.gradle;

import io.github.avinashio.lazyspringboot.application.project.ProjectInspector;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GradleProjectInspector
		implements ProjectInspector {

private static final Pattern
		SPRING_BOOT_VERSION_PATTERN =
		Pattern.compile(
				"org\\.springframework\\.boot['\"\\)]*\\s+version\\s+['\"]([^'\"]+)['\"]");
private static final Pattern
		GROUP_PATTERN =
		Pattern.compile(
				"group\\s*=\\s*['\"]([^'\"]+)['\"]");
private static final Pattern
		JAVA_VERSION_PATTERN =
		Pattern.compile(
				"JavaLanguageVersion\\.of\\s*\\(\\s*(\\d+)\\s*\\)");
private final GradleDependencyParser
		dependencyParser;

public GradleProjectInspector(
		GradleDependencyParser dependencyParser) {
	
	this.dependencyParser =
			dependencyParser;
}

@Override
public boolean supports(
		Path projectDirectory) {
	
	return findBuildFile(
			projectDirectory) != null;
}

@Override
public boolean isSpringBootProject(
		Path projectDirectory)
		throws IOException {
	
	Path buildFile =
			findBuildFile(
					projectDirectory);
	
	if (buildFile == null) {
		return false;
	}
	
	String content =
			Files.readString(
					buildFile);
	
	return content.contains(
			"org.springframework.boot");
}

@Override
public ProjectMetadata inspect(
		Path projectDirectory)
		throws IOException {
	
	Path buildFile =
			findBuildFile(
					projectDirectory);
	
	if (buildFile == null) {
		throw new IOException(
				"Gradle build file not found: "
						+ projectDirectory);
	}
	
	String content =
			Files.readString(
					buildFile);
	
	return new ProjectMetadata(
			extract(
					GROUP_PATTERN,
					content),
			projectDirectory
					.getFileName()
					.toString(),
			extractSpringBootVersion(
					content),
			extract(
					JAVA_VERSION_PATTERN,
					content),
			dependencyParser.parse(
					content));
}


private Path findBuildFile(
		Path projectDirectory) {
	
	Path kotlinBuildFile =
			projectDirectory.resolve(
					"build.gradle.kts");
	
	if (Files.isRegularFile(
			kotlinBuildFile)) {
		
		return kotlinBuildFile;
	}
	
	Path groovyBuildFile =
			projectDirectory.resolve(
					"build.gradle");
	
	if (Files.isRegularFile(
			groovyBuildFile)) {
		
		return groovyBuildFile;
	}
	
	return null;
}

private String extractSpringBootVersion(
		String content) {
	
	Matcher matcher =
			SPRING_BOOT_VERSION_PATTERN.matcher(
					content);
	
	if (matcher.find()) {
		return matcher.group(1);
	}
	
	return null;
}

private String extract(
		Pattern pattern,
		String content) {
	
	Matcher matcher =
			pattern.matcher(
					content);
	
	if (matcher.find()) {
		return matcher.group(1);
	}
	
	return null;
}

}