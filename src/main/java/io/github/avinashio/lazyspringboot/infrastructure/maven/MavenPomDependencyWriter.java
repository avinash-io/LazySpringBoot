package io.github.avinashio.lazyspringboot.infrastructure.maven;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyDeclaration;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyScope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Component
public class MavenPomDependencyWriter {

private static final String DEPENDENCIES_CLOSE =
		"</dependencies>";

private static final String PROJECT_CLOSE =
		"</project>";

private final MavenDependencyParser
		dependencyParser;

public MavenPomDependencyWriter(
		MavenDependencyParser dependencyParser) {
	
	this.dependencyParser =
			dependencyParser;
}

public void addDependencies(
		Path pomPath,
		List<DependencyDeclaration> dependencies)
		throws IOException {
	
	if (dependencies.isEmpty()) {
		return;
	}
	
	String original =
			Files.readString(
					pomPath,
					StandardCharsets.UTF_8);
	
	validatePom(original);
	
	String updated =
			original;
	
	for (DependencyDeclaration declaration
			: dependencies) {
		
		if (declaration.scope()
					== DependencyScope.ANNOTATION_PROCESSOR) {
			
			continue;
		}
		
		if (containsDependency(
				updated,
				declaration.coordinate())) {
			
			continue;
		}
		
		updated =
				insertDependency(
						updated,
						declaration);
	}
	
	if (original.equals(updated)) {
		return;
	}
	
	writeSafely(
			pomPath,
			original,
			updated);
}

private boolean containsDependency(
		String pom,
		DependencyCoordinate dependency)
		throws IOException {
	
	try (ByteArrayInputStream inputStream =
				 new ByteArrayInputStream(
						 pom.getBytes(
								 StandardCharsets.UTF_8))) {
		
		return dependencyParser
					   .parse(inputStream)
					   .contains(dependency);
	}
}

private String insertDependency(
		String pom,
		DependencyDeclaration declaration)
		throws IOException {
	
	String dependencyXml =
			dependencyXml(
					declaration);
	
	int dependenciesCloseIndex =
			pom.indexOf(
					DEPENDENCIES_CLOSE);
	
	if (dependenciesCloseIndex >= 0) {
		
		return pom.substring(
				0,
				dependenciesCloseIndex)
					   + dependencyXml
					   + pom.substring(
				dependenciesCloseIndex);
	}
	
	int projectCloseIndex =
			pom.lastIndexOf(
					PROJECT_CLOSE);
	
	if (projectCloseIndex < 0) {
		
		throw new IOException(
				"Invalid Maven POM: missing </project>");
	}
	
	String dependenciesXml =
			"\n\t<dependencies>\n"
					+ dependencyXml
					+ "\t</dependencies>\n";
	
	return pom.substring(
			0,
			projectCloseIndex)
				   + dependenciesXml
				   + pom.substring(
			projectCloseIndex);
}

private String dependencyXml(
		DependencyDeclaration declaration) {
	
	DependencyCoordinate dependency =
			declaration.coordinate();
	
	StringBuilder xml =
			new StringBuilder();
	
	xml.append(
			"\t\t<dependency>\n");
	
	xml.append(
					"\t\t\t<groupId>")
			.append(
					dependency.groupId())
			.append(
					"</groupId>\n");
	
	xml.append(
					"\t\t\t<artifactId>")
			.append(
					dependency.artifactId())
			.append(
					"</artifactId>\n");
	
	appendScope(
			xml,
			declaration.scope());
	
	xml.append(
			"\t\t</dependency>\n");
	
	return xml.toString();
}

private void appendScope(
		StringBuilder xml,
		DependencyScope scope) {
	
	switch (scope) {
		
		case COMPILE -> {
			// Maven compile is the default scope.
		}
		
		case RUNTIME -> xml.append(
				"\t\t\t<scope>runtime</scope>\n");
		
		case COMPILE_ONLY -> xml.append(
				"\t\t\t<optional>true</optional>\n");
		
		case TEST -> xml.append(
				"\t\t\t<scope>test</scope>\n");
		
		case ANNOTATION_PROCESSOR -> {
			// Maven does not represent this as a second
			// ordinary dependency declaration.
		}
	}
}

private void writeSafely(
		Path pomPath,
		String original,
		String updated)
		throws IOException {
	
	Path temporaryPath =
			pomPath.resolveSibling(
					pomPath.getFileName()
							+ ".lazyspringboot.tmp");
	
	Path backupPath =
			pomPath.resolveSibling(
					pomPath.getFileName()
							+ ".lazyspringboot.bak");
	
	try {
		
		Files.writeString(
				temporaryPath,
				updated,
				StandardCharsets.UTF_8);
		
		Files.writeString(
				backupPath,
				original,
				StandardCharsets.UTF_8);
		
		replacePom(
				temporaryPath,
				pomPath);
		
	} finally {
		
		Files.deleteIfExists(
				temporaryPath);
	}
}

private void replacePom(
		Path temporaryPath,
		Path pomPath)
		throws IOException {
	
	try {
		
		Files.move(
				temporaryPath,
				pomPath,
				StandardCopyOption.ATOMIC_MOVE,
				StandardCopyOption.REPLACE_EXISTING);
		
	} catch (
			  AtomicMoveNotSupportedException exception) {
		
		Files.move(
				temporaryPath,
				pomPath,
				StandardCopyOption.REPLACE_EXISTING);
	}
}

private void validatePom(
		String pom)
		throws IOException {
	
	if (!pom.contains(
			PROJECT_CLOSE)) {
		
		throw new IOException(
				"Invalid Maven POM: missing </project>");
	}
}
}