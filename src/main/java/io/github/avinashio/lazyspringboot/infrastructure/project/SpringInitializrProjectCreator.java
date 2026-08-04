package io.github.avinashio.lazyspringboot.infrastructure.project;

import io.github.avinashio.lazyspringboot.application.project.CreateSpringProjectUseCase;
import io.github.avinashio.lazyspringboot.domain.project.NewProjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

@Service
public class SpringInitializrProjectCreator
		implements CreateSpringProjectUseCase {

private static final int HTTP_OK = 200;

private final SpringInitializrRequestBuilder
		requestBuilder;

private final ZipExtractor
		zipExtractor;

private final HttpClient httpClient;

@Autowired
public SpringInitializrProjectCreator(
		SpringInitializrRequestBuilder requestBuilder,
		ZipExtractor zipExtractor) {
	
	this(
			requestBuilder,
			zipExtractor,
			HttpClient.newHttpClient());
}

SpringInitializrProjectCreator(
		SpringInitializrRequestBuilder requestBuilder,
		ZipExtractor zipExtractor,
		HttpClient httpClient) {
	
	this.requestBuilder =
			requestBuilder;
	
	this.zipExtractor =
			zipExtractor;
	
	this.httpClient =
			httpClient;
}

@Override
public Path create(
		NewProjectRequest request,
		Path destination)
		throws IOException,
					   InterruptedException {
	
	Path projectDirectory =
			destination.resolve(
					request.name());
	
	if (Files.exists(
			projectDirectory)) {
		
		throw new IOException(
				"Project directory already exists: "
						+ projectDirectory);
	}
	
	HttpRequest httpRequest =
			HttpRequest.newBuilder(
							requestBuilder.build(
									request))
					.GET()
					.build();
	
	HttpResponse<InputStream> response =
			httpClient.send(
					httpRequest,
					HttpResponse.BodyHandlers
							.ofInputStream());
	
	if (response.statusCode()
				!= HTTP_OK) {
		
		closeResponseBody(
				response);
		
		throw new IOException(
				"Spring Initializr rejected the project configuration"
						+ " (HTTP "
						+ response.statusCode()
						+ ")");
	}
	
	boolean projectDirectoryCreated =
			false;
	
	try (InputStream inputStream =
				 response.body()) {
		
		Files.createDirectories(
				projectDirectory);
		
		projectDirectoryCreated =
				true;
		
		zipExtractor.extract(
				inputStream,
				projectDirectory);
		
		return projectDirectory;
		
	} catch (IOException exception) {
		
		if (projectDirectoryCreated) {
			
			deleteProjectDirectory(
					projectDirectory,
					exception);
		}
		
		throw exception;
	}
}

private void closeResponseBody(
		HttpResponse<InputStream> response)
		throws IOException {
	
	InputStream body =
			response.body();
	
	if (body != null) {
		body.close();
	}
}

private void deleteProjectDirectory(
		Path projectDirectory,
		IOException originalException) {
	
	try {
		
		deleteRecursively(
				projectDirectory);
		
	} catch (IOException cleanupException) {
		
		originalException.addSuppressed(
				cleanupException);
	}
}

private void deleteRecursively(
		Path directory)
		throws IOException {
	
	if (!Files.exists(
			directory)) {
		
		return;
	}
	
	try (Stream<Path> paths =
				 Files.walk(
						 directory)) {
		
		for (Path path :
				paths.sorted(
								Comparator.reverseOrder())
						.toList()) {
			
			Files.deleteIfExists(
					path);
		}
	}
}
}