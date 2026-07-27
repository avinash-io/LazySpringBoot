package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.ui.controller.WorkspaceController;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceInputHandler {

    private final WorkspaceController
            workspaceController;

    public WorkspaceInputHandler(
            WorkspaceController workspaceController) {

        this.workspaceController =
                workspaceController;
    }

    public boolean handle(
            KeyEvent keyEvent) {

        if (!workspaceController.isOpen()) {
            return false;
        }

        switch (keyEvent.type()) {

            case CHARACTER -> {

                if (!keyEvent.hasCharacter()) {
                    return false;
                }

                switch (Character.toLowerCase(
                        keyEvent.character())) {

                    case 'c' -> {

                        workspaceController
                                .copyWorkspacePath();

                        return true;
                    }

                    case 'o' -> {

                        workspaceController
                                .openWorkspace();

                        return true;
                    }

                    default -> {
                        return false;
                    }
                }
            }

            case ESCAPE -> {

                workspaceController.close();

                return true;
            }

            default -> {
                return false;
            }
        }
    }
}