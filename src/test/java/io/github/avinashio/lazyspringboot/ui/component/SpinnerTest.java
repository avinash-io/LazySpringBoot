package io.github.avinashio.lazyspringboot.ui.component;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SpinnerTest {

    @Test
    void shouldRotateFrames() {

        Spinner spinner =
                new Spinner();

        assertThat(spinner.nextFrame())
                .isEqualTo('|');

        assertThat(spinner.nextFrame())
                .isEqualTo('/');

        assertThat(spinner.nextFrame())
                .isEqualTo('-');

        assertThat(spinner.nextFrame())
                .isEqualTo('\\');

        assertThat(spinner.nextFrame())
                .isEqualTo('|');
    }

    @Test
    void shouldReset() {

        Spinner spinner =
                new Spinner();

        spinner.nextFrame();
        spinner.nextFrame();

        spinner.reset();

        assertThat(spinner.nextFrame())
                .isEqualTo('|');
    }
}