package io.github.avinashio.lazyspringboot.infrastructure.project;

import io.github.avinashio.lazyspringboot.domain.project.NewProjectRequest;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import org.springframework.stereotype.Component;

@Component
public class SpringInitializrRequestBuilder {

    private static final String BASE_URL =
            "https://start.spring.io/starter.zip";

    public URI build(
            NewProjectRequest request) {

        StringJoiner parameters =
                new StringJoiner("&");

        add(parameters, "type",
                request.buildTool() == null
                        ? "maven-project"
                        : request.buildTool()
                        .name()
                        .toLowerCase()
                        + "-project");

        add(parameters, "groupId",
                request.groupId());

        add(parameters, "artifactId",
                request.artifactId());

        add(parameters, "name",
                request.name());

        add(parameters, "packageName",
                request.packageName());

        add(parameters, "javaVersion",
                request.javaVersion());

        add(parameters, "bootVersion",
                request.springBootVersion());

        if (!request.dependencies().isEmpty()) {
            add(parameters,
                    "dependencies",
                    String.join(
                            ",",
                            request.dependencies()));
        }

        return URI.create(
                BASE_URL
                        + "?"
                        + parameters);
    }

    private void add(
            StringJoiner joiner,
            String key,
            String value) {

        if (value == null
                || value.isBlank()) {
            return;
        }

        joiner.add(
                encode(key)
                        + "="
                        + encode(value));
    }

    private String encode(
            String value) {

        return URLEncoder.encode(
                value,
                StandardCharsets.UTF_8);
    }
}