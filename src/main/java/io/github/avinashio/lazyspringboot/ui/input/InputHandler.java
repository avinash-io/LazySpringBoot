package io.github.avinashio.lazyspringboot.ui.input;

public interface InputHandler {

    /**
     * Returns true if this handler processed the event.
     */
    boolean handle(KeyEvent keyEvent);
}