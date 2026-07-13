package io.github.avinashio.lazyspringboot.ui.component;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.stereotype.Component;

@Component
public class TerminalStyle {

    public String dim(String value) {
        return new AttributedString(
                value,
                AttributedStyle.DEFAULT.faint())
                .toAnsi();
    }
}