package io.github.avinashio.lazyspringboot.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DependencyControllerTest {

    @Test
    void shouldCreateController() {

        DependencyController controller =
                new DependencyController();

        assertThat(controller)
                .isNotNull();
    }
}