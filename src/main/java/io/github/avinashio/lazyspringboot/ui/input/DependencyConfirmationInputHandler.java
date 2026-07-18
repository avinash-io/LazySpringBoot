package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.application.dependency.ApplyDependenciesUseCase;
import io.github.avinashio.lazyspringboot.application.project.RefreshSelectedProjectUseCase;
import io.github.avinashio.lazyspringboot.ui.service.DependencyItemsService;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class DependencyConfirmationInputHandler
        implements InputHandler {

    private final UiState uiState;

    private final ApplyDependenciesUseCase
            applyDependenciesUseCase;

    private final RefreshSelectedProjectUseCase
            refreshSelectedProjectUseCase;

    private final DependencyItemsService
            dependencyItemsService;

    public DependencyConfirmationInputHandler(
            UiState uiState,
            ApplyDependenciesUseCase applyDependenciesUseCase,
            RefreshSelectedProjectUseCase refreshSelectedProjectUseCase,
            DependencyItemsService dependencyItemsService) {

        this.uiState = uiState;
        this.applyDependenciesUseCase =
                applyDependenciesUseCase;
        this.refreshSelectedProjectUseCase =
                refreshSelectedProjectUseCase;
        this.dependencyItemsService =
                dependencyItemsService;
    }

    @Override
    public boolean handle(
            KeyEvent keyEvent) {

        if (!uiState.dependencyConfirmationActive()) {
            return false;
        }

        switch (keyEvent.type()) {

            case ESCAPE ->
                    uiState.stopDependencyConfirmation();

            case ENTER ->
                    applyDependencies();

            default -> {
                // No action.
            }
        }

        return true;
    }

    private void applyDependencies() {

        var project = uiState.selectedProject();

        int count =
                uiState.selectedDependencyItems().size();

        try {

            applyDependenciesUseCase.apply(
                    project,
                    uiState.selectedDependencyItems());

            var refreshed =
                    refreshSelectedProjectUseCase.refresh(
                            project);

            uiState.replaceSelectedProject(
                    refreshed);

            dependencyItemsService.refresh();

            uiState.clearDependencySelections();

            uiState.stopDependencyConfirmation();

            uiState.showSuccessMessage(
                    "Added "
                            + count
                            + (count == 1
                            ? " dependency to "
                            : " dependencies to ")
                            + project.name());

        } catch (IOException e) {

            uiState.stopDependencyConfirmation();

            String message =
                    e.getMessage();

            if (message == null
                    || message.isBlank()) {

                message =
                        "Failed to apply dependencies";
            } else {

                message =
                        "Failed to apply dependencies: "
                                + message;
            }

            uiState.showErrorMessage(
                    message);
        }
    }
}