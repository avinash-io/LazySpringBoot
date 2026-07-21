package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.ui.controller.TextInputController;
import io.github.avinashio.lazyspringboot.ui.project.ProjectNavigation;
import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;
import org.springframework.stereotype.Component;

@Component
public class ProjectSearchInputHandler {

    private final TextInputController
            textInputController;

    private final ProjectNavigation
            projectNavigation;

    public ProjectSearchInputHandler(
            TextInputController textInputController,
            ProjectNavigation projectNavigation) {

        this.textInputController =
                textInputController;

        this.projectNavigation =
                projectNavigation;
    }

    public boolean handle(
            KeyEvent keyEvent) {

        if (!textInputController.active(
                TextInputPurpose.PROJECT_SEARCH)) {

            return false;
        }

        switch (keyEvent.type()) {

            case UP ->
                    projectNavigation
                            .selectPreviousVisible();

            case DOWN ->
                    projectNavigation
                            .selectNextVisible();

            case ENTER ->
                    textInputController.stop();

            case ESCAPE ->
                    textInputController.stop();

            case BACKSPACE -> {
                textInputController.backspace();

                projectNavigation
                        .selectFirstVisible();
            }

            case CHARACTER -> {
                appendCharacter(
                        keyEvent);

                projectNavigation
                        .selectFirstVisible();
            }

            default -> {
                // Ignore unsupported keys while searching.
            }
        }

        return true;
    }

    private void appendCharacter(
            KeyEvent keyEvent) {

        Character character =
                keyEvent.character();

        if (character == null) {
            return;
        }

        textInputController.append(
                character);
    }
}