package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.domain.action.ProjectActionOutput;
import io.github.avinashio.lazyspringboot.ui.screen.ProjectActionOutputScreen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.springframework.stereotype.Component;

@Component
public class ProjectActionOutputInputHandler
        implements InputHandler {

    private final UiState uiState;

    private final ProjectActionOutputScreen
            projectActionOutputScreen;

    public ProjectActionOutputInputHandler(
            UiState uiState,
            ProjectActionOutputScreen projectActionOutputScreen) {

        this.uiState = uiState;
        this.projectActionOutputScreen =
                projectActionOutputScreen;
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

        int visibleHeight =
                projectActionOutputScreen
                        .visibleHeight();

        switch (keyEvent.type()) {

            case ESCAPE ->
                    uiState.closeProjectActionOutput();

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

            default -> {
                // No action.
            }
        }

        return true;
    }
}