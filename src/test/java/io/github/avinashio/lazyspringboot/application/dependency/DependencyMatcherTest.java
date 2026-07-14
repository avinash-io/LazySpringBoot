package io.github.avinashio.lazyspringboot.application.dependency;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DependencyMatcherTest {

    private final DependencyMatcher matcher =
            new DependencyMatcher(
                    new DependencyCoordinateResolver());

    @Test
    void shouldMatchSpringBootStarterDependency() {
        SpringDependency dependency =
                dependency("data-jpa");

        Set<DependencyCoordinate> existingDependencies =
                Set.of(
                        new DependencyCoordinate(
                                "org.springframework.boot",
                                "spring-boot-starter-data-jpa"));

        assertThat(
                matcher.isAlreadyPresent(
                        dependency,
                        existingDependencies))
                .isTrue();
    }

    @Test
    void shouldMatchSpringBootArtifact() {
        SpringDependency dependency =
                dependency("devtools");

        Set<DependencyCoordinate> existingDependencies =
                Set.of(
                        new DependencyCoordinate(
                                "org.springframework.boot",
                                "spring-boot-devtools"));

        assertThat(
                matcher.isAlreadyPresent(
                        dependency,
                        existingDependencies))
                .isTrue();
    }

    @Test
    void shouldMatchSpringWebMvcAlias() {
        SpringDependency dependency =
                dependency("web");

        Set<DependencyCoordinate> existingDependencies =
                Set.of(
                        new DependencyCoordinate(
                                "org.springframework.boot",
                                "spring-boot-starter-webmvc"));

        assertThat(
                matcher.isAlreadyPresent(
                        dependency,
                        existingDependencies))
                .isTrue();
    }

    @Test
    void shouldMatchLombokAlias() {
        SpringDependency dependency =
                dependency("lombok");

        Set<DependencyCoordinate> existingDependencies =
                Set.of(
                        new DependencyCoordinate(
                                "org.projectlombok",
                                "lombok"));

        assertThat(
                matcher.isAlreadyPresent(
                        dependency,
                        existingDependencies))
                .isTrue();
    }

    @Test
    void shouldMatchDirectArtifactId() {
        SpringDependency dependency =
                dependency("lombok");

        Set<DependencyCoordinate> existingDependencies =
                Set.of(
                        new DependencyCoordinate(
                                "org.projectlombok",
                                "lombok"));

        assertThat(
                matcher.isAlreadyPresent(
                        dependency,
                        existingDependencies))
                .isTrue();
    }

    @Test
    void shouldNotMatchUnrelatedDependency() {
        SpringDependency dependency =
                dependency("web");

        Set<DependencyCoordinate> existingDependencies =
                Set.of(
                        new DependencyCoordinate(
                                "org.springframework.boot",
                                "spring-boot-starter-data-jpa"));

        assertThat(
                matcher.isAlreadyPresent(
                        dependency,
                        existingDependencies))
                .isFalse();
    }

    @Test
    void shouldNotUsePartialArtifactIdMatching() {
        SpringDependency dependency =
                dependency("web");

        Set<DependencyCoordinate> existingDependencies =
                Set.of(
                        new DependencyCoordinate(
                                "com.example",
                                "my-web-tools"));

        assertThat(
                matcher.isAlreadyPresent(
                        dependency,
                        existingDependencies))
                .isFalse();
    }

    private SpringDependency dependency(String id) {
        return new SpringDependency(
                id,
                id,
                "Test dependency",
                "Test");
    }


}