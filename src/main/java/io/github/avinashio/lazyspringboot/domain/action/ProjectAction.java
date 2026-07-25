package io.github.avinashio.lazyspringboot.domain.action;

public enum ProjectAction {
    BUILD("Build"),
    INSTALL("Install"),
    TEST("Test"),
    RUN("Run"),
    VIEW_LOGS("View Logs"),
    RESTART("Restart"),
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