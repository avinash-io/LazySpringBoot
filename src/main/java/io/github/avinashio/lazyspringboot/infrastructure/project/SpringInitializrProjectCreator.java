package io.github.avinashio.lazyspringboot.infrastructure.project;

import io.github.avinashio.lazyspringboot.application.project.CreateSpringProjectUseCase;
import io.github.avinashio.lazyspringboot.domain.project.NewProjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Service;

@Service
public class SpringInitializrProjectCreator
        implements CreateSpringProjectUseCase {

    private final SpringInitializrRequestBuilder
            requestBuilder;

    private final ZipExtractor
            zipExtractor;

    private final HttpClient httpClient;

    public SpringInitializrProjectCreator(
            SpringInitializrRequestBuilder requestBuilder,
            ZipExtractor zipExtractor) {

        this.requestBuilder =
                requestBuilder;
        this.zipExtractor =
                zipExtractor;
        this.httpClient =
                HttpClient.newHttpClient();
    }

    @Override
    public Path create(
            NewProjectRequest request,
            Path destination)
            throws IOException,
            InterruptedException {

        Files.createDirectories(destination);

        HttpRequest httpRequest =
                HttpRequest.newBuilder(
                                requestBuilder.build(request))
                        .GET()
                        .build();

        HttpResponse<InputStream> response =
                httpClient.send(
                        httpRequest,
                        HttpResponse.BodyHandlers.ofInputStream());

        if (response.statusCode() != 200) {
            throw new IOException(
                    "Spring Initializr returned HTTP "
                            + response.statusCode());
        }

        try (InputStream inputStream =
                     response.body()) {

            zipExtractor.extract(
                    inputStream,
                    destination);
        }

        return destination.resolve(
                request.name());
    }
}