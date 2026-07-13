package io.github.avinashio.lazyspringboot.ui.input;

import java.io.IOException;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;
import org.springframework.stereotype.Component;

@Component
public class KeyReader {

    private static final int ESCAPE = 27;
    private static final int BACKSPACE = 127;
    private static final int CONTROL_BACKSPACE = 8;
    private static final int CONTROL_SEQUENCE_INTRODUCER = '[';
    private static final long ESCAPE_SEQUENCE_TIMEOUT_MILLIS = 50;

    private final Terminal terminal;

    public KeyReader(Terminal terminal) {
        this.terminal = terminal;
    }

    public KeyEvent read() throws IOException {
        int input = terminal.reader().read();

        return switch (input) {
            case 'q' -> KeyEvent.of(KeyType.QUIT);
            case ' ' -> KeyEvent.of(KeyType.SPACE);
            case '/' -> KeyEvent.of(KeyType.SEARCH);
            case '\r', '\n' -> KeyEvent.of(KeyType.ENTER);
            case BACKSPACE, CONTROL_BACKSPACE ->
                    KeyEvent.of(KeyType.BACKSPACE);
            case ESCAPE -> readEscapeSequence();
            default -> readCharacter(input);
        };
    }

    private KeyEvent readEscapeSequence()
            throws IOException {
        NonBlockingReader reader =
                terminal.reader();

        int sequenceType =
                reader.read(
                        ESCAPE_SEQUENCE_TIMEOUT_MILLIS);

        if (sequenceType
                == NonBlockingReader.READ_EXPIRED) {
            return KeyEvent.of(KeyType.ESCAPE);
        }

        if (sequenceType
                != CONTROL_SEQUENCE_INTRODUCER) {
            return KeyEvent.of(KeyType.UNKNOWN);
        }

        int direction =
                reader.read(
                        ESCAPE_SEQUENCE_TIMEOUT_MILLIS);

        return switch (direction) {
            case 'A' -> KeyEvent.of(KeyType.UP);
            case 'B' -> KeyEvent.of(KeyType.DOWN);
            case 'C' -> KeyEvent.of(KeyType.RIGHT);
            case 'D' -> KeyEvent.of(KeyType.LEFT);
            default -> KeyEvent.of(KeyType.UNKNOWN);
        };
    }

    private KeyEvent readCharacter(int input) {
        if (isPrintable(input)) {
            return KeyEvent.character((char) input);
        }

        return KeyEvent.of(KeyType.UNKNOWN);
    }

    private boolean isPrintable(int input) {
        return input >= 32 && input <= 126;
    }
}