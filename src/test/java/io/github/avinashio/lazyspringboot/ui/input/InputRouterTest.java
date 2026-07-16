package io.github.avinashio.lazyspringboot.ui.input;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

class InputRouterTest {

    @Test
    void shouldStopAfterFirstHandlerConsumesEvent() {

        AtomicBoolean secondCalled =
                new AtomicBoolean(false);

        InputHandler first =
                event -> true;

        InputHandler second =
                event -> {
                    secondCalled.set(true);
                    return true;
                };

        InputRouter router =
                new InputRouter(
                        List.of(
                                first,
                                second));

        router.handle(
                KeyEvent.of(
                        KeyType.ENTER));

        assertThat(secondCalled.get())
                .isFalse();
    }
}