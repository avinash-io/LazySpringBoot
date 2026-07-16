package io.github.avinashio.lazyspringboot.ui.state;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CreateProjectStateTest {

    @Test
    void shouldOpenWizard() {

        CreateProjectState state =
                new CreateProjectState();

        state.open();

        assertThat(state.active())
                .isTrue();

        assertThat(state.selectedField())
                .isZero();
    }

    @Test
    void shouldNavigateFields() {

        CreateProjectState state =
                new CreateProjectState();

        state.open();

        state.nextField();
        state.nextField();

        assertThat(state.selectedField())
                .isEqualTo(2);

        state.previousField();

        assertThat(state.selectedField())
                .isEqualTo(1);
    }
}