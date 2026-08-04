package io.github.avinashio.lazyspringboot.infrastructure.project;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ConfigurationFileFormat;
import io.github.avinashio.lazyspringboot.domain.project.NewProjectRequest;
import io.github.avinashio.lazyspringboot.domain.project.ProjectPackaging;
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
			.contains("packaging=jar")
			.contains("configurationFileFormat=yaml")
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

@Test
void shouldUseJarPackaging() {
	
	URI uri =
			builder.build(
					request(
							BuildTool.MAVEN,
							ProjectPackaging.JAR,
							ConfigurationFileFormat.YAML));
	
	assertThat(uri.toString())
			.contains(
					"packaging=jar");
}

@Test
void shouldUseWarPackaging() {
	
	URI uri =
			builder.build(
					request(
							BuildTool.MAVEN,
							ProjectPackaging.WAR,
							ConfigurationFileFormat.YAML));
	
	assertThat(uri.toString())
			.contains(
					"packaging=war");
}

@Test
void shouldUseYamlConfigurationFileFormat() {
	
	URI uri =
			builder.build(
					request(
							BuildTool.MAVEN,
							ProjectPackaging.JAR,
							ConfigurationFileFormat.YAML));
	
	assertThat(uri.toString())
			.contains(
					"configurationFileFormat=yaml");
}

@Test
void shouldUsePropertiesConfigurationFileFormat() {
	
	URI uri =
			builder.build(
					request(
							BuildTool.MAVEN,
							ProjectPackaging.JAR,
							ConfigurationFileFormat.PROPERTIES));
	
	assertThat(uri.toString())
			.contains(
					"configurationFileFormat=properties");
}

private NewProjectRequest request(
		BuildTool buildTool) {
	
	return request(
			buildTool,
			ProjectPackaging.JAR,
			ConfigurationFileFormat.YAML);
}

private NewProjectRequest request(
		BuildTool buildTool,
		ProjectPackaging packaging,
		ConfigurationFileFormat configurationFileFormat) {
	
	return new NewProjectRequest(
			"com.example",
			"demo",
			"demo",
			"com.example.demo",
			"21",
			"4.1.0",
			buildTool,
			packaging,
			configurationFileFormat,
			List.of(
					"web",
					"data-jpa"));
}
}