package io.github.avinashio.lazyspringboot.ui.component;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class DurationFormatterTest {

    private final DurationFormatter formatter =
            new DurationFormatter();

    @Test
    void shouldFormatSeconds() {

        assertThat(
                formatter.format(
                        Duration.ofSeconds(5)))
                .isEqualTo("00:00:05");
    }

    @Test
    void shouldFormatMinutes() {

        assertThat(
                formatter.format(
                        Duration.ofMinutes(5)))
                .isEqualTo("00:05:00");
    }

    @Test
    void shouldFormatHours() {

        assertThat(
                formatter.format(
                        Duration.ofHours(2)
                                .plusMinutes(15)
                                .plusSeconds(8)))
                .isEqualTo("02:15:08");
    }
}