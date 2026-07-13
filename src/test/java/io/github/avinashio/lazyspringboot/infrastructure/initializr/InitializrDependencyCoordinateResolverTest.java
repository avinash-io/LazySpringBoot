package io.github.avinashio.lazyspringboot.infrastructure.initializr;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import java.util.Optional;

import io.github.avinashio.lazyspringboot.infrastructure.maven.MavenDependencyParser;
import org.junit.jupiter.api.Test;

class InitializrDependencyCoordinateResolverTest {

    @Test
    void shouldResolveSpringWebCoordinate()
            throws Exception {
        InitializrDependencyCoordinateResolver resolver =
                new InitializrDependencyCoordinateResolver(
                        new MavenDependencyParser());

        SpringDependency dependency =
                new SpringDependency(
                        "web",
                        "Spring Web",
                        "Web applications",
                        "Web");

        Optional<DependencyCoordinate> coordinate =
                resolver.resolve(dependency);

        assertThat(coordinate)
                .contains(
                        new DependencyCoordinate(
                                "org.springframework.boot",
                                "spring-boot-starter-webmvc"));
    }
}