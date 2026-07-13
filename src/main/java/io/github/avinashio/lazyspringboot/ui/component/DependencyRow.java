package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;

public sealed interface DependencyRow
        permits DependencyRow.GroupHeader,
        DependencyRow.Dependency {

    record GroupHeader(String name)
            implements DependencyRow {}

    record Dependency(
            int dependencyIndex,
            DependencyItem item)
            implements DependencyRow {}
}