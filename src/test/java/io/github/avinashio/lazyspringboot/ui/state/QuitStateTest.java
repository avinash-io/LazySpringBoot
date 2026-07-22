package io.github.avinashio.lazyspringboot.ui.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class QuitStateTest {

    @Test
    void shouldOpenWithRunningProjects() {

        QuitState state =
                new QuitState();

        state.open(
                List.of(
                        "payment-api",
                        "auth-service"));

        assertThat(
                state.active())
                .isTrue();

        assertThat(
                state.runningProjects())
                .containsExactly(
                        "payment-api",
                        "auth-service");

        assertThat(
                state.runningProjectCount())
                .isEqualTo(
                        2);

        assertThat(
                state.runningProjectsPhase())
                .isTrue();

        assertThat(
                state.selectedOption())
                .isEqualTo(
                        QuitOption.STOP_RUNNING);
    }

    @Test
    void shouldTransitionToReadyToQuit() {

        QuitState state =
                new QuitState();

        state.open(
                List.of(
                        "demo"));

        state.showReadyToQuit();

        assertThat(
                state.readyToQuitPhase())
                .isTrue();

        assertThat(
                state.options())
                .containsExactly(
                        QuitOption.QUIT,
                        QuitOption.CANCEL);

        assertThat(
                state.selectedOption())
                .isEqualTo(
                        QuitOption.QUIT);
    }

    @Test
    void shouldCloseAndResetState() {

        QuitState state =
                new QuitState();

        state.open(
                List.of(
                        "demo"));

        state.close();

        assertThat(
                state.active())
                .isFalse();

        assertThat(
                state.runningProjects())
                .isEmpty();

        assertThat(
                state.selectedOptionIndex())
                .isZero();
    }

    @Test
    void shouldNavigateOptions() {

        QuitState state =
                new QuitState();

        state.open(
                List.of(
                        "demo"));

        state.moveNext();

        assertThat(
                state.selectedOption())
                .isEqualTo(
                        QuitOption.QUIT_ANYWAY);

        state.movePrevious();

        assertThat(
                state.selectedOption())
                .isEqualTo(
                        QuitOption.STOP_RUNNING);
    }
}