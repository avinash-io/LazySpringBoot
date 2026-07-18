package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.ui.controller.CreateProjectController;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import java.nio.file.Path;
import org.springframework.stereotype.Component;

@Component
public class CreateProjectInputHandler
        implements InputHandler {

    private final CreateProjectController
            createProjectController;

    public CreateProjectInputHandler(
            CreateProjectController createProjectController) {
        this.createProjectController =
                createProjectController;
    }

    @Override
    public boolean handle(
            KeyEvent keyEvent) {

        CreateProjectState state =
                createProjectController.state();

        if (!state.active()) {
            return false;
        }

        if (state.editing()) {

            switch (keyEvent.type()) {

                case ENTER -> {

                    if (state.selectedField() == 5) {

                        createProjectController.generate(
                                Path.of("")
                                        .toAbsolutePath());

                    } else {

                        state.startEditing();
                    }
                }

                case BACKSPACE ->
                        state.backspace();

                case CHARACTER -> {

                    if (keyEvent.hasCharacter()) {
                        state.append(
                                keyEvent.character());
                    }
                }

                case ESCAPE -> {

                    state.stopEditing();

                    createProjectController.close();
                }

                default -> {
                    // No action.
                }
            }

            return true;
        }

        switch (keyEvent.type()) {

            case ESCAPE ->
                    createProjectController.close();

            case DOWN ->
                    state.nextField();

            case UP ->
                    state.previousField();

            case ENTER ->
                    state.startEditing();

            default -> {
                // No action.
            }
        }

        return true;
    }
}