package io.github.avinashio.lazyspringboot.ui.input;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class KeyEventTest {

    @Test
    void shouldCreateKeyEventWithoutCharacter() {
        KeyEvent event =
                KeyEvent.of(KeyType.UP);

        assertThat(event.type())
                .isEqualTo(KeyType.UP);

        assertThat(event.hasCharacter())
                .isFalse();

        assertThat(event.character())
                .isNull();
    }

    @Test
    void shouldCreateCharacterKeyEvent() {
        KeyEvent event =
                KeyEvent.character('w');

        assertThat(event.type())
                .isEqualTo(KeyType.CHARACTER);

        assertThat(event.hasCharacter())
                .isTrue();

        assertThat(event.character())
                .isEqualTo('w');
    }
}