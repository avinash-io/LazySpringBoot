package io.github.avinashio.lazyspringboot.ui.state;

import org.springframework.stereotype.Component;

@Component
public class CommandPaletteState {

    private boolean open;

    private int selectedCommandIndex;

    public boolean open() {
        return open;
    }

    public void openPalette() {
        open = true;
        selectedCommandIndex = 0;
    }

    public void closePalette() {
        open = false;
    }

    public int selectedCommandIndex() {
        return selectedCommandIndex;
    }

    public void selectNext(
            int commandCount) {

        if (commandCount <= 0) {
            return;
        }

        if (selectedCommandIndex
                < commandCount - 1) {
            selectedCommandIndex++;
        }
    }

    public void selectPrevious() {

        if (selectedCommandIndex > 0) {
            selectedCommandIndex--;
        }
    }
}