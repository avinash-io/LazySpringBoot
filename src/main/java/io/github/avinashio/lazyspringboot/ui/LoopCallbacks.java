package io.github.avinashio.lazyspringboot.ui;

import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;

public interface LoopCallbacks {

    void render();

    void handle(
            KeyEvent keyEvent);

    boolean shouldQuit(
            KeyEvent keyEvent);

    boolean useTimeout();

    long timeoutMillis();
}