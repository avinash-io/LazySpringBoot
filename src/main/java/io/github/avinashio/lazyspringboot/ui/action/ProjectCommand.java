package io.github.avinashio.lazyspringboot.ui.action;

public enum ProjectCommand {

    OPEN_FOLDER(
            "Open Folder",
            'o'),

    COPY_PATH(
            "Copy Path",
            'c'),

    OPEN_INTELLIJ(
            "Open IntelliJ",
            'i'),

    OPEN_VS_CODE(
            "Open VS Code",
            'v');

    private final String displayName;

    private final char shortcut;

    ProjectCommand(
            String displayName,
            char shortcut) {

        this.displayName =
                displayName;

        this.shortcut =
                shortcut;
    }

    public String displayName() {

        return displayName;
    }

    public char shortcut() {

        return shortcut;
    }
}