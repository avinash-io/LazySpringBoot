package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectActionOutput;
import io.github.avinashio.lazyspringboot.ui.controller.LogSearchController;
import io.github.avinashio.lazyspringboot.ui.screen.ProjectActionOutputScreen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.springframework.stereotype.Component;

@Component
public class ProjectActionOutputInputHandler
        implements InputHandler {

    private final UiState uiState;

    private final ProjectActionOutputScreen
            projectActionOutputScreen;

    private final LogSearchController
            logSearchController;

    public ProjectActionOutputInputHandler(
            UiState uiState,
            ProjectActionOutputScreen projectActionOutputScreen,
            LogSearchController logSearchController) {

        this.uiState =
                uiState;

        this.projectActionOutputScreen =
                projectActionOutputScreen;

        this.logSearchController =
                logSearchController;
    }

    @Override
    public boolean handle(
            KeyEvent keyEvent) {

        if (!uiState.projectActionOutputActive()) {
            return false;
        }

        ProjectActionOutput output =
                uiState.projectActionOutput();

        if (output == null) {
            return true;
        }

        if (logSearchController.active()) {

            handleSearchInput(
                    keyEvent);

            return true;
        }

        int visibleHeight =
                projectActionOutputScreen
                        .visibleHeight();

        switch (keyEvent.type()) {

            case ESCAPE ->
                    uiState.closeProjectActionOutput();

            case SEARCH ->
                    startSearch(
                            output);

            case UP ->
                    uiState.outputViewport()
                            .scrollUp();

            case DOWN ->
                    uiState.outputViewport()
                            .scrollDown(
                                    output.lines().size(),
                                    visibleHeight);

            case PAGE_UP ->
                    uiState.outputViewport()
                            .pageUp(
                                    visibleHeight);

            case PAGE_DOWN ->
                    uiState.outputViewport()
                            .pageDown(
                                    output.lines().size(),
                                    visibleHeight);

            case GO_TO_TOP ->
                    uiState.outputViewport()
                            .moveToTop();

            case GO_TO_BOTTOM ->
                    uiState.outputViewport()
                            .moveToBottom(
                                    output.lines().size(),
                                    visibleHeight);

            case CHARACTER ->
                    handleCharacter(
                            keyEvent,
                            output,
                            visibleHeight);

            default -> {
                // No action.
            }
        }

        return true;
    }

    private void startSearch(
            ProjectActionOutput output) {

        if (output.action()
                != ProjectAction.VIEW_LOGS) {

            return;
        }

        logSearchController.start();
    }

    private void handleSearchInput(
            KeyEvent keyEvent) {

        switch (keyEvent.type()) {

            case ESCAPE ->
                    logSearchController.stopInput();

            case ENTER ->
                    logSearchController.apply();

            case BACKSPACE ->
                    logSearchController.backspace();

            case CHARACTER -> {

                if (keyEvent.hasCharacter()) {

                    logSearchController.append(
                            keyEvent.character());
                }
            }

            default -> {
                // No action.
            }
        }
    }

    private void handleCharacter(
            KeyEvent keyEvent,
            ProjectActionOutput output,
            int visibleHeight) {

        if (!keyEvent.hasCharacter()) {
            return;
        }

        if (keyEvent.character() == 'g') {

            uiState.outputViewport()
                    .moveToTop();

            return;
        }

        if (keyEvent.character() == 'G') {

            uiState.outputViewport()
                    .moveToBottom(
                            output.lines().size(),
                            visibleHeight);

            return;
        }

        if (output.action()
                != ProjectAction.VIEW_LOGS) {

            return;
        }

        if (keyEvent.character() == 'n') {

            logSearchController.next();

            return;
        }

        if (keyEvent.character() == 'N') {

            logSearchController.previous();
        }
    }
}