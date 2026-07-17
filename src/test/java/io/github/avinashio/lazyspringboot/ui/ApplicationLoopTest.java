package io.github.avinashio.lazyspringboot.ui;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ApplicationLoopTest {

    @Test
    void shouldInstantiate() {

        assertThat(
                LoopCallbacks.class)
                .isNotNull();
    }
}