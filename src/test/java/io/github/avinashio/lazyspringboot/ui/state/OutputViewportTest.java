package io.github.avinashio.lazyspringboot.ui.state;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OutputViewportTest {

    private final OutputViewport viewport =
            new OutputViewport();

    @Test
    void shouldScrollDown() {
        viewport.scrollDown(
                20,
                5);

        assertThat(viewport.offset())
                .isEqualTo(1);
    }

    @Test
    void shouldNotScrollPastMaximumOffset() {
        for (int index = 0;
             index < 20;
             index++) {
            viewport.scrollDown(
                    10,
                    5);
        }

        assertThat(viewport.offset())
                .isEqualTo(5);
    }

    @Test
    void shouldScrollUp() {
        viewport.scrollDown(
                20,
                5);

        viewport.scrollDown(
                20,
                5);

        viewport.scrollUp();

        assertThat(viewport.offset())
                .isEqualTo(1);
    }

    @Test
    void shouldNotScrollAboveZero() {
        viewport.scrollUp();

        assertThat(viewport.offset())
                .isZero();
    }

    @Test
    void shouldScrollPageDown() {
        viewport.pageDown(
                30,
                10);

        assertThat(viewport.offset())
                .isEqualTo(10);
    }

    @Test
    void shouldNotPageDownPastMaximumOffset() {
        viewport.pageDown(
                15,
                10);

        viewport.pageDown(
                15,
                10);

        assertThat(viewport.offset())
                .isEqualTo(5);
    }

    @Test
    void shouldScrollPageUp() {
        viewport.pageDown(
                30,
                10);

        viewport.pageDown(
                30,
                10);

        viewport.pageUp(10);

        assertThat(viewport.offset())
                .isEqualTo(10);
    }

    @Test
    void shouldNotPageUpAboveZero() {
        viewport.pageDown(
                30,
                10);

        viewport.pageUp(20);

        assertThat(viewport.offset())
                .isZero();
    }

    @Test
    void shouldMoveToBottom() {
        viewport.moveToBottom(
                30,
                10);

        assertThat(viewport.offset())
                .isEqualTo(20);
    }

    @Test
    void shouldMoveToTop() {
        viewport.moveToBottom(
                30,
                10);

        viewport.moveToTop();

        assertThat(viewport.offset())
                .isZero();
    }

    @Test
    void shouldKeepZeroWhenContentFitsViewport() {
        viewport.moveToBottom(
                5,
                10);

        assertThat(viewport.offset())
                .isZero();
    }

    @Test
    void shouldIgnorePageDownWithInvalidHeight() {
        viewport.pageDown(
                30,
                0);

        assertThat(viewport.offset())
                .isZero();
    }

    @Test
    void shouldIgnorePageUpWithInvalidHeight() {
        viewport.moveToBottom(
                30,
                10);

        viewport.pageUp(0);

        assertThat(viewport.offset())
                .isEqualTo(20);
    }

    @Test
    void shouldResetOffset() {
        viewport.moveToBottom(
                30,
                10);

        viewport.reset();

        assertThat(viewport.offset())
                .isZero();
    }
}