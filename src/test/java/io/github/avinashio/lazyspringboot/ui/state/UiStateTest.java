package io.github.avinashio.lazyspringboot.ui.state;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UiStateTest {

    @Test
    void shouldSelectNextProject() {
        UiState state = new UiState();

        state.selectNextProject();

        assertThat(state.selectedProjectIndex()).isEqualTo(1);
    }

    @Test
    void shouldSelectPreviousProject() {
        UiState state = new UiState();

        state.selectNextProject();
        state.selectPreviousProject();

        assertThat(state.selectedProjectIndex()).isZero();
    }

    @Test
    void shouldNotMoveBeforeFirstProject() {
        UiState state = new UiState();

        state.selectPreviousProject();

        assertThat(state.selectedProjectIndex()).isZero();
    }

    @Test
    void shouldNotMovePastLastProject() {
        UiState state = new UiState();

        state.selectNextProject();
        state.selectNextProject();
        state.selectNextProject();

        assertThat(state.selectedProjectIndex()).isEqualTo(2);
    }
}