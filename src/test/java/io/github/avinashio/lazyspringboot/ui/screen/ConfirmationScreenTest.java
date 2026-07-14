package io.github.avinashio.lazyspringboot.ui.screen;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyAvailability;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.component.TextFormatter;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class ConfirmationScreenTest {

    @Test
    void shouldBuildSelectedDependencyConfirmation() {
        ConfirmationScreen screen =
                new ConfirmationScreen(
                        null,
                        new TextFormatter());

        UiState state = createState();

        List<String> lines =
                screen.buildLines(state, 120);

        assertThat(lines)
                .contains(
                        "Apply dependencies?",
                        "Project: testboot",
                        "  + PostgreSQL Driver",
                        "  + Spring Data JPA");

        assertThat(lines)
                .anyMatch(
                        line ->
                                line.contains("Enter Apply")
                                        && line.contains("Esc Cancel"));
    }

    @Test
    void shouldRenderTerminalTooSmallMessage() {
        ConfirmationScreen screen =
                new ConfirmationScreen(
                        null,
                        new TextFormatter());

        List<String> lines =
                screen.buildLines(
                        createState(),
                        40);

        assertThat(lines)
                .containsExactly(
                        "Terminal width is too small.");
    }

    private UiState createState() {
        UiState state = new UiState();

        state.setProjects(
                List.of(createProject()));

        state.setDependencyItems(
                List.of(
                        dependency(
                                "postgresql",
                                "PostgreSQL Driver"),
                        dependency(
                                "data-jpa",
                                "Spring Data JPA")));

        state.toggleSelectedDependency();

        state.selectNextDependency();
        state.toggleSelectedDependency();

        state.startDependencyConfirmation();

        return state;
    }

    private SpringProject createProject() {
        return new SpringProject(
                "testboot",
                Path.of("/workspace/testboot"),
                null);
    }

    private DependencyItem dependency(
            String id,
            String name) {
        return new DependencyItem(
                new SpringDependency(
                        id,
                        name,
                        "Test dependency",
                        "SQL"),
                DependencyAvailability.AVAILABLE,
                false);
    }
}