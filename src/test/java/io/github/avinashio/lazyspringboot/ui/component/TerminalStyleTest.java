package io.github.avinashio.lazyspringboot.ui.component;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TerminalStyleTest {

    private final TerminalStyle terminalStyle =
            new TerminalStyle();

    @Test
    void shouldApplyDimStyle() {
        String styled =
                terminalStyle.dim("[*] Lombok");

        assertThat(styled)
                .contains("[*] Lombok");

        assertThat(styled)
                .isNotEqualTo("[*] Lombok");
    }
}