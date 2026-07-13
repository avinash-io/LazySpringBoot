package io.github.avinashio.lazyspringboot.ui.component;

import org.springframework.stereotype.Component;

@Component
public class TextFormatter {

    public String fit(String value, int width) {
        if (width <= 0) {
            return "";
        }

        if (value.length() >= width) {
            return value.substring(0, width);
        }

        return value + " ".repeat(width - value.length());
    }
}