package io.github.avinashio.lazyspringboot.domain.command;

public enum CommandPaletteItem {

    BUILD("Build Project"),
    TEST("Test Project"),
    RUN("Run Project"),
    STOP("Stop Project"),
    VIEW_LOGS("View Logs"),
    ADD_DEPENDENCY("Add Dependency"),
    REMOVE_DEPENDENCY("Remove Dependency"),
    CREATE_PROJECT("Create Project"),
    REFRESH_PROJECTS("Refresh Projects");

    private final String title;

    CommandPaletteItem(
            String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}