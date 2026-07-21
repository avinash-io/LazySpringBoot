package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.ui.dependency.DependencyNavigation;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.springframework.stereotype.Component;
import io.github.avinashio.lazyspringboot.ui.controller.TextInputController;
import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;


@Component
public class DependencySearchInputHandler
        implements InputHandler {

    private final UiState uiState;

    private final TextInputController
            textInputController;

    private final DependencyNavigation
            dependencyNavigation;

    public DependencySearchInputHandler(
            UiState uiState, TextInputController textInputController,
            DependencyNavigation dependencyNavigation) {

        this.uiState =
                uiState;
        this.textInputController = textInputController;

        this.dependencyNavigation =
                dependencyNavigation;
    }

    @Override
    public boolean handle(
            KeyEvent keyEvent) {

        if (!textInputController.active(
                TextInputPurpose.DEPENDENCY_SEARCH)) {
            return false;
        }

        switch (keyEvent.type()) {

            case ESCAPE ->
                    textInputController.stop();

            case BACKSPACE -> {

                textInputController.backspace();

                dependencyNavigation
                        .selectFirstVisible();
            }

            case QUIT ->
                    appendCharacter('q');

            case UNDO ->
                    appendCharacter('u');

            case ACTIONS ->
                    appendCharacter('a');

            case GO_TO_TOP ->
                    appendCharacter('g');

            case GO_TO_BOTTOM ->
                    appendCharacter('G');

            case SEARCH ->
                    appendCharacter('/');

            case SPACE ->
                    uiState.toggleSelectedDependency();

            case CHARACTER -> {

                if (!keyEvent.hasCharacter()) {
                    break;
                }

                if (keyEvent.character() == ' ') {

                    uiState.toggleSelectedDependency();

                } else {

                    appendCharacter(
                            keyEvent.character());
                }
            }

            case UP ->
                    dependencyNavigation
                            .selectPreviousVisible();

            case DOWN ->
                    dependencyNavigation
                            .selectNextVisible();

            default -> {
                // No action.
            }
        }

        return true;
    }

    private void appendCharacter(
            char character) {

        textInputController.append(
                character);

        dependencyNavigation
                .selectFirstVisible();
    }
}