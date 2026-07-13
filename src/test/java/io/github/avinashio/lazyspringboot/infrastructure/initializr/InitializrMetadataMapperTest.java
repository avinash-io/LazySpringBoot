package io.github.avinashio.lazyspringboot.infrastructure.initializr;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import java.util.List;
import org.junit.jupiter.api.Test;

class InitializrMetadataMapperTest {

    private final InitializrMetadataMapper mapper =
            new InitializrMetadataMapper();

    @Test
    void shouldMapDependencyMetadataToDomainDependencies() {
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
                                                                "Build web applications"))))));

        List<SpringDependency> dependencies =
                mapper.map(metadata);

        assertThat(dependencies).hasSize(1);

        SpringDependency dependency = dependencies.getFirst();

        assertThat(dependency.id()).isEqualTo("web");
        assertThat(dependency.name()).isEqualTo("Spring Web");
        assertThat(dependency.description())
                .isEqualTo("Build web applications");
        assertThat(dependency.group()).isEqualTo("Web");
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
                                                                "Persist data"))))));

        List<SpringDependency> dependencies =
                mapper.map(metadata);

        assertThat(dependencies)
                .extracting(SpringDependency::id)
                .containsExactly("web", "data-jpa");
    }
}