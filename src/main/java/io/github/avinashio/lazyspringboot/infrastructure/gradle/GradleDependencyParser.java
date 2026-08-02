package io.github.avinashio.lazyspringboot.infrastructure.gradle;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GradleDependencyParser {

private static final Pattern
		DEPENDENCY_PATTERN =
		Pattern.compile(
				"(?:implementation|api|compileOnly|runtimeOnly|"
						+ "testImplementation|testCompileOnly|testRuntimeOnly)"
						+ "\\s*\\(?\\s*['\"]"
						+ "([^:'\"]+):([^:'\"]+)"
						+ "(?::[^'\"]+)?['\"]\\s*\\)?");

public List<DependencyCoordinate> parse(
		String content) {
	
	List<DependencyCoordinate> dependencies =
			new ArrayList<>();
	
	Matcher matcher =
			DEPENDENCY_PATTERN.matcher(
					content);
	
	while (matcher.find()) {
		
		dependencies.add(
				new DependencyCoordinate(
						matcher.group(1),
						matcher.group(2)));
	}
	
	return List.copyOf(
			dependencies);
}
}