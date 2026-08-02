package io.github.avinashio.lazyspringboot.infrastructure.gradle;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class GradleDependencyWriter {

private static final String DEFAULT_INDENTATION =
		"    ";

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
	
	int dependenciesIndex =
			content.indexOf(
					"dependencies");
	
	int closingBrace =
			findDependenciesClosingBrace(
					content,
					dependenciesIndex);
	
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
	
	String indentation =
			detectDependencyIndentation(
					content,
					dependenciesIndex,
					closingBrace);
	
	StringBuilder additions =
			new StringBuilder();
	
	for (DependencyCoordinate dependency
			: dependenciesToAdd) {
		
		additions.append(
				formatDependency(
						dependency,
						kotlinDsl,
						indentation));
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
		boolean kotlinDsl,
		String indentation) {
	
	String coordinate =
			dependency.groupId()
					+ ":"
					+ dependency.artifactId();
	
	if (kotlinDsl) {
		
		return indentation
					   + "implementation(\""
					   + coordinate
					   + "\")"
					   + System.lineSeparator();
	}
	
	return indentation
				   + "implementation '"
				   + coordinate
				   + "'"
				   + System.lineSeparator();
}

private String detectDependencyIndentation(
		String content,
		int dependenciesIndex,
		int closingBrace) {
	
	int openingBrace =
			content.indexOf(
					'{',
					dependenciesIndex);
	
	if (openingBrace < 0) {
		return DEFAULT_INDENTATION;
	}
	
	int lineStart =
			content.lastIndexOf(
					'\n',
					openingBrace);
	
	String blockIndentation =
			leadingWhitespace(
					content,
					lineStart + 1);
	
	int position =
			openingBrace + 1;
	
	while (position < closingBrace) {
		
		int nextLineStart =
				content.indexOf(
						'\n',
						position);
		
		if (nextLineStart < 0
					|| nextLineStart >= closingBrace) {
			
			break;
		}
		
		nextLineStart++;
		
		int nextLineEnd =
				content.indexOf(
						'\n',
						nextLineStart);
		
		if (nextLineEnd < 0
					|| nextLineEnd > closingBrace) {
			
			nextLineEnd =
					closingBrace;
		}
		
		String line =
				content.substring(
						nextLineStart,
						nextLineEnd);
		
		if (!line.isBlank()) {
			
			String indentation =
					leadingWhitespace(
							line,
							0);
			
			if (indentation.length()
						> blockIndentation.length()) {
				
				return indentation;
			}
		}
		
		position =
				nextLineEnd;
	}
	
	return blockIndentation
				   + DEFAULT_INDENTATION;
}

private String leadingWhitespace(
		String value,
		int start) {
	
	int index =
			start;
	
	while (index < value.length()) {
		
		char character =
				value.charAt(
						index);
		
		if (character != ' '
					&& character != '\t') {
			
			break;
		}
		
		index++;
	}
	
	return value.substring(
			start,
			index);
}

private int findDependenciesClosingBrace(
		String content,
		int dependenciesIndex) {
	
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