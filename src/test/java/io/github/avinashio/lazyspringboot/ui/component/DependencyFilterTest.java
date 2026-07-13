package io.github.avinashio.lazyspringboot.ui.component;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyAvailability;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import java.util.List;
import org.junit.jupiter.api.Test;

class DependencyFilterTest {

    private final DependencyFilter filter =
            new DependencyFilter();

    @Test
    void shouldReturnAllDependenciesForEmptyQuery() {
        List<DependencyItem> items =
                List.of(
                        dependency(
                                "web",
                                "Spring Web",
                                "Web"),
                        dependency(
                                "postgresql",
                                "PostgreSQL Driver",
                                "SQL"));

        assertThat(filter.filter(items, ""))
                .containsExactlyElementsOf(items);
    }

    @Test
    void shouldFilterByDependencyName() {
        List<DependencyItem> result =
                filter.filter(
                        dependencies(),
                        "postgres");

        assertThat(result)
                .extracting(
                        item -> item.dependency().id())
                .containsExactly("postgresql");
    }

    @Test
    void shouldFilterIgnoringCase() {
        List<DependencyItem> result =
                filter.filter(
                        dependencies(),
                        "SPRING WEB");

        assertThat(result)
                .extracting(
                        item -> item.dependency().id())
                .containsExactly("web");
    }

    @Test
    void shouldFilterByGroup() {
        List<DependencyItem> result =
                filter.filter(
                        dependencies(),
                        "sql");

        assertThat(result)
                .extracting(
                        item -> item.dependency().id())
                .containsExactly("postgresql");
    }

    @Test
    void shouldReturnEmptyListWhenNothingMatches() {
        assertThat(
                filter.filter(
                        dependencies(),
                        "something-impossible"))
                .isEmpty();
    }

    private List<DependencyItem> dependencies() {
        return List.of(
                dependency(
                        "web",
                        "Spring Web",
                        "Web"),
                dependency(
                        "postgresql",
                        "PostgreSQL Driver",
                        "SQL"));
    }

    private DependencyItem dependency(
            String id,
            String name,
            String group) {
        return new DependencyItem(
                new SpringDependency(
                        id,
                        name,
                        "Test dependency",
                        group),
                DependencyAvailability.AVAILABLE,
                false);
    }

    @Test
    void shouldReturnMatchingOriginalIndexes() {
        assertThat(
                filter.matchingIndexes(
                        dependencies(),
                        "postgres"))
                .containsExactly(1);
    }
}