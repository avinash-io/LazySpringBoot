package io.github.avinashio.lazyspringboot.ui.state;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ViewportTest {

    private final Viewport viewport = new Viewport();

    @Test
    void shouldInitiallyStartAtZero() {
        assertThat(viewport.offset()).isZero();
    }

    @Test
    void shouldNotScrollWhenSelectionIsVisible() {
        viewport.update(5, 100, 10);

        assertThat(viewport.offset()).isZero();
    }

    @Test
    void shouldScrollWhenSelectionMovesBelowViewport() {
        viewport.update(10, 100, 10);

        assertThat(viewport.offset()).isEqualTo(1);
    }

    @Test
    void shouldContinueScrollingWithSelection() {
        viewport.update(15, 100, 10);

        assertThat(viewport.offset()).isEqualTo(6);
    }

    @Test
    void shouldScrollBackWhenSelectionMovesAboveViewport() {
        viewport.update(15, 100, 10);
        viewport.update(4, 100, 10);

        assertThat(viewport.offset()).isEqualTo(4);
    }

    @Test
    void shouldNotScrollPastLastValidOffset() {
        viewport.update(99, 100, 10);

        assertThat(viewport.offset()).isEqualTo(90);
    }

    @Test
    void shouldResetForEmptyItems() {
        viewport.update(50, 100, 10);
        viewport.update(0, 0, 10);

        assertThat(viewport.offset()).isZero();
    }

    @Test
    void shouldResetViewport() {
        viewport.update(50, 100, 10);

        viewport.reset();

        assertThat(viewport.offset()).isZero();
    }
}