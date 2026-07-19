package io.github.avinashio.lazyspringboot.infrastructure.initializr;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.initializr.InitializrConfiguration;
import java.util.List;
import org.junit.jupiter.api.Test;

class InitializrMetadataMapperTest {

    private final InitializrMetadataMapper mapper =
            new InitializrMetadataMapper();

    @Test
    void shouldMapInitializrMetadataToConfiguration() {

        InitializrMetadata metadata =
                new InitializrMetadata(
                        new InitializrDependencyMetadata(
                                List.of(
                                        new InitializrDependencyGroup(
                                                "Web",
                                                List.of(
                                                        new InitializrDependency(
                                                                "web",
                                                                "Spring Web",
                                                                "Build web applications"))))),
                        new InitializrOptionMetadata(
                                "21",
                                List.of(
                                        new InitializrOption(
                                                "17",
                                                "17"),
                                        new InitializrOption(
                                                "21",
                                                "21"))),
                        new InitializrOptionMetadata(
                                "4.1.0",
                                List.of(
                                        new InitializrOption(
                                                "4.0.0",
                                                "4.0.0"),
                                        new InitializrOption(
                                                "4.1.0",
                                                "4.1.0"))));

        InitializrConfiguration configuration =
                mapper.map(
                        metadata);

        assertThat(
                configuration.dependencies())
                .hasSize(1);

        assertThat(
                configuration.dependencies()
                        .getFirst()
                        .id())
                .isEqualTo("web");

        assertThat(
                configuration.dependencies()
                        .getFirst()
                        .name())
                .isEqualTo(
                        "Spring Web");

        assertThat(
                configuration.dependencies()
                        .getFirst()
                        .description())
                .isEqualTo(
                        "Build web applications");

        assertThat(
                configuration.dependencies()
                        .getFirst()
                        .group())
                .isEqualTo("Web");

        assertThat(
                configuration.javaVersions())
                .containsExactly(
                        "17",
                        "21");

        assertThat(
                configuration.defaultJavaVersion())
                .isEqualTo("21");

        assertThat(
                configuration.springBootVersions())
                .containsExactly(
                        "4.0.0",
                        "4.1.0");

        assertThat(
                configuration.defaultSpringBootVersion())
                .isEqualTo("4.1.0");
    }

    @Test
    void shouldFlattenMultipleDependencyGroups() {

        InitializrMetadata metadata =
                new InitializrMetadata(
                        new InitializrDependencyMetadata(
                                List.of(
                                        new InitializrDependencyGroup(
                                                "Web",
                                                List.of(
                                                        new InitializrDependency(
                                                                "web",
                                                                "Spring Web",
                                                                "Web applications"))),
                                        new InitializrDependencyGroup(
                                                "SQL",
                                                List.of(
                                                        new InitializrDependency(
                                                                "data-jpa",
                                                                "Spring Data JPA",
                                                                "Persist data"))))),
                        new InitializrOptionMetadata(
                                "21",
                                List.of(
                                        new InitializrOption(
                                                "21",
                                                "21"))),
                        new InitializrOptionMetadata(
                                "4.1.0",
                                List.of(
                                        new InitializrOption(
                                                "4.1.0",
                                                "4.1.0"))));

        InitializrConfiguration configuration =
                mapper.map(
                        metadata);

        assertThat(
                configuration.dependencies())
                .extracting(
                        dependency ->
                                dependency.id())
                .containsExactly(
                        "web",
                        "data-jpa");
    }
}