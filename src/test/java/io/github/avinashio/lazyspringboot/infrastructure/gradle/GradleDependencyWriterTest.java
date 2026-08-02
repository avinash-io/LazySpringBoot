package io.github.avinashio.lazyspringboot.infrastructure.gradle;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyDeclaration;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyScope;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class GradleDependencyWriterTest {

@TempDir
Path temporaryDirectory;

@Test
void shouldAddDependencyToGroovyBuild()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle");
	
	Files.writeString(
			buildFile,
			"""
					plugins {
						id 'org.springframework.boot' version '4.1.0'
					}
					
					dependencies {
						implementation 'org.springframework.boot:spring-boot-starter-web'
					}
					""");
	
	GradleDependencyWriter writer =
			createWriter();
	
	writer.addDependencies(
			buildFile,
			List.of(
					declaration(
							"org.postgresql",
							"postgresql")));
	
	String content =
			Files.readString(
					buildFile);
	
	assertThat(content)
			.contains(
					"implementation 'org.postgresql:postgresql'");
}

@Test
void shouldAddDependencyToKotlinBuild()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle.kts");
	
	Files.writeString(
			buildFile,
			"""
					plugins {
						id("org.springframework.boot") version "4.1.0"
					}
					
					dependencies {
						implementation("org.springframework.boot:spring-boot-starter-web")
					}
					""");
	
	GradleDependencyWriter writer =
			createWriter();
	
	writer.addDependencies(
			buildFile,
			List.of(
					declaration(
							"org.postgresql",
							"postgresql")));
	
	String content =
			Files.readString(
					buildFile);
	
	assertThat(content)
			.contains(
					"implementation(\"org.postgresql:postgresql\")");
}

@Test
void shouldNotAddDuplicateDependency()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle");
	
	String original =
			"""
					dependencies {
						implementation 'org.postgresql:postgresql'
					}
					""";
	
	Files.writeString(
			buildFile,
			original);
	
	GradleDependencyWriter writer =
			createWriter();
	
	writer.addDependencies(
			buildFile,
			List.of(
					declaration(
							"org.postgresql",
							"postgresql")));
	
	assertThat(
			Files.readString(
					buildFile))
			.isEqualTo(
					original);
}

@Test
void shouldCreateBackupBeforeModification()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle");
	
	String original =
			"""
					dependencies {
					}
					""";
	
	Files.writeString(
			buildFile,
			original);
	
	GradleDependencyWriter writer =
			createWriter();
	
	writer.addDependencies(
			buildFile,
			List.of(
					declaration(
							"org.postgresql",
							"postgresql")));
	
	Path backup =
			temporaryDirectory.resolve(
					"build.gradle.lazyspringboot.bak");
	
	assertThat(backup)
			.exists();
	
	assertThat(
			Files.readString(
					backup))
			.isEqualTo(
					original);
}

@Test
void shouldPreserveNestedBlocksWhenAddingDependency()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle");
	
	Files.writeString(
			buildFile,
			"""
					dependencies {
						implementation('com.example:custom') {
							exclude group: 'example.old'
						}
					}
					
					tasks {
						named('test') {
							useJUnitPlatform()
						}
					}
					""");
	
	GradleDependencyWriter writer =
			createWriter();
	
	writer.addDependencies(
			buildFile,
			List.of(
					declaration(
							"org.postgresql",
							"postgresql")));
	
	String content =
			Files.readString(
					buildFile);
	
	assertThat(content)
			.contains(
					"implementation('com.example:custom')")
			.contains(
					"exclude group: 'example.old'")
			.contains(
					"implementation 'org.postgresql:postgresql'");
	
	assertThat(content)
			.contains(
					"""
							tasks {
								named('test') {
									useJUnitPlatform()
								}
							}
							""");
}

private GradleDependencyWriter createWriter() {
	
	return new GradleDependencyWriter(
			new GradleBuildBackupRestorer());
}

@Test
void shouldPreserveExistingSpaceIndentation()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle");
	
	String content =
			"dependencies {\n"
					+ "        implementation 'com.example:existing'\n"
					+ "}\n";
	
	Files.writeString(
			buildFile,
			content);
	
	GradleDependencyWriter writer =
			createWriter();
	
	writer.addDependencies(
			buildFile,
			List.of(
					declaration(
							"org.postgresql",
							"postgresql")));
	
	String updatedContent =
			Files.readString(
					buildFile);
	
	assertThat(updatedContent)
			.contains(
					"        implementation 'com.example:existing'")
			.contains(
					"        implementation 'org.postgresql:postgresql'");
}

@Test
void shouldPreserveExistingTabIndentation()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle");
	
	String content =
			"dependencies {\n"
					+ "\timplementation 'com.example:existing'\n"
					+ "}\n";
	
	Files.writeString(
			buildFile,
			content);
	
	GradleDependencyWriter writer =
			createWriter();
	
	writer.addDependencies(
			buildFile,
			List.of(
					declaration(
							"org.postgresql",
							"postgresql")));
	
	assertThat(
			Files.readString(
					buildFile))
			.contains(
					"\timplementation 'org.postgresql:postgresql'");
}

private DependencyDeclaration declaration(
		String groupId,
		String artifactId) {
	
	return new DependencyDeclaration(
			new DependencyCoordinate(
					groupId,
					artifactId),
			DependencyScope.COMPILE);
}

@Test
void shouldWriteRuntimeDependency()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle");
	
	Files.writeString(
			buildFile,
			"""
					dependencies {
					}
					""");
	
	GradleDependencyWriter writer =
			createWriter();
	
	writer.addDependencies(
			buildFile,
			List.of(
					new DependencyDeclaration(
							new DependencyCoordinate(
									"org.postgresql",
									"postgresql"),
							DependencyScope.RUNTIME)));
	
	assertThat(
			Files.readString(
					buildFile))
			.contains(
					"runtimeOnly 'org.postgresql:postgresql'");
}

@Test
void shouldWriteLombokWithDifferentConfigurations()
		throws Exception {
	
	Path buildFile =
			temporaryDirectory.resolve(
					"build.gradle");
	
	Files.writeString(
			buildFile,
			"""
					dependencies {
					}
					""");
	
	DependencyCoordinate lombok =
			new DependencyCoordinate(
					"org.projectlombok",
					"lombok");
	
	GradleDependencyWriter writer =
			createWriter();
	
	writer.addDependencies(
			buildFile,
			List.of(
					new DependencyDeclaration(
							lombok,
							DependencyScope.COMPILE_ONLY),
					new DependencyDeclaration(
							lombok,
							DependencyScope.ANNOTATION_PROCESSOR)));
	
	String content =
			Files.readString(
					buildFile);
	
	assertThat(content)
			.contains(
					"compileOnly 'org.projectlombok:lombok'")
			.contains(
					"annotationProcessor 'org.projectlombok:lombok'");
}

}