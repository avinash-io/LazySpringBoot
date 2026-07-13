package io.github.avinashio.lazyspringboot.ui.component;

import org.jline.utils.AttributedString;
import org.springframework.stereotype.Component;

@Component
public class TextFormatter {

    public String fit(String text, int width) {
        if (width <= 0) {
            return "";
        }

        AttributedString attributedString =
                AttributedString.fromAnsi(text);

        int visibleLength =
                attributedString.columnLength();

        if (visibleLength > width) {
            return attributedString
                    .columnSubSequence(0, width)
                    .toAnsi();
        }

        return text
                + " ".repeat(width - visibleLength);
    }
}