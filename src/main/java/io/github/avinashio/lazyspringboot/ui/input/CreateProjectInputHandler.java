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

        if (state.metadataStage()) {

            handleMetadataStage(
                    state,
                    keyEvent);

            return true;
        }

        handleDependencyStage(
                state,
                keyEvent);

        return true;
    }

    private void handleMetadataStage(
            CreateProjectState state,
            KeyEvent keyEvent) {

        if (state.versionSelecting()) {

            handleVersionSelection(
                    state,
                    keyEvent);

            return;
        }

        if (state.editing()) {

            handleMetadataEditing(
                    state,
                    keyEvent);

            return;
        }

        switch (keyEvent.type()) {

            case ESCAPE ->
                    createProjectController.close();

            case DOWN ->
                    state.nextField();

            case UP ->
                    state.previousField();

            case ENTER -> {

                if (state.selectedField() >= 4) {

                    state.startVersionSelection();

                } else {

                    state.startEditing();
                }
            }

            case TAB ->
                    createProjectController
                            .continueToDependencies();

            default -> {
                // No action.
            }
        }
    }

    private void handleVersionSelection(
            CreateProjectState state,
            KeyEvent keyEvent) {

        switch (keyEvent.type()) {

            case UP ->
                    state.selectPreviousVersion();

            case DOWN ->
                    state.selectNextVersion();

            case ENTER ->
                    state.confirmVersionSelection();

            case ESCAPE ->
                    state.stopVersionSelection();

            default -> {
                // No action.
            }
        }
    }

    private void handleMetadataEditing(
            CreateProjectState state,
            KeyEvent keyEvent) {

        switch (keyEvent.type()) {

            case ENTER ->
                    state.stopEditing();

            case BACKSPACE ->
                    state.backspace();

            case CHARACTER -> {

                if (keyEvent.hasCharacter()) {

                    state.append(
                            keyEvent.character());
                }
            }

            case ESCAPE ->
                    state.stopEditing();

            default -> {
                // No action.
            }
        }
    }

    private void handleDependencyStage(
            CreateProjectState state,
            KeyEvent keyEvent) {

        if (state.dependencySearchActive()) {

            handleDependencySearch(
                    state,
                    keyEvent);

            return;
        }

        switch (keyEvent.type()) {

            case UP ->
                    state.selectPreviousDependency();

            case DOWN ->
                    state.selectNextDependency();

            case SPACE ->
                    state.toggleSelectedDependency();

            case SEARCH ->
                    state.startDependencySearch();

            case ENTER ->
                    createProjectController.generate(
                            Path.of("")
                                    .toAbsolutePath());

            case ESCAPE ->
                    state.showMetadataStage();

            default -> {
                // No action.
            }
        }
    }

    private void handleDependencySearch(
            CreateProjectState state,
            KeyEvent keyEvent) {

        switch (keyEvent.type()) {

            case UP ->
                    state.selectPreviousDependency();

            case DOWN ->
                    state.selectNextDependency();

            case SPACE ->
                    state.toggleSelectedDependency();

            case ENTER ->
                    state.toggleSelectedDependency();

            case BACKSPACE ->
                    state.backspaceDependencySearch();

            case ESCAPE ->
                    state.stopDependencySearch();

            case CHARACTER -> {

                if (!keyEvent.hasCharacter()) {
                    break;
                }

                if (keyEvent.character() == ' ') {

                    state.toggleSelectedDependency();

                } else {

                    state.appendDependencySearch(
                            keyEvent.character());
                }
            }

            case QUIT ->
                    state.appendDependencySearch(
                            'q');

            case UNDO ->
                    state.appendDependencySearch(
                            'u');

            case ACTIONS ->
                    state.appendDependencySearch(
                            'a');

            case GO_TO_TOP ->
                    state.appendDependencySearch(
                            'g');

            case GO_TO_BOTTOM ->
                    state.appendDependencySearch(
                            'G');

            default -> {
                // No action.
            }
        }
    }
}