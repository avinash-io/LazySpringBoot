package io.github.avinashio.lazyspringboot.infrastructure.gradle;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyDeclaration;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GradleDependencyParser {

private static final Pattern DEPENDENCY_PATTERN =
		Pattern.compile(
				"(implementation|api|compileOnly|annotationProcessor|"
						+ "runtimeOnly|testImplementation|testCompileOnly|"
						+ "testRuntimeOnly|testAnnotationProcessor)"
						+ "\\s*\\(?\\s*['\"]"
						+ "([^:'\"]+):([^:'\"]+)"
						+ "(?::[^'\"]+)?['\"]\\s*\\)?");

public List<DependencyCoordinate> parse(
		String content) {
	
	return parseDeclarations(
			content)
				   .stream()
				   .map(
						   DependencyDeclaration::coordinate)
				   .distinct()
				   .toList();
}

public List<DependencyDeclaration> parseDeclarations(
		String content) {
	
	List<DependencyDeclaration> declarations =
			new ArrayList<>();
	
	Matcher matcher =
			DEPENDENCY_PATTERN.matcher(
					content);
	
	while (matcher.find()) {
		
		DependencyScope scope =
				scopeFor(
						matcher.group(1));
		
		if (scope == null) {
			continue;
		}
		
		declarations.add(
				new DependencyDeclaration(
						new DependencyCoordinate(
								matcher.group(2),
								matcher.group(3)),
						scope));
	}
	
	return List.copyOf(
			declarations);
}

private DependencyScope scopeFor(
		String configuration) {
	
	return switch (configuration) {
		
		case "implementation",
			 "api" -> DependencyScope.COMPILE;
		
		case "runtimeOnly" -> DependencyScope.RUNTIME;
		
		case "compileOnly" -> DependencyScope.COMPILE_ONLY;
		
		case "annotationProcessor" -> DependencyScope.ANNOTATION_PROCESSOR;
		
		case "testImplementation",
			 "testCompileOnly",
			 "testRuntimeOnly",
			 "testAnnotationProcessor" -> DependencyScope.TEST;
		
		default -> null;
	};
}
}