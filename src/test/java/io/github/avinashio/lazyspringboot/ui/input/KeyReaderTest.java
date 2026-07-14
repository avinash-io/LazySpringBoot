package io.github.avinashio.lazyspringboot.ui.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;
import org.junit.jupiter.api.Test;

class KeyReaderTest {

    private static final long ESCAPE_SEQUENCE_TIMEOUT_MILLIS =
            50;

    @Test
    void shouldReadQuitKey() throws IOException {
        KeyReader keyReader = createKeyReader('q');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.QUIT));
    }

    @Test
    void shouldReadCarriageReturnAsEnter()
            throws IOException {
        KeyReader keyReader = createKeyReader('\r');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.ENTER));
    }

    @Test
    void shouldReadNewlineAsEnter()
            throws IOException {
        KeyReader keyReader = createKeyReader('\n');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.ENTER));
    }

    @Test
    void shouldReadSpaceKey() throws IOException {
        KeyReader keyReader = createKeyReader(' ');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.SPACE));
    }

    @Test
    void shouldReadSearchKey() throws IOException {
        KeyReader keyReader = createKeyReader('/');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.SEARCH));
    }

    @Test
    void shouldReadBackspaceKey()
            throws IOException {
        KeyReader keyReader = createKeyReader(127);

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.BACKSPACE));
    }

    @Test
    void shouldReadControlHAsBackspace()
            throws IOException {
        KeyReader keyReader = createKeyReader(8);

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.BACKSPACE));
    }

    @Test
    void shouldReadCharacterKey()
            throws IOException {
        KeyReader keyReader = createKeyReader('w');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.character('w'));
    }

    @Test
    void shouldReadUppercaseCharacterKey()
            throws IOException {
        KeyReader keyReader = createKeyReader('W');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.character('W'));
    }

    @Test
    void shouldReadNumericCharacterKey()
            throws IOException {
        KeyReader keyReader = createKeyReader('4');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.character('4'));
    }

    @Test
    void shouldReadEscapeKey()
            throws IOException {
        Terminal terminal = mock(Terminal.class);
        NonBlockingReader reader =
                mock(NonBlockingReader.class);

        when(terminal.reader())
                .thenReturn(reader);

        when(reader.read())
                .thenReturn(27);

        when(
                reader.read(
                        ESCAPE_SEQUENCE_TIMEOUT_MILLIS))
                .thenReturn(
                        NonBlockingReader.READ_EXPIRED);

        KeyReader keyReader =
                new KeyReader(terminal);

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.ESCAPE));
    }

    @Test
    void shouldReadUpArrowKey()
            throws IOException {
        KeyReader keyReader =
                createEscapeSequenceKeyReader(
                        '[',
                        'A');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.UP));
    }

    @Test
    void shouldReadDownArrowKey()
            throws IOException {
        KeyReader keyReader =
                createEscapeSequenceKeyReader(
                        '[',
                        'B');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.DOWN));
    }

    @Test
    void shouldReadRightArrowKey()
            throws IOException {
        KeyReader keyReader =
                createEscapeSequenceKeyReader(
                        '[',
                        'C');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.RIGHT));
    }

    @Test
    void shouldReadLeftArrowKey()
            throws IOException {
        KeyReader keyReader =
                createEscapeSequenceKeyReader(
                        '[',
                        'D');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.LEFT));
    }

    @Test
    void shouldReturnUnknownForUnsupportedEscapeSequence()
            throws IOException {
        KeyReader keyReader =
                createEscapeSequenceKeyReader(
                        '[',
                        'Z');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.UNKNOWN));
    }

    @Test
    void shouldReturnUnknownForUnsupportedKey()
            throws IOException {
        KeyReader keyReader = createKeyReader(1);

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.UNKNOWN));
    }

    private KeyReader createKeyReader(int input)
            throws IOException {
        Terminal terminal = mock(Terminal.class);

        NonBlockingReader reader =
                mock(NonBlockingReader.class);

        when(terminal.reader())
                .thenReturn(reader);

        when(reader.read())
                .thenReturn(input);

        return new KeyReader(terminal);
    }

    private KeyReader createEscapeSequenceKeyReader(
            int secondCharacter,
            int thirdCharacter)
            throws IOException {
        Terminal terminal = mock(Terminal.class);

        NonBlockingReader reader =
                mock(NonBlockingReader.class);

        when(terminal.reader())
                .thenReturn(reader);

        when(reader.read())
                .thenReturn(27);

        when(
                reader.read(
                        ESCAPE_SEQUENCE_TIMEOUT_MILLIS))
                .thenReturn(
                        secondCharacter,
                        thirdCharacter);

        return new KeyReader(terminal);
    }

    @Test
    void shouldReadUndoKey() throws IOException {
        KeyReader keyReader = createKeyReader('u');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.UNDO));
    }

}