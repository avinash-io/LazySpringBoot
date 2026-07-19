package io.github.avinashio.lazyspringboot.domain.initializr;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import java.util.List;

public record InitializrConfiguration(
        List<SpringDependency> dependencies,
        List<String> javaVersions,
        String defaultJavaVersion,
        List<String> springBootVersions,
        String defaultSpringBootVersion) {}