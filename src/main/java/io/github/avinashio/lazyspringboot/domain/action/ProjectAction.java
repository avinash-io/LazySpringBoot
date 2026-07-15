package io.github.avinashio.lazyspringboot.domain.action;

public enum ProjectAction {
    BUILD("Build"),
    TEST("Test"),
    RUN("Run"),
    VIEW_LOGS("View Logs"),
    STOP("Stop");

    private final String displayName;

    ProjectAction(
            String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}