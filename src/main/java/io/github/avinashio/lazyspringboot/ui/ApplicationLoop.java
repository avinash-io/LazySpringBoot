package io.github.avinashio.lazyspringboot.ui;

import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;
import io.github.avinashio.lazyspringboot.ui.input.KeyReader;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class ApplicationLoop {

    private final KeyReader keyReader;

    public ApplicationLoop(
            KeyReader keyReader) {
        this.keyReader = keyReader;
    }

    public void run(
            LoopCallbacks callbacks)
            throws Exception {

        while (true) {

            KeyEvent event =
                    read(callbacks);

            if (callbacks.shouldQuit(event)) {
                return;
            }

            callbacks.handle(event);

            callbacks.render();
        }
    }

    private KeyEvent read(
            LoopCallbacks callbacks)
            throws IOException {

        if (callbacks.useTimeout()) {
            return keyReader.read(
                    callbacks.timeoutMillis());
        }

        return keyReader.read();
    }
}