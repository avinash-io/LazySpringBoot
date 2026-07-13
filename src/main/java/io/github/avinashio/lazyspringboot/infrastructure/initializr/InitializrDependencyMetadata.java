package io.github.avinashio.lazyspringboot.infrastructure.initializr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record InitializrDependencyMetadata(
        List<InitializrDependencyGroup> values) {}