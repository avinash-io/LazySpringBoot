package io.github.avinashio.lazyspringboot.ui.screen;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.ui.component.ModalRenderer;
import io.github.avinashio.lazyspringboot.ui.state.QuitState;
import java.util.List;
import org.junit.jupiter.api.Test;

class QuitConfirmationScreenTest {

    @Test
    void shouldShowRunningProjects() {

        QuitConfirmationScreen screen =
                new QuitConfirmationScreen(
                        null);

        QuitState state =
                new QuitState();

        state.open(
                List.of(
                        "payment-api",
                        "auth-service"));

        List<String> lines =
                screen.buildContent(
                        state);

        assertThat(lines)
                .contains(
                        "▶ Running projects",
                        " > [x] payment-api",
                        "   [x] auth-service",
                        "────────────────────────",
                        "  Actions",
                        " What would you like to do?");
    }

    @Test
    void shouldShowOverflowMessage() {

        QuitConfirmationScreen screen =
                new QuitConfirmationScreen(
                        null);

        QuitState state =
                new QuitState();

        state.open(
                List.of(
                        "one",
                        "two",
                        "three",
                        "four",
                        "five",
                        "six"));

        List<String> lines =
                screen.buildContent(
                        state);

        assertThat(lines)
                .contains(
                        " > [x] one",
                        "   [x] two",
                        "   [x] three",
                        "   [x] four",
                        "   [x] five",
                        "   [x] six");
    }

    @Test
    void shouldShowReadyToQuitMessage() {

        QuitConfirmationScreen screen =
                new QuitConfirmationScreen(
                        null);

        QuitState state =
                new QuitState();

        state.showReadyToQuit();

        List<String> lines =
                screen.buildContent(
                        state);

        assertThat(lines)
                .contains(
                        " ✓ All running projects have",
                        "   been stopped.",
                        " > Quit LazySpringBoot");
    }
}