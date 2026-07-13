package io.github.avinashio.lazyspringboot.ui.component;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyAvailability;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import java.util.List;
import org.junit.jupiter.api.Test;

class DependencyRowBuilderTest {

    private final DependencyRowBuilder rowBuilder =
            new DependencyRowBuilder();

    @Test
    void shouldAddGroupHeaderBeforeDependencies() {
        List<DependencyItem> items =
                List.of(
                        dependency("native", "Developer Tools"),
                        dependency("lombok", "Developer Tools"));

        List<DependencyRow> rows =
                rowBuilder.build(items, items);

        assertThat(rows)
                .containsExactly(
                        new DependencyRow.GroupHeader(
                                "Developer Tools"),
                        new DependencyRow.Dependency(
                                0,
                                dependency(
                                        "native",
                                        "Developer Tools")),
                        new DependencyRow.Dependency(
                                1,
                                dependency(
                                        "lombok",
                                        "Developer Tools")));
    }

    @Test
    void shouldAddHeaderWhenGroupChanges() {
        List<DependencyItem> items =
                List.of(
                        dependency(
                                "lombok",
                                "Developer Tools"),
                        dependency("web", "Web"),
                        dependency(
                                "web-services",
                                "Web"));

        List<DependencyRow> rows =
                rowBuilder.build(items, items);

        assertThat(rows)
                .containsExactly(
                        new DependencyRow.GroupHeader(
                                "Developer Tools"),
                        new DependencyRow.Dependency(
                                0,
                                dependency(
                                        "lombok",
                                        "Developer Tools")),
                        new DependencyRow.GroupHeader("Web"),
                        new DependencyRow.Dependency(
                                1,
                                dependency("web", "Web")),
                        new DependencyRow.Dependency(
                                2,
                                dependency(
                                        "web-services",
                                        "Web")));
    }

    @Test
    void shouldReturnEmptyRowsForEmptyDependencies() {
        List<DependencyItem> items =
                List.of();

        assertThat(rowBuilder.build(items, items))
                .isEmpty();
    }

    @Test
    void shouldFindRenderedRowForDependencyIndex() {
        List<DependencyItem> items =
                List.of(
                        dependency(
                                "native",
                                "Developer Tools"),
                        dependency("web", "Web"));

        List<DependencyRow> rows =
                rowBuilder.build(items, items);

        int rowIndex =
                rowBuilder.findDependencyRowIndex(
                        rows,
                        1);

        assertThat(rowIndex).isEqualTo(3);
    }

    @Test
    void shouldPreserveOriginalDependencyIndexForFilteredItems() {
        DependencyItem nativeDependency =
                dependency(
                        "native",
                        "Developer Tools");

        DependencyItem webDependency =
                dependency(
                        "web",
                        "Web");

        DependencyItem postgresqlDependency =
                dependency(
                        "postgresql",
                        "SQL");

        List<DependencyItem> allItems =
                List.of(
                        nativeDependency,
                        webDependency,
                        postgresqlDependency);

        List<DependencyRow> rows =
                rowBuilder.build(
                        List.of(postgresqlDependency),
                        allItems);

        assertThat(rows)
                .containsExactly(
                        new DependencyRow.GroupHeader("SQL"),
                        new DependencyRow.Dependency(
                                2,
                                postgresqlDependency));
    }

    private DependencyItem dependency(
            String id,
            String group) {
        return new DependencyItem(
                new SpringDependency(
                        id,
                        id,
                        "Test dependency",
                        group),
                DependencyAvailability.AVAILABLE,
                false);
    }
}