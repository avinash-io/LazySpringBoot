package io.github.avinashio.lazyspringboot.infrastructure.gradle;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class GradleProjectInspectorTest {

private final GradleProjectInspector inspector =
		new GradleProjectInspector(
				new GradleDependencyParser());

@TempDir
Path temporaryDirectory;

@Test
void shouldSupportGradleProject() {
	
	assertThat(
			inspector.supports(
					temporaryDirectory))
			.isFalse();
}

@Test
void shouldSupportBuildGradle()
		throws IOException {
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle"),
			"");
	
	assertThat(
			inspector.supports(
					temporaryDirectory))
			.isTrue();
}

@Test
void shouldSupportBuildGradleKts()
		throws IOException {
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle.kts"),
			"");
	
	assertThat(
			inspector.supports(
					temporaryDirectory))
			.isTrue();
}

@Test
void shouldReturnBasicProjectMetadata()
		throws IOException {
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle"),
			"");
	
	ProjectMetadata metadata =
			inspector.inspect(
					temporaryDirectory);
	
	assertThat(
			metadata.artifactId())
			.isEqualTo(
					temporaryDirectory
							.getFileName()
							.toString());
	
	assertThat(
			metadata.dependencies())
			.isEmpty();
}

@Test
void shouldIdentifyGroovySpringBootProject()
		throws IOException {
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle"),
			"""
					plugins {
						id 'java'
						id 'org.springframework.boot' version '4.1.0'
					}
					""");
	
	assertThat(
			inspector.isSpringBootProject(
					temporaryDirectory))
			.isTrue();
}

@Test
void shouldIdentifyKotlinSpringBootProject()
		throws IOException {
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle.kts"),
			"""
					plugins {
						java
						id("org.springframework.boot") version "4.1.0"
					}
					""");
	
	assertThat(
			inspector.isSpringBootProject(
					temporaryDirectory))
			.isTrue();
}


@Test
void shouldRejectPlainGradleProject()
		throws IOException {
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle"),
			"""
					plugins {
						id 'java'
					}
					""");
	
	assertThat(
			inspector.isSpringBootProject(
					temporaryDirectory))
			.isFalse();
}

@Test
void shouldExtractSpringBootVersionFromGroovyBuild()
		throws IOException {
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle"),
			"""
					plugins {
						id 'java'
						id 'org.springframework.boot' version '4.1.0'
					}
					""");
	
	ProjectMetadata metadata =
			inspector.inspect(
					temporaryDirectory);
	
	assertThat(
			metadata.springBootVersion())
			.isEqualTo(
					"4.1.0");
}

@Test
void shouldExtractSpringBootVersionFromKotlinBuild()
		throws IOException {
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle.kts"),
			"""
					plugins {
						java
						id("org.springframework.boot") version "4.1.0"
					}
					""");
	
	ProjectMetadata metadata =
			inspector.inspect(
					temporaryDirectory);
	
	assertThat(
			metadata.springBootVersion())
			.isEqualTo(
					"4.1.0");
}

@Test
void shouldExtractGroupFromGroovyBuild()
		throws IOException {
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle"),
			"""
					plugins {
						id 'org.springframework.boot' version '4.1.0'
					}
					
					group = 'com.example'
					""");
	
	ProjectMetadata metadata =
			inspector.inspect(
					temporaryDirectory);
	
	assertThat(metadata.groupId())
			.isEqualTo("com.example");
}

@Test
void shouldExtractGroupFromKotlinBuild()
		throws IOException {
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle.kts"),
			"""
					plugins {
						id("org.springframework.boot") version "4.1.0"
					}
					
					group = "com.example"
					""");
	
	ProjectMetadata metadata =
			inspector.inspect(
					temporaryDirectory);
	
	assertThat(metadata.groupId())
			.isEqualTo("com.example");
}

@Test
void shouldExtractJavaVersionFromGroovyBuild()
		throws IOException {
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle"),
			"""
					plugins {
						id 'org.springframework.boot' version '4.1.0'
					}
					
					java {
						toolchain {
							languageVersion = JavaLanguageVersion.of(26)
						}
					}
					""");
	
	ProjectMetadata metadata =
			inspector.inspect(
					temporaryDirectory);
	
	assertThat(metadata.javaVersion())
			.isEqualTo("26");
}

@Test
void shouldExtractJavaVersionFromKotlinBuild()
		throws IOException {
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle.kts"),
			"""
					plugins {
						id("org.springframework.boot") version "4.1.0"
					}
					
					java {
						toolchain {
							languageVersion =
								JavaLanguageVersion.of(26)
						}
					}
					""");
	
	ProjectMetadata metadata =
			inspector.inspect(
					temporaryDirectory);
	
	assertThat(metadata.javaVersion())
			.isEqualTo("26");
}

@Test
void shouldExtractDependenciesFromGroovyBuild()
		throws IOException {
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle"),
			"""
					plugins {
						id 'org.springframework.boot' version '4.1.0'
					}
					
					dependencies {
						implementation 'org.springframework.boot:spring-boot-starter-web'
						runtimeOnly 'org.postgresql:postgresql'
						testImplementation 'org.springframework.boot:spring-boot-starter-test'
					}
					""");
	
	ProjectMetadata metadata =
			inspector.inspect(
					temporaryDirectory);
	
	assertThat(metadata.dependencies())
			.containsExactly(
					new DependencyCoordinate(
							"org.springframework.boot",
							"spring-boot-starter-web"),
					new DependencyCoordinate(
							"org.postgresql",
							"postgresql"),
					new DependencyCoordinate(
							"org.springframework.boot",
							"spring-boot-starter-test"));
}

@Test
void shouldExtractDependenciesFromKotlinBuild()
		throws IOException {
	
	Files.writeString(
			temporaryDirectory.resolve(
					"build.gradle.kts"),
			"""
					plugins {
						id("org.springframework.boot") version "4.1.0"
					}
					
					dependencies {
						implementation("org.springframework.boot:spring-boot-starter-web")
						runtimeOnly("org.postgresql:postgresql")
						testImplementation("org.springframework.boot:spring-boot-starter-test")
					}
					""");
	
	ProjectMetadata metadata =
			inspector.inspect(
					temporaryDirectory);
	
	assertThat(metadata.dependencies())
			.containsExactly(
					new DependencyCoordinate(
							"org.springframework.boot",
							"spring-boot-starter-web"),
					new DependencyCoordinate(
							"org.postgresql",
							"postgresql"),
					new DependencyCoordinate(
							"org.springframework.boot",
							"spring-boot-starter-test"));
}

}