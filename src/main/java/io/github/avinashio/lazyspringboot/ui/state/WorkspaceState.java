package io.github.avinashio.lazyspringboot.ui.state;

import org.springframework.stereotype.Component;

@Component
public class WorkspaceState {

    private boolean open;

    private boolean editing;

    private String workspace = "";

    private final StringBuilder inputBuffer =
            new StringBuilder();

    private String errorMessage = "";

    public void open() {

        open = true;

        editing = false;

        errorMessage = "";
    }

    public void close() {

        open = false;

        editing = false;

        errorMessage = "";
    }

    public boolean isOpen() {

        return open;
    }

    public boolean editing() {

        return editing;
    }

    public void startEditing(
            String workspace) {

        editing = true;

        this.workspace = workspace;

        inputBuffer.setLength(0);

        inputBuffer.append(workspace);
    }

    public void stopEditing() {

        editing = false;
    }

    public String workspace() {

        return workspace;
    }

    public void append(
            char character) {

        inputBuffer.append(character);

        workspace = inputBuffer.toString();
    }

    public void backspace() {

        if (inputBuffer.isEmpty()) {
            return;
        }

        inputBuffer.deleteCharAt(
                inputBuffer.length() - 1);

        workspace = inputBuffer.toString();
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