package io.github.avinashio.lazyspringboot.ui.command;

import org.springframework.stereotype.Component;

@Component
public class CommandPaletteState {

    private boolean active;

    private int selectedIndex;

    public boolean active() {
        return active;
    }

    public void open() {
        active = true;
        selectedIndex = 0;
    }

    public void close() {
        active = false;
    }

    public int selectedIndex() {
        return selectedIndex;
    }

    public void next(
            int size) {

        if (selectedIndex < size - 1) {
            selectedIndex++;
        }
    }

    public void previous() {

        if (selectedIndex > 0) {
            selectedIndex--;
        }
    }
}