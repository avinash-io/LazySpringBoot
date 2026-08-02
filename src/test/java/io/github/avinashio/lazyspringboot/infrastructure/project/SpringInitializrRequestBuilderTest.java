package io.github.avinashio.lazyspringboot.infrastructure.project;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.NewProjectRequest;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SpringInitializrRequestBuilderTest {

private final SpringInitializrRequestBuilder builder =
		new SpringInitializrRequestBuilder();

@Test
void shouldBuildStarterUrl() {
	
	URI uri =
			builder.build(
					request(
							BuildTool.MAVEN));
	
	assertThat(uri.toString())
			.contains("groupId=com.example")
			.contains("artifactId=demo")
			.contains("dependencies=web%2Cdata-jpa");
}

@Test
void shouldUseMavenProjectType() {
	
	URI uri =
			builder.build(
					request(
							BuildTool.MAVEN));
	
	assertThat(uri.toString())
			.contains(
					"type=maven-project");
}

@Test
void shouldUseGradleProjectType() {
	
	URI uri =
			builder.build(
					request(
							BuildTool.GRADLE));
	
	assertThat(uri.toString())
			.contains(
					"type=gradle-project");
}

@Test
void shouldUseGradleKotlinProjectType() {
	
	URI uri =
			builder.build(
					request(
							BuildTool.GRADLE_KOTLIN));
	
	assertThat(uri.toString())
			.contains(
					"type=gradle-project-kotlin");
}

@Test
void shouldDefaultToMavenWhenBuildToolIsNull() {
	
	URI uri =
			builder.build(
					request(
							null));
	
	assertThat(uri.toString())
			.contains(
					"type=maven-project");
}

private NewProjectRequest request(
		BuildTool buildTool) {
	
	return new NewProjectRequest(
			"com.example",
			"demo",
			"demo",
			"com.example.demo",
			"21",
			"4.1.0",
			buildTool,
			List.of(
					"web",
					"data-jpa"));
}
}