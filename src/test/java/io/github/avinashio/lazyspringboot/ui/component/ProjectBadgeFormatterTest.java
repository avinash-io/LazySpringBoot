package io.github.avinashio.lazyspringboot.ui.component;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProjectBadgeFormatterTest {

    private final ProjectBadgeFormatter formatter =
            new ProjectBadgeFormatter();

    @Test
    void shouldFormatWebBadge() {

        SpringProject project =
                projectWithDependencies(
                        dependency(
                                "spring-boot-starter-webmvc"));

        assertThat(formatter.format(project))
                .isEqualTo("[W]");
    }

    @Test
    void shouldFormatJpaBadge() {

        SpringProject project =
                projectWithDependencies(
                        dependency(
                                "spring-boot-starter-data-jpa"));

        assertThat(formatter.format(project))
                .isEqualTo("[J]");
    }

    @Test
    void shouldFormatSecurityBadge() {

        SpringProject project =
                projectWithDependencies(
                        dependency(
                                "spring-boot-starter-security"));

        assertThat(formatter.format(project))
                .isEqualTo("[S]");
    }

    @Test
    void shouldFormatActuatorBadge() {

        SpringProject project =
                projectWithDependencies(
                        dependency(
                                "spring-boot-starter-actuator"));

        assertThat(formatter.format(project))
                .isEqualTo("[A]");
    }

    @Test
    void shouldFormatMultipleBadges() {

        SpringProject project =
                projectWithDependencies(
                        dependency(
                                "spring-boot-starter-webmvc"),
                        dependency(
                                "spring-boot-starter-data-jpa"),
                        dependency(
                                "spring-boot-starter-security"),
                        dependency(
                                "spring-boot-starter-actuator"));

        assertThat(formatter.format(project))
                .isEqualTo(
                        "[W] [J] [S] [A]");
    }

    @Test
    void shouldReturnEmptyWhenNoKnownDependenciesExist() {

        SpringProject project =
                projectWithDependencies(
                        dependency("lombok"));

        assertThat(formatter.format(project))
                .isEmpty();
    }

    private SpringProject projectWithDependencies(
            DependencyCoordinate... dependencies) {

        return new SpringProject(
                "demo",
                Path.of("/demo"),
                new ProjectMetadata(
                        "com.example",
                        "demo",
                        "4.1.0",
                        "26",
                        BuildTool.MAVEN,
                        List.of(dependencies)));
    }

    private DependencyCoordinate dependency(
            String artifactId) {

        return new DependencyCoordinate(
                "org.springframework.boot",
                artifactId);
    }
}