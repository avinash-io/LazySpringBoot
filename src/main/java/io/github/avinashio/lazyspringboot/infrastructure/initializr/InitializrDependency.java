package io.github.avinashio.lazyspringboot.infrastructure.initializr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record InitializrDependency(
        String id,
        String name,
        String description) {}