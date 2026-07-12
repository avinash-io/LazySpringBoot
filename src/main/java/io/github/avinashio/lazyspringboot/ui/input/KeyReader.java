package io.github.avinashio.lazyspringboot.ui.input;

import java.io.IOException;
import org.jline.terminal.Terminal;
import org.springframework.stereotype.Component;

@Component
public class KeyReader {

    private static final int ESCAPE = 27;
    private static final int CONTROL_SEQUENCE_INTRODUCER = '[';

    private final Terminal terminal;

    public KeyReader(Terminal terminal) {
        this.terminal = terminal;
    }

    public Key read() throws IOException {
        int input = terminal.reader().read();

        return switch (input) {
            case 'q' -> Key.QUIT;
            case '\r', '\n' -> Key.ENTER;
            case ESCAPE -> readEscapeSequence();
            default -> Key.UNKNOWN;
        };
    }

    private Key readEscapeSequence() throws IOException {
        int sequenceType = terminal.reader().read();

        if (sequenceType != CONTROL_SEQUENCE_INTRODUCER) {
            return Key.UNKNOWN;
        }

        int direction = terminal.reader().read();

        return switch (direction) {
            case 'A' -> Key.UP;
            case 'B' -> Key.DOWN;
            case 'C' -> Key.RIGHT;
            case 'D' -> Key.LEFT;
            default -> Key.UNKNOWN;
        };
    }
}