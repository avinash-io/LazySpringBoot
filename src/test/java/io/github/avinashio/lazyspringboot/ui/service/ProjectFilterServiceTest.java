package io.github.avinashio.lazyspringboot.ui.service;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProjectFilterServiceTest {

    private final ProjectFilterService
            projectFilterService =
            new ProjectFilterService();

    @Test
    void shouldReturnAllProjectsWhenQueryIsEmpty() {

        List<SpringProject> projects =
                projects();

        List<SpringProject> result =
                projectFilterService.filter(
                        projects,
                        "");

        assertThat(result)
                .containsExactlyElementsOf(
                        projects);
    }

    @Test
    void shouldReturnAllProjectsWhenQueryIsBlank() {

        List<SpringProject> projects =
                projects();

        List<SpringProject> result =
                projectFilterService.filter(
                        projects,
                        "   ");

        assertThat(result)
                .containsExactlyElementsOf(
                        projects);
    }

    @Test
    void shouldReturnAllProjectsWhenQueryIsNull() {

        List<SpringProject> projects =
                projects();

        List<SpringProject> result =
                projectFilterService.filter(
                        projects,
                        null);

        assertThat(result)
                .containsExactlyElementsOf(
                        projects);
    }

    @Test
    void shouldFilterProjectsByName() {

        List<SpringProject> result =
                projectFilterService.filter(
                        projects(),
                        "payment");

        assertThat(result)
                .extracting(
                        SpringProject::name)
                .containsExactly(
                        "payment-service");
    }

    @Test
    void shouldFilterProjectsIgnoringCase() {

        List<SpringProject> result =
                projectFilterService.filter(
                        projects(),
                        "PAYMENT");

        assertThat(result)
                .extracting(
                        SpringProject::name)
                .containsExactly(
                        "payment-service");
    }

    @Test
    void shouldFilterProjectsByPath() {

        List<SpringProject> result =
                projectFilterService.filter(
                        projects(),
                        "backend");

        assertThat(result)
                .extracting(
                        SpringProject::name)
                .containsExactly(
                        "payment-service");
    }

    @Test
    void shouldReturnMultipleMatchingProjects() {

        List<SpringProject> result =
                projectFilterService.filter(
                        projects(),
                        "service");

        assertThat(result)
                .extracting(
                        SpringProject::name)
                .containsExactly(
                        "payment-service",
                        "user-service");
    }

    @Test
    void shouldReturnEmptyListWhenNothingMatches() {

        List<SpringProject> result =
                projectFilterService.filter(
                        projects(),
                        "does-not-exist");

        assertThat(result)
                .isEmpty();
    }

    private List<SpringProject> projects() {

        return List.of(
                project(
                        "payment-service",
                        "/workspace/backend/payment-service"),
                project(
                        "user-service",
                        "/workspace/services/user-service"),
                project(
                        "demo",
                        "/workspace/demo"));
    }

    private SpringProject project(
            String name,
            String path) {

        return new SpringProject(
                name,
                Path.of(path),
                new ProjectMetadata(
                        "com.example",
                        name,
                        "4.1.0",
                        "26",
                        BuildTool.MAVEN,
                        List.of()));
    }
}