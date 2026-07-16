package io.github.avinashio.lazyspringboot.infrastructure.actuator;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Component;

@Component
public class ActuatorClient {

    private final HttpClient httpClient =
            HttpClient.newHttpClient();

    public String get(
            URI uri)
            throws IOException,
            InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder(uri)
                        .GET()
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}