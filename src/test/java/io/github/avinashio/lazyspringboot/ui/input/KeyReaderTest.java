package io.github.avinashio.lazyspringboot.ui.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;
import org.junit.jupiter.api.Test;

class KeyReaderTest {

    private static final long
            ESCAPE_SEQUENCE_TIMEOUT_MILLIS = 50;

    private static final long
            POLL_TIMEOUT_MILLIS = 150;

    @Test
    void shouldReadQuitLetterAsCharacter()
            throws IOException {

        KeyReader keyReader =
                createKeyReader('q');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.character('q'));
    }

    @Test
    void shouldReadUndoLetterAsCharacter()
            throws IOException {

        KeyReader keyReader =
                createKeyReader('u');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.character('u'));
    }

    @Test
    void shouldReadActionsLetterAsCharacter()
            throws IOException {

        KeyReader keyReader =
                createKeyReader('a');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.character('a'));
    }

    @Test
    void shouldReadLowercaseGLetterAsCharacter()
            throws IOException {

        KeyReader keyReader =
                createKeyReader('g');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.character('g'));
    }

    @Test
    void shouldReadUppercaseGLetterAsCharacter()
            throws IOException {

        KeyReader keyReader =
                createKeyReader('G');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.character('G'));
    }

    @Test
    void shouldReadCarriageReturnAsEnter()
            throws IOException {

        KeyReader keyReader =
                createKeyReader('\r');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(
                                KeyType.ENTER));
    }

    @Test
    void shouldReadNewlineAsEnter()
            throws IOException {

        KeyReader keyReader =
                createKeyReader('\n');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(
                                KeyType.ENTER));
    }

    @Test
    void shouldReadSpaceKey()
            throws IOException {

        KeyReader keyReader =
                createKeyReader(' ');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(
                                KeyType.SPACE));
    }

    @Test
    void shouldReadSearchKey()
            throws IOException {

        KeyReader keyReader =
                createKeyReader('/');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(
                                KeyType.SEARCH));
    }

    @Test
    void shouldReadBackspaceKey()
            throws IOException {

        KeyReader keyReader =
                createKeyReader(127);

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(
                                KeyType.BACKSPACE));
    }

    @Test
    void shouldReadControlHAsBackspace()
            throws IOException {

        KeyReader keyReader =
                createKeyReader(8);

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(
                                KeyType.BACKSPACE));
    }

    @Test
    void shouldReadCharacterKey()
            throws IOException {

        KeyReader keyReader =
                createKeyReader('w');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.character('w'));
    }

    @Test
    void shouldReadUppercaseCharacterKey()
            throws IOException {

        KeyReader keyReader =
                createKeyReader('W');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.character('W'));
    }

    @Test
    void shouldReadNumericCharacterKey()
            throws IOException {

        KeyReader keyReader =
                createKeyReader('4');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.character('4'));
    }

    @Test
    void shouldReturnTimeoutWhenTimedReadExpires()
            throws IOException {

        Terminal terminal =
                mock(Terminal.class);

        NonBlockingReader reader =
                mock(NonBlockingReader.class);

        when(terminal.reader())
                .thenReturn(reader);

        when(reader.read(
                POLL_TIMEOUT_MILLIS))
                .thenReturn(
                        NonBlockingReader.READ_EXPIRED);

        KeyReader keyReader =
                new KeyReader(terminal);

        assertThat(
                keyReader.read(
                        POLL_TIMEOUT_MILLIS))
                .isEqualTo(
                        KeyEvent.of(
                                KeyType.TIMEOUT));
    }

    @Test
    void shouldReadCharacterDuringTimedRead()
            throws IOException {

        Terminal terminal =
                mock(Terminal.class);

        NonBlockingReader reader =
                mock(NonBlockingReader.class);

        when(terminal.reader())
                .thenReturn(reader);

        when(reader.read(
                POLL_TIMEOUT_MILLIS))
                .thenReturn(
                        (int) 'q');

        KeyReader keyReader =
                new KeyReader(terminal);

        assertThat(
                keyReader.read(
                        POLL_TIMEOUT_MILLIS))
                .isEqualTo(
                        KeyEvent.character('q'));
    }

    @Test
    void shouldReadEscapeKey()
            throws IOException {

        Terminal terminal =
                mock(Terminal.class);

        NonBlockingReader reader =
                mock(NonBlockingReader.class);

        when(terminal.reader())
                .thenReturn(reader);

        when(reader.read())
                .thenReturn(27);

        when(reader.read(
                ESCAPE_SEQUENCE_TIMEOUT_MILLIS))
                .thenReturn(
                        NonBlockingReader.READ_EXPIRED);

        KeyReader keyReader =
                new KeyReader(terminal);

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(
                                KeyType.ESCAPE));
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
                        KeyEvent.of(
                                KeyType.UP));
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
                        KeyEvent.of(
                                KeyType.DOWN));
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
                        KeyEvent.of(
                                KeyType.RIGHT));
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
                        KeyEvent.of(
                                KeyType.LEFT));
    }

    @Test
    void shouldReadPageUpKey()
            throws IOException {

        KeyReader keyReader =
                createPageSequenceKeyReader(
                        '5');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(
                                KeyType.PAGE_UP));
    }

    @Test
    void shouldReadPageDownKey()
            throws IOException {

        KeyReader keyReader =
                createPageSequenceKeyReader(
                        '6');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(
                                KeyType.PAGE_DOWN));
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
                        KeyEvent.of(
                                KeyType.UNKNOWN));
    }

    @Test
    void shouldReturnUnknownForUnsupportedKey()
            throws IOException {

        KeyReader keyReader =
                createKeyReader(1);

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(
                                KeyType.UNKNOWN));
    }

    private KeyReader createKeyReader(
            int input)
            throws IOException {

        Terminal terminal =
                mock(Terminal.class);

        NonBlockingReader reader =
                mock(NonBlockingReader.class);

        when(terminal.reader())
                .thenReturn(reader);

        when(reader.read())
                .thenReturn(input);

        return new KeyReader(terminal);
    }

    private KeyReader
    createEscapeSequenceKeyReader(
            int secondCharacter,
            int thirdCharacter)
            throws IOException {

        Terminal terminal =
                mock(Terminal.class);

        NonBlockingReader reader =
                mock(NonBlockingReader.class);

        when(terminal.reader())
                .thenReturn(reader);

        when(reader.read())
                .thenReturn(27);

        when(reader.read(
                ESCAPE_SEQUENCE_TIMEOUT_MILLIS))
                .thenReturn(
                        secondCharacter,
                        thirdCharacter);

        return new KeyReader(terminal);
    }

    private KeyReader
    createPageSequenceKeyReader(
            int sequenceValue)
            throws IOException {

        Terminal terminal =
                mock(Terminal.class);

        NonBlockingReader reader =
                mock(NonBlockingReader.class);

        when(terminal.reader())
                .thenReturn(reader);

        when(reader.read())
                .thenReturn(27);

        when(reader.read(
                ESCAPE_SEQUENCE_TIMEOUT_MILLIS))
                .thenReturn(
                        (int) '[',
                        sequenceValue,
                        (int) '~');

        return new KeyReader(terminal);
    }
}