package io.github.avinashio.lazyspringboot.infrastructure.initializr;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.springframework.stereotype.Component;
import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenDependencyParser;
import java.util.List;


@Component
public class InitializrDependencyCoordinateResolver {

    private static final String INITIALIZR_URL =
            "https://start.spring.io/starter.zip";

    private static final String USER_AGENT =
            "LazySpringBoot/0.0.1";

    private final MavenDependencyParser dependencyParser;

    private final HttpClient httpClient;
    private final Map<String, Optional<DependencyCoordinate>> cache =
            new HashMap<>();

    public InitializrDependencyCoordinateResolver(
            MavenDependencyParser dependencyParser) {
        this.httpClient = HttpClient.newHttpClient();
        this.dependencyParser = dependencyParser;
    }

    public Optional<DependencyCoordinate> resolve(
            SpringDependency dependency)
            throws IOException, InterruptedException {
        Optional<DependencyCoordinate> cached =
                cache.get(dependency.id());

        if (cached != null) {
            return cached;
        }

        Optional<DependencyCoordinate> coordinate =
                resolveFromInitializr(dependency.id());

        cache.put(dependency.id(), coordinate);

        return coordinate;
    }

    private Optional<DependencyCoordinate> resolveFromInitializr(
            String dependencyId)
            throws IOException, InterruptedException {
        String encodedDependencyId =
                URLEncoder.encode(
                        dependencyId,
                        StandardCharsets.UTF_8);

        URI uri =
                URI.create(
                        INITIALIZR_URL
                                + "?type=maven-project"
                                + "&language=java"
                                + "&dependencies="
                                + encodedDependencyId);

        HttpRequest request =
                HttpRequest.newBuilder(uri)
                        .header("User-Agent", USER_AGENT)
                        .GET()
                        .build();

        HttpResponse<byte[]> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofByteArray());

        if (response.statusCode() != 200) {
            return Optional.empty();
        }

        return findRequestedDependency(response.body());
    }

    private Optional<DependencyCoordinate> findRequestedDependency(
            byte[] zipContent)
            throws IOException {
        try (ZipInputStream zipInputStream =
                     new ZipInputStream(
                             new ByteArrayInputStream(zipContent))) {
            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.getName().endsWith("pom.xml")) {
                    continue;
                }

                List<DependencyCoordinate> dependencies =
                        dependencyParser.parse(zipInputStream);

                return dependencies.stream()
                        .findFirst();
            }
        }

        return Optional.empty();
    }
}