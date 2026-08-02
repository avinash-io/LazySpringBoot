package io.github.avinashio.lazyspringboot.infrastructure.gradle;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class GradleDependencyWriter {

private final GradleDependencyParser
		dependencyParser;

private final GradleBuildBackupRestorer
		backupRestorer;

public GradleDependencyWriter(
		GradleDependencyParser dependencyParser,
		GradleBuildBackupRestorer backupRestorer) {
	
	this.dependencyParser =
			dependencyParser;
	
	this.backupRestorer =
			backupRestorer;
}

public void addDependencies(
		Path buildFile,
		List<DependencyCoordinate> dependencies)
		throws IOException {
	
	String content =
			Files.readString(
					buildFile);
	
	List<DependencyCoordinate> existingDependencies =
			dependencyParser.parse(
					content);
	
	List<DependencyCoordinate> dependenciesToAdd =
			dependencies.stream()
					.filter(dependency ->
									!existingDependencies.contains(
											dependency))
					.toList();
	
	if (dependenciesToAdd.isEmpty()) {
		return;
	}
	
	int closingBrace =
			findDependenciesClosingBrace(
					content);
	
	if (closingBrace < 0) {
		throw new IOException(
				"Gradle dependencies block not found");
	}
	
	backupRestorer.createBackup(
			buildFile);
	
	boolean kotlinDsl =
			buildFile.getFileName()
					.toString()
					.endsWith(".kts");
	
	StringBuilder additions =
			new StringBuilder();
	
	for (DependencyCoordinate dependency
			: dependenciesToAdd) {
		
		additions.append(
				formatDependency(
						dependency,
						kotlinDsl));
	}
	
	String updatedContent =
			content.substring(
					0,
					closingBrace)
					+ additions
					+ content.substring(
					closingBrace);
	
	Files.writeString(
			buildFile,
			updatedContent);
}

private String formatDependency(
		DependencyCoordinate dependency,
		boolean kotlinDsl) {
	
	String coordinate =
			dependency.groupId()
					+ ":"
					+ dependency.artifactId();
	
	if (kotlinDsl) {
		
		return "    implementation(\""
					   + coordinate
					   + "\")"
					   + System.lineSeparator();
	}
	
	return "    implementation '"
				   + coordinate
				   + "'"
				   + System.lineSeparator();
}

private int findDependenciesClosingBrace(
		String content) {
	
	int dependenciesIndex =
			content.indexOf(
					"dependencies");
	
	if (dependenciesIndex < 0) {
		return -1;
	}
	
	int openingBrace =
			content.indexOf(
					'{',
					dependenciesIndex);
	
	if (openingBrace < 0) {
		return -1;
	}
	
	int depth = 0;
	
	for (int index = openingBrace;
		 index < content.length();
		 index++) {
		
		char character =
				content.charAt(
						index);
		
		if (character == '{') {
			depth++;
		} else if (character == '}') {
			
			depth--;
			
			if (depth == 0) {
				return index;
			}
		}
	}
	
	return -1;
}
}