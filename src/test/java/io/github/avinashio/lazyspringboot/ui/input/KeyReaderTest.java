package io.github.avinashio.lazyspringboot.ui.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;
import org.junit.jupiter.api.Test;

class KeyReaderTest {

    @Test
    void shouldReadQuitKey() throws IOException {
        KeyReader keyReader = createKeyReader('q');

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.QUIT));
    }

    @Test
    void shouldReadEnterKey() throws IOException {
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
    void shouldReadBackspaceKey() throws IOException {
        KeyReader keyReader = createKeyReader(127);

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.BACKSPACE));
    }

    @Test
    void shouldReadCharacterKey() throws IOException {
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
    void shouldReadEscapeKey() throws IOException {
        Terminal terminal = mock(Terminal.class);
        NonBlockingReader reader =
                mock(NonBlockingReader.class);

        when(terminal.reader()).thenReturn(reader);
        when(reader.read()).thenReturn(27);
        when(reader.read(50))
                .thenReturn(NonBlockingReader.READ_EXPIRED);

        KeyReader keyReader =
                new KeyReader(terminal);

        assertThat(keyReader.read())
                .isEqualTo(
                        KeyEvent.of(KeyType.ESCAPE));
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

        when(terminal.reader()).thenReturn(reader);
        when(reader.read()).thenReturn(input);

        return new KeyReader(terminal);
    }
}