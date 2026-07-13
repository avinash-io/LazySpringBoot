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
        List<DependencyRow> rows =
                rowBuilder.build(
                        List.of(
                                dependency("native", "Developer Tools"),
                                dependency("lombok", "Developer Tools")));

        assertThat(rows)
                .containsExactly(
                        new DependencyRow.GroupHeader(
                                "Developer Tools"),
                        new DependencyRow.Dependency(
                                0,
                                dependency("native", "Developer Tools")),
                        new DependencyRow.Dependency(
                                1,
                                dependency("lombok", "Developer Tools")));
    }

    @Test
    void shouldAddHeaderWhenGroupChanges() {
        List<DependencyRow> rows =
                rowBuilder.build(
                        List.of(
                                dependency("lombok", "Developer Tools"),
                                dependency("web", "Web"),
                                dependency("web-services", "Web")));

        assertThat(rows)
                .containsExactly(
                        new DependencyRow.GroupHeader(
                                "Developer Tools"),
                        new DependencyRow.Dependency(
                                0,
                                dependency("lombok", "Developer Tools")),
                        new DependencyRow.GroupHeader("Web"),
                        new DependencyRow.Dependency(
                                1,
                                dependency("web", "Web")),
                        new DependencyRow.Dependency(
                                2,
                                dependency("web-services", "Web")));
    }

    @Test
    void shouldReturnEmptyRowsForEmptyDependencies() {
        assertThat(rowBuilder.build(List.of()))
                .isEmpty();
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

    @Test
    void shouldFindRenderedRowForDependencyIndex() {
        List<DependencyRow> rows =
                rowBuilder.build(
                        List.of(
                                dependency("native", "Developer Tools"),
                                dependency("web", "Web")));

        int rowIndex =
                rowBuilder.findDependencyRowIndex(
                        rows,
                        1);

        assertThat(rowIndex).isEqualTo(3);
    }
}