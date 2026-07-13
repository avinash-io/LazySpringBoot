package io.github.avinashio.lazyspringboot.domain.dependency;

public record DependencyItem(
        SpringDependency dependency,
        DependencyAvailability availability,
        boolean selected) {

    public boolean selectable() {
        return availability == DependencyAvailability.AVAILABLE;
    }
}