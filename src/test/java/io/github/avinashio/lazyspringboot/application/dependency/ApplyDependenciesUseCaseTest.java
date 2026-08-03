package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.dependency.*;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.infrastructure.gradle.GradleBuildBackupRestorer;
import io.github.avinashio.lazyspringboot.infrastructure.gradle.GradleDependencyWriter;
import io.github.avinashio.lazyspringboot.infrastructure.gradle.GradleProjectDependencyWriter;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenDependencyParser;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenPomBackupRestorer;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenPomDependencyWriter;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenProjectDependencyWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ApplyDependenciesUseCaseTest {

@TempDir
Path temporaryDirectory;

@Test
void shouldApplySelectedDependenciesToPom()
		throws Exception {
	
	Path pomPath =
			temporaryDirectory.resolve(
					"pom.xml");
	
	Files.writeString(
			pomPath,
			"""
					<project>
					  <groupId>com.example</groupId>
					  <artifactId>demo</artifactId>
					</project>
					""");
	
	ApplyDependenciesUseCase useCase =
			createMavenUseCase();
	
	useCase.apply(
			project(
					BuildTool.MAVEN),
			List.of(
					selectedDependency(
							"postgresql"),
					selectedDependency(
							"data-jpa")));
	
	try (var inputStream =
				 Files.newInputStream(
						 pomPath)) {
		
		assertThat(
				new MavenDependencyParser()
						.parse(
								inputStream))
				.contains(
						new DependencyCoordinate(
								"org.postgresql",
								"postgresql"),
						new DependencyCoordinate(
								"org.springframework.boot",
								"spring-boot-starter-data-jpa"));
	}
	
	String content =
			Files.readString(
					pomPath);
	
	assertThat(content)
			.contains(
					"<scope>runtime</scope>");
}

@Test
void shouldApplyLombokToPomAsOptionalDependency()
		throws Exception {
	
	Path pomPath =
			temporaryDirectory.resolve(
					"pom.xml");
	
	Files.writeString(
			pomPath,
			"""
					<project>
					  <dependencies>
					  </dependencies>
					</project>
					""");
	
	ApplyDependenciesUseCase useCase =
			createMavenUseCase();
	
	useCase.apply(
			project(
					BuildTool.MAVEN),
			List.of(
					selectedDependency(
							"lombok")));
	
	String content =
			Files.readString(
					pomPath);
	
	assertThat(content)
			.contains(
					"<groupId>org.projectlombok</groupId>")
			.contains(
					"<artifactId>lombok</artifactId>")
			.contains(
					"<optional>true</optional>");
	
	assertThat(
			countOccurrences(
					content,
					"<artifactId>lombok</artifactId>"))
			.isEqualTo(1);
}

@Test
void shouldIgnoreAlreadyPresentDependencyItems()
		throws Exception {
	
	Path pomPath =
			temporaryDirectory.resolve(
					"pom.xml");
	
	String original =
			"""
					<project>
					  <dependencies>
						<dependency>
						  <groupId>org.projectlombok</groupId>
						  <artifactId>lombok</artifactId>
						</dependency>
					  </dependencies>
					</project>
					""";
	
	Files.writeString(
			pomPath,
			original);
	
	ApplyDependenciesUseCase useCase =
			createMavenUseCase();
	
	useCase.apply(
			project(
					BuildTool.MAVEN),
			List.of(
					new DependencyItem(
							dependency(
									"lombok"),
							DependencyAvailability.ALREADY_PRESENT,
							false)));
	
	assertThat(
			Files.readString(
					pomPath))
			.isEqualTo(
					original);
}

@Test
void shouldIgnoreUnselectedAvailableDependencies()
		throws Exception {
	
	Path pomPath =
			temporaryDirectory.resolve(
					"pom.xml");
	
	String original =
			"""
					<project>
					</project>
					""";
	
	Files.writeString(
			pomPath,
			original);
	
	ApplyDependenciesUseCase useCase =
			createMavenUseCase();
	
	useCase.apply(
			project(
					BuildTool.MAVEN),
			List.of(
					new DependencyItem(
							dependency(
									"postgresql"),
							DependencyAvailability.AVAILABLE,
							false)));
	
	assertThat(
			Files.readString(
					pomPath))
			.isEqualTo(
					original);
}

@Test
void shouldApplySelectedDependenciesToGradleBuild()
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
	
	ApplyDependenciesUseCase useCase =
			createGradleUseCase();
	
	useCase.apply(
			project(
					BuildTool.GRADLE),
			List.of(
					selectedDependency(
							"postgresql"),
					selectedDependency(
							"data-jpa")));
	
	String content =
			Files.readString(
					buildFile);
	
	assertThat(content)
			.contains(
					"runtimeOnly 'org.postgresql:postgresql'")
			.contains(
					"implementation 'org.springframework.boot:spring-boot-starter-data-jpa'");
}

@Test
void shouldApplySelectedDependenciesToGradleKotlinBuild()
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
	
	ApplyDependenciesUseCase useCase =
			createGradleUseCase();
	
	useCase.apply(
			project(
					BuildTool.GRADLE_KOTLIN),
			List.of(
					selectedDependency(
							"postgresql")));
	
	assertThat(
			Files.readString(
					buildFile))
			.contains(
					"runtimeOnly(\"org.postgresql:postgresql\")");
}

@Test
void shouldApplyLombokWithBothGradleConfigurations()
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
	
	ApplyDependenciesUseCase useCase =
			createGradleUseCase();
	
	useCase.apply(
			project(
					BuildTool.GRADLE),
			List.of(
					selectedDependency(
							"lombok")));
	
	String content =
			Files.readString(
					buildFile);
	
	assertThat(content)
			.contains(
					"compileOnly 'org.projectlombok:lombok'")
			.contains(
					"annotationProcessor 'org.projectlombok:lombok'");
}

private ApplyDependenciesUseCase createMavenUseCase() {
	
	MavenDependencyParser dependencyParser =
			new MavenDependencyParser();
	
	MavenProjectDependencyWriter writer =
			new MavenProjectDependencyWriter(
					new MavenPomDependencyWriter(
							dependencyParser),
					new MavenPomBackupRestorer());
	
	return new ApplyDependenciesUseCase(
			declarationResolver(),
			List.of(
					writer));
}

private ApplyDependenciesUseCase createGradleUseCase() {
	
	GradleBuildBackupRestorer backupRestorer =
			new GradleBuildBackupRestorer();
	
	GradleProjectDependencyWriter writer =
			new GradleProjectDependencyWriter(
					new GradleDependencyWriter(
							backupRestorer),
					backupRestorer);
	
	return new ApplyDependenciesUseCase(
			declarationResolver(),
			List.of(
					writer));
}

private DependencyDeclarationResolver declarationResolver() {
	
	DependencyDeclarationProvider provider =
			dependency ->
					Optional.of(
							switch (dependency.id()) {
								
								case "postgresql" -> List.of(
										new DependencyDeclaration(
												new DependencyCoordinate(
														"org.postgresql",
														"postgresql"),
												DependencyScope.RUNTIME));
								
								case "lombok" -> List.of(
										new DependencyDeclaration(
												new DependencyCoordinate(
														"org.projectlombok",
														"lombok"),
												DependencyScope.COMPILE_ONLY),
										new DependencyDeclaration(
												new DependencyCoordinate(
														"org.projectlombok",
														"lombok"),
												DependencyScope.ANNOTATION_PROCESSOR));
								
								default -> List.of(
										new DependencyDeclaration(
												new DependencyCoordinate(
														"org.springframework.boot",
														"spring-boot-starter-"
																+ dependency.id()),
												DependencyScope.COMPILE));
							});
	
	return new DependencyDeclarationResolver(
			new DependencyCoordinateResolver(),
			provider);
}

private DependencyItem selectedDependency(
		String id) {
	
	return new DependencyItem(
			dependency(
					id),
			DependencyAvailability.AVAILABLE,
			true);
}

private SpringDependency dependency(
		String id) {
	
	return new SpringDependency(
			id,
			id,
			"Test dependency",
			"Test");
}

private SpringProject project(
		BuildTool buildTool) {
	
	return new SpringProject(
			"demo",
			temporaryDirectory,
			buildTool,
			new ProjectMetadata(
					"com.example",
					"demo",
					"4.1.0",
					"26",
					List.of()));
}

private int countOccurrences(
		String value,
		String target) {
	
	return value
				   .split(
						   java.util.regex.Pattern.quote(
								   target),
						   -1)
				   .length
				   - 1;
}
}