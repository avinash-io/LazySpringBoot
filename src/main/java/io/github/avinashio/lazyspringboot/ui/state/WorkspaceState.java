package io.github.avinashio.lazyspringboot.ui.state;

import org.springframework.stereotype.Component;

@Component
public class WorkspaceState {

    private boolean open;

    private String workspace = "";

    private String errorMessage = "";

    public void open() {

        open = true;

        errorMessage = "";
    }

    public void close() {

        open = false;

        errorMessage = "";
    }

    public boolean isOpen() {

        return open;
    }

    public String workspace() {

        return workspace;
    }

    public void setWorkspace(
            String workspace) {

        this.workspace = workspace;
    }

    public String errorMessage() {

        return errorMessage;
    }

    public boolean hasErrorMessage() {

        return !errorMessage.isBlank();
    }

    public void showErrorMessage(
            String message) {

        errorMessage = message;
    }

    public void clearErrorMessage() {

        errorMessage = "";
    }
}