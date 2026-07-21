package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.ui.controller.WorkspaceController;
import io.github.avinashio.lazyspringboot.ui.state.WorkspaceState;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceInputHandler {

    private final WorkspaceController
            workspaceController;

    private final WorkspaceState
            workspaceState;

    public WorkspaceInputHandler(
            WorkspaceController workspaceController,
            WorkspaceState workspaceState) {

        this.workspaceController =
                workspaceController;

        this.workspaceState =
                workspaceState;
    }

    public boolean handle(
            KeyEvent keyEvent) {

        if (!workspaceController.isOpen()) {
            return false;
        }

        switch (keyEvent.type()) {

            case CHARACTER -> {

                workspaceState.append(
                        keyEvent.character());

                return true;
            }

            case SEARCH -> {

                workspaceState.append('/');

                return true;
            }

            case BACKSPACE -> {

                workspaceState.backspace();

                return true;
            }

            case ESCAPE -> {

                workspaceController.close();

                return true;
            }

            case ENTER -> {
                workspaceController.changeWorkspace(workspaceState.workspace());
                return true;
            }

            default -> {
                return false;
            }
        }
    }
}