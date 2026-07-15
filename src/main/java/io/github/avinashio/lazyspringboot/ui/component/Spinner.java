package io.github.avinashio.lazyspringboot.ui.component;

import org.springframework.stereotype.Component;

@Component
public class Spinner {

    private static final char[] FRAMES = {
            '|',
            '/',
            '-',
            '\\'
    };

    private int index;

    public synchronized char nextFrame() {
        char frame = FRAMES[index];

        index =
                (index + 1)
                        % FRAMES.length;

        return frame;
    }

    public synchronized void reset() {
        index = 0;
    }
}