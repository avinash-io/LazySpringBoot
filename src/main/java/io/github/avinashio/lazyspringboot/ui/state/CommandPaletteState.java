package io.github.avinashio.lazyspringboot.ui.state;

import org.springframework.stereotype.Component;

@Component
public class CommandPaletteState {

    private boolean open;

    private int selectedCommandIndex;


    private String searchQuery = "";

    public boolean open() {
        return open;
    }

    public void openPalette() {

        open = true;

        selectedCommandIndex = 0;

        searchQuery = "";
    }

    public void closePalette() {

        open = false;

        selectedCommandIndex = 0;

        searchQuery = "";
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

    public String searchQuery() {
        return searchQuery;
    }

    public void appendSearchCharacter(
            char character) {

        searchQuery += character;

        selectedCommandIndex = 0;
    }

    public void removeLastSearchCharacter() {

        if (searchQuery.isEmpty()) {
            return;
        }

        searchQuery =
                searchQuery.substring(
                        0,
                        searchQuery.length() - 1);

        selectedCommandIndex = 0;
    }
}