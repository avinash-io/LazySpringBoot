package io.github.avinashio.lazyspringboot.application.dependency;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import org.junit.jupiter.api.Test;

class DependencyCoordinateResolverTest {

    private final DependencyCoordinateResolver resolver =
            new DependencyCoordinateResolver();

    @Test
    void shouldResolveSpringBootStarterByConvention() {
        assertThat(
                resolver.resolve(
                        dependency("data-jpa")))
                .isEqualTo(
                        new DependencyCoordinate(
                                "org.springframework.boot",
                                "spring-boot-starter-data-jpa"));
    }

    @Test
    void shouldResolveSpringWebAlias() {
        assertThat(
                resolver.resolve(
                        dependency("web")))
                .isEqualTo(
                        new DependencyCoordinate(
                                "org.springframework.boot",
                                "spring-boot-starter-webmvc"));
    }

    @Test
    void shouldResolveLombokAlias() {
        assertThat(
                resolver.resolve(
                        dependency("lombok")))
                .isEqualTo(
                        new DependencyCoordinate(
                                "org.projectlombok",
                                "lombok"));
    }

    @Test
    void shouldResolvePostgresqlAlias() {
        assertThat(
                resolver.resolve(
                        dependency("postgresql")))
                .isEqualTo(
                        new DependencyCoordinate(
                                "org.postgresql",
                                "postgresql"));
    }

    @Test
    void shouldProvideDetectionCandidates() {
        assertThat(
                resolver.candidates(
                        dependency("devtools")))
                .contains(
                        new DependencyCoordinate(
                                "org.springframework.boot",
                                "spring-boot-starter-devtools"),
                        new DependencyCoordinate(
                                "org.springframework.boot",
                                "spring-boot-devtools"));
    }

    private SpringDependency dependency(
            String id) {
        return new SpringDependency(
                id,
                id,
                "Test dependency",
                "Test");
    }
}