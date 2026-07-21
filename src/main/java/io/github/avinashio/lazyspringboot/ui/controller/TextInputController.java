package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;
import io.github.avinashio.lazyspringboot.ui.state.TextInputState;
import org.springframework.stereotype.Controller;

@Controller
public class TextInputController {

    private final TextInputState textInputState;

    public TextInputController(
            TextInputState textInputState) {

        this.textInputState =
                textInputState;
    }

    public void start(
            TextInputPurpose purpose) {

        textInputState.start(
                purpose);
    }

    public void stop() {

        textInputState.stop();
    }

    public void append(
            char character) {

        textInputState.append(
                character);
    }

    public void backspace() {

        textInputState.backspace();
    }

    public boolean active(
            TextInputPurpose purpose) {

        return textInputState.isActive(
                purpose);
    }

    public String value() {

        return textInputState.value();
    }
}