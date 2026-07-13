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
                .isEqualTo(Key.QUIT);
    }

    @Test
    void shouldReadEnterKey() throws IOException {
        KeyReader keyReader = createKeyReader('\n');

        assertThat(keyReader.read())
                .isEqualTo(Key.ENTER);
    }

    @Test
    void shouldReadSpaceKey() throws IOException {
        KeyReader keyReader = createKeyReader(' ');

        assertThat(keyReader.read())
                .isEqualTo(Key.SPACE);
    }

    @Test
    void shouldReturnUnknownForUnsupportedKey()
            throws IOException {
        KeyReader keyReader = createKeyReader('z');

        assertThat(keyReader.read())
                .isEqualTo(Key.UNKNOWN);
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