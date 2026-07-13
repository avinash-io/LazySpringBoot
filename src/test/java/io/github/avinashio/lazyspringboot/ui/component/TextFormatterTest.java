package io.github.avinashio.lazyspringboot.ui.component;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TextFormatterTest {

    private final TextFormatter textFormatter =
            new TextFormatter();

    @Test
    void shouldPadTextToRequestedWidth() {
        String result = textFormatter.fit("Java", 8);

        assertThat(result).isEqualTo("Java    ");
    }

    @Test
    void shouldTruncateTextExceedingWidth() {
        String result =
                textFormatter.fit("LazySpringBoot", 4);

        assertThat(result).isEqualTo("Lazy");
    }

    @Test
    void shouldReturnExactTextWhenWidthMatches() {
        String result = textFormatter.fit("Java", 4);

        assertThat(result).isEqualTo("Java");
    }

    @Test
    void shouldReturnEmptyStringForZeroWidth() {
        String result = textFormatter.fit("Java", 0);

        assertThat(result).isEmpty();
    }
}