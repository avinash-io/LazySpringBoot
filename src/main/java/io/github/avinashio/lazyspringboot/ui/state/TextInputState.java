package io.github.avinashio.lazyspringboot.ui.state;

import org.springframework.stereotype.Component;

@Component
public class TextInputState {

    private boolean active;

    private TextInputPurpose purpose;

    private String value = "";

    public boolean active() {
        return active;
    }

    public TextInputPurpose purpose() {
        return purpose;
    }

    public String value() {
        return value;
    }

    public void start(
            TextInputPurpose purpose) {

        this.purpose = purpose;

        active = true;

        value = "";
    }

    public void stop() {

        active = false;

        purpose = null;

        value = "";
    }

    public void append(
            char character) {

        if (!active) {
            return;
        }

        value += character;
    }

    public void backspace() {

        if (!active || value.isEmpty()) {
            return;
        }

        value =
                value.substring(
                        0,
                        value.length() - 1);
    }

    public boolean isActive(
            TextInputPurpose purpose) {

        return active
                && this.purpose == purpose;
    }
}