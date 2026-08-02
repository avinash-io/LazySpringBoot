package io.github.avinashio.lazyspringboot.infrastructure.project;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.NewProjectRequest;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

@Component
public class SpringInitializrRequestBuilder {

private static final String BASE_URL =
		"https://start.spring.io/starter.zip";

public URI build(
		NewProjectRequest request) {
	
	StringJoiner parameters =
			new StringJoiner("&");
	
	add(
			parameters,
			"type",
			initializrType(
					request.buildTool()));
	
	add(
			parameters,
			"groupId",
			request.groupId());
	
	add(
			parameters,
			"artifactId",
			request.artifactId());
	
	add(
			parameters,
			"name",
			request.name());
	
	add(
			parameters,
			"packageName",
			request.packageName());
	
	add(
			parameters,
			"javaVersion",
			request.javaVersion());
	
	add(
			parameters,
			"bootVersion",
			request.springBootVersion());
	
	if (!request.dependencies().isEmpty()) {
		
		add(
				parameters,
				"dependencies",
				String.join(
						",",
						request.dependencies()));
	}
	
	return URI.create(
			BASE_URL
					+ "?"
					+ parameters);
}

private String initializrType(
		BuildTool buildTool) {
	
	if (buildTool == null) {
		return "maven-project";
	}
	
	return switch (buildTool) {
		
		case MAVEN -> "maven-project";
		
		case GRADLE -> "gradle-project";
		
		case GRADLE_KOTLIN -> "gradle-project-kotlin";
		
		case UNKNOWN -> throw new IllegalArgumentException(
				"Unsupported build tool: "
						+ buildTool);
	};
}

private void add(
		StringJoiner joiner,
		String key,
		String value) {
	
	if (value == null
				|| value.isBlank()) {
		return;
	}
	
	joiner.add(
			encode(key)
					+ "="
					+ encode(value));
}

private String encode(
		String value) {
	
	return URLEncoder.encode(
			value,
			StandardCharsets.UTF_8);
}
}