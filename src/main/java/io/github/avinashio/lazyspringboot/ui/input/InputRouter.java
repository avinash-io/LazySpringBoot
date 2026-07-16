package io.github.avinashio.lazyspringboot.ui.input;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class InputRouter {

    private final List<InputHandler>
            handlers;

    public InputRouter(
            List<InputHandler> handlers) {

        this.handlers = handlers;
    }

    public void handle(
            KeyEvent keyEvent) {

        for (InputHandler handler : handlers) {

            if (handler.handle(keyEvent)) {
                return;
            }
        }
    }
}