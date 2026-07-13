package io.github.avinashio.lazyspringboot.infrastructure.initializr;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Component;

@Component
public class SpringInitializrClient {

    private static final URI INITIALIZR_URI =
            URI.create("https://start.spring.io");

    private static final String USER_AGENT =
            "LazySpringBoot/0.0.1";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public SpringInitializrClient(ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = objectMapper;
    }

    public InitializrMetadata getMetadata()
            throws IOException, InterruptedException {
        HttpRequest request =
                HttpRequest.newBuilder(INITIALIZR_URI)
                        .header("Accept", "application/vnd.initializr.v2.3+json")
                        .header("User-Agent", USER_AGENT)
                        .GET()
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException(
                    "Spring Initializr returned HTTP "
                            + response.statusCode());
        }

        return objectMapper.readValue(
                response.body(),
                InitializrMetadata.class);
    }
}