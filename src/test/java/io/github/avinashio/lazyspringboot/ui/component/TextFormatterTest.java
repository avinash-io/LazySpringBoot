package io.github.avinashio.lazyspringboot.ui.component;

import static org.assertj.core.api.Assertions.assertThat;

import org.jline.utils.AttributedString;
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

    @Test
    void shouldFitAnsiStyledTextUsingVisibleWidth() {
        String styled =
                new TerminalStyle()
                        .dim("Lombok");

        String result =
                textFormatter.fit(styled, 10);

        assertThat(
                AttributedString
                        .fromAnsi(result)
                        .columnLength())
                .isEqualTo(10);
    }

    @Test
    void shouldTruncateAnsiStyledTextUsingVisibleWidth() {
        String styled =
                new TerminalStyle()
                        .dim("Spring Boot DevTools");

        String result =
                textFormatter.fit(styled, 6);

        AttributedString visible =
                AttributedString.fromAnsi(result);

        assertThat(visible.columnLength())
                .isEqualTo(6);

        assertThat(visible.toString())
                .isEqualTo("Spring");
    }
}