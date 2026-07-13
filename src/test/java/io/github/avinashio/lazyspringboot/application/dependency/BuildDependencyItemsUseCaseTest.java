package io.github.avinashio.lazyspringboot.application.dependency;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyAvailability;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class BuildDependencyItemsUseCaseTest {

    private final BuildDependencyItemsUseCase useCase =
            new BuildDependencyItemsUseCase(
                    new DependencyMatcher());

    @Test
    void shouldMarkExistingDependencyAsAlreadyPresent() {
        SpringProject project =
                projectWith(
                        new DependencyCoordinate(
                                "org.springframework.boot",
                                "spring-boot-starter-webmvc"));

        List<DependencyItem> items =
                useCase.build(
                        List.of(dependency("web")),
                        project);

        assertThat(items.getFirst().availability())
                .isEqualTo(
                        DependencyAvailability.ALREADY_PRESENT);
    }

    @Test
    void shouldMarkMissingDependencyAsAvailable() {
        SpringProject project =
                projectWith();

        List<DependencyItem> items =
                useCase.build(
                        List.of(dependency("web")),
                        project);

        assertThat(items.getFirst().availability())
                .isEqualTo(
                        DependencyAvailability.AVAILABLE);
    }

    private SpringDependency dependency(String id) {
        return new SpringDependency(
                id,
                "Spring Web",
                "Web applications",
                "Web");
    }

    private SpringProject projectWith(
            DependencyCoordinate... dependencies) {
        return new SpringProject(
                "testboot",
                Path.of("/projects/testboot"),
                new ProjectMetadata(
                        "com.example",
                        "testboot",
                        "4.1.0",
                        "26",
                        BuildTool.MAVEN,
                        List.of(dependencies)));
    }
}