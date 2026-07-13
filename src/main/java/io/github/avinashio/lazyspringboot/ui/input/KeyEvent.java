package io.github.avinashio.lazyspringboot.ui.input;

public record KeyEvent(
        KeyType type,
        Character character) {

    public static KeyEvent of(KeyType type) {
        return new KeyEvent(type, null);
    }

    public static KeyEvent character(char character) {
        return new KeyEvent(
                KeyType.CHARACTER,
                character);
    }

    public boolean hasCharacter() {
        return character != null;
    }
}