package io.github.avinashio.lazyspringboot.infrastructure.gradle;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyDeclaration;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyScope;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GradleDependencyParserTest {

private final GradleDependencyParser parser =
		new GradleDependencyParser();

@Test
void shouldParseGroovyDependencies() {
	
	String content =
			"""
					dependencies {
						implementation 'org.springframework.boot:spring-boot-starter-web'
						runtimeOnly 'org.postgresql:postgresql'
					}
					""";
	
	assertThat(
			parser.parse(
					content))
			.containsExactly(
					new DependencyCoordinate(
							"org.springframework.boot",
							"spring-boot-starter-web"),
					new DependencyCoordinate(
							"org.postgresql",
							"postgresql"));
}

@Test
void shouldParseKotlinDependencies() {
	
	String content =
			"""
					dependencies {
						implementation("org.springframework.boot:spring-boot-starter-web")
						runtimeOnly("org.postgresql:postgresql")
					}
					""";
	
	assertThat(
			parser.parse(
					content))
			.containsExactly(
					new DependencyCoordinate(
							"org.springframework.boot",
							"spring-boot-starter-web"),
					new DependencyCoordinate(
							"org.postgresql",
							"postgresql"));
}

@Test
void shouldParseDependencyScopes() {
	
	String content =
			"""
					dependencies {
						implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
						runtimeOnly 'org.postgresql:postgresql'
						compileOnly 'org.projectlombok:lombok'
						annotationProcessor 'org.projectlombok:lombok'
					}
					""";
	
	assertThat(
			parser.parseDeclarations(
					content))
			.containsExactly(
					new DependencyDeclaration(
							new DependencyCoordinate(
									"org.springframework.boot",
									"spring-boot-starter-data-jpa"),
							DependencyScope.COMPILE),
					new DependencyDeclaration(
							new DependencyCoordinate(
									"org.postgresql",
									"postgresql"),
							DependencyScope.RUNTIME),
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
}

@Test
void shouldReturnLombokOnceWhenParsingCoordinates() {
	
	String content =
			"""
					dependencies {
						compileOnly 'org.projectlombok:lombok'
						annotationProcessor 'org.projectlombok:lombok'
					}
					""";
	
	assertThat(
			parser.parse(
					content))
			.containsExactly(
					new DependencyCoordinate(
							"org.projectlombok",
							"lombok"));
}

@Test
void shouldParseKotlinDslDeclarations() {
	
	String content =
			"""
					dependencies {
						runtimeOnly("org.postgresql:postgresql")
						compileOnly("org.projectlombok:lombok")
						annotationProcessor("org.projectlombok:lombok")
					}
					""";
	
	assertThat(
			parser.parseDeclarations(
					content))
			.extracting(
					DependencyDeclaration::scope)
			.containsExactly(
					DependencyScope.RUNTIME,
					DependencyScope.COMPILE_ONLY,
					DependencyScope.ANNOTATION_PROCESSOR);
}

@Test
void shouldIgnoreUnsupportedGradleConfigurations() {
	
	String content =
			"""
					dependencies {
						customConfiguration 'com.example:custom:1.0.0'
						implementation 'com.example:supported:1.0.0'
					}
					""";
	
	assertThat(
			parser.parse(
					content))
			.containsExactly(
					new DependencyCoordinate(
							"com.example",
							"supported"));
}

@Test
void shouldIgnoreDependencyVersions() {
	
	String content =
			"""
					dependencies {
						implementation 'com.example:demo:1.2.3'
					}
					""";
	
	assertThat(
			parser.parse(
					content))
			.containsExactly(
					new DependencyCoordinate(
							"com.example",
							"demo"));
}
}