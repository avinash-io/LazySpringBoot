package io.github.avinashio.lazyspringboot.infrastructure.initializr;

import io.github.avinashio.lazyspringboot.application.dependency.DependencyDeclarationProvider;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyDeclaration;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.infrastructure.gradle.GradleDependencyParser;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class InitializrDependencyDeclarationResolver
		implements DependencyDeclarationProvider {

private static final String INITIALIZR_URL =
		"https://start.spring.io/starter.zip";

private static final String USER_AGENT =
		"LazySpringBoot";

private final HttpClient httpClient;

private final GradleDependencyParser dependencyParser;

public InitializrDependencyDeclarationResolver(
		HttpClient httpClient,
		GradleDependencyParser dependencyParser) {
	
	this.httpClient =
			httpClient;
	
	this.dependencyParser =
			dependencyParser;
}

@Override
public Optional<List<DependencyDeclaration>> resolve(
		SpringDependency dependency) {
	
	try {
		
		return resolveFromInitializr(
				dependency.id());
		
	} catch (
			  IOException
			  | InterruptedException exception) {
		
		if (exception
					instanceof InterruptedException) {
			
			Thread.currentThread()
					.interrupt();
		}
		
		return Optional.empty();
	}
}

private Optional<List<DependencyDeclaration>>
resolveFromInitializr(
		String dependencyId)
		throws IOException,
					   InterruptedException {
	
	String encodedDependencyId =
			URLEncoder.encode(
					dependencyId,
					StandardCharsets.UTF_8);
	
	URI uri =
			URI.create(
					INITIALIZR_URL
							+ "?type=gradle-project"
							+ "&language=java"
							+ "&dependencies="
							+ encodedDependencyId);
	
	HttpRequest request =
			HttpRequest.newBuilder(uri)
					.header(
							"User-Agent",
							USER_AGENT)
					.GET()
					.build();
	
	HttpResponse<byte[]> response =
			httpClient.send(
					request,
					HttpResponse.BodyHandlers
							.ofByteArray());
	
	if (response.statusCode() != 200) {
		return Optional.empty();
	}
	
	return findDeclarations(
			response.body(),
			dependencyId);
}

private Optional<List<DependencyDeclaration>>
findDeclarations(
		byte[] zipBytes,
		String dependencyId)
		throws IOException {
	
	try (ZipInputStream zipInputStream =
				 new ZipInputStream(
						 new ByteArrayInputStream(
								 zipBytes))) {
		
		ZipEntry entry;
		
		while ((entry =
						zipInputStream.getNextEntry())
					   != null) {
			
			if (!entry.getName()
						 .endsWith(
								 "build.gradle")) {
				
				continue;
			}
			
			String buildFile =
					new String(
							zipInputStream
									.readAllBytes(),
							StandardCharsets.UTF_8);
			
			List<DependencyDeclaration> declarations =
					dependencyParser
							.parseDeclarations(
									buildFile);
			
			List<DependencyDeclaration> matchingDeclarations =
					declarations.stream()
							.filter(
									declaration ->
											matchesDependency(
													declaration,
													dependencyId))
							.toList();
			
			if (matchingDeclarations.isEmpty()) {
				return Optional.empty();
			}
			
			return Optional.of(
					matchingDeclarations);
		}
	}
	
	return Optional.empty();
}

private boolean matchesDependency(
		DependencyDeclaration declaration,
		String dependencyId) {
	
	String artifactId =
			declaration.coordinate()
					.artifactId();
	
	return artifactId.equals(
			dependencyId)
				   || artifactId.endsWith(
			"-" + dependencyId);
}
}