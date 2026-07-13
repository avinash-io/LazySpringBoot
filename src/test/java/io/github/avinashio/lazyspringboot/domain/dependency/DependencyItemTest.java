package io.github.avinashio.lazyspringboot.domain.dependency;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DependencyItemTest {

    @Test
    void shouldAllowAvailableDependencySelection() {
        DependencyItem item =
                new DependencyItem(
                        dependency(),
                        DependencyAvailability.AVAILABLE,
                        false);

        assertThat(item.selectable()).isTrue();
    }

    @Test
    void shouldRejectAlreadyPresentDependencySelection() {
        DependencyItem item =
                new DependencyItem(
                        dependency(),
                        DependencyAvailability.ALREADY_PRESENT,
                        false);

        assertThat(item.selectable()).isFalse();
    }

    private SpringDependency dependency() {
        return new SpringDependency(
                "web",
                "Spring Web",
                "Web applications",
                "Web");
    }
}