package io.github.avinashio.lazyspringboot.ui.component;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.stereotype.Component;

@Component
public class TerminalStyle {

    public String running(
            String value) {

        return style(
                value,
                AttributedStyle.GREEN);
    }

    public String starting(
            String value) {

        return style(
                value,
                AttributedStyle.YELLOW);
    }

    public String failed(
            String value) {

        return style(
                value,
                AttributedStyle.RED);
    }

    public String stopped(
            String value) {

        return dim(
                value);
    }

    public String dim(
            String value) {

        return new AttributedString(
                value,
                AttributedStyle.DEFAULT.faint())
                .toAnsi();
    }

    public String highlight(
            String value) {

        return new AttributedString(
                value,
                AttributedStyle.DEFAULT.inverse())
                .toAnsi();
    }

    private String style(
            String value,
            int foregroundColor) {

        return new AttributedString(
                value,
                AttributedStyle.DEFAULT
                        .foreground(
                                foregroundColor))
                .toAnsi();
    }
}