package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.domain.action.ActionItem;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectActionController;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectActionExecutor;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectActionsInputHandler
        implements InputHandler {

    private final UiState uiState;

    private final ProjectActionController
            projectActionController;

    private final ProjectActionExecutor
            projectActionExecutor;

    public ProjectActionsInputHandler(
            UiState uiState,
            ProjectActionController projectActionController,
            ProjectActionExecutor projectActionExecutor) {

        this.uiState = uiState;
        this.projectActionController =
                projectActionController;
        this.projectActionExecutor =
                projectActionExecutor;
    }

    @Override
    public boolean handle(
            KeyEvent keyEvent) {

        if (!uiState.projectActionsActive()) {
            return false;
        }

        List<ActionItem> actions =
                projectActionController.actions(
                        uiState.selectedProject());

        if (projectActionController.handleKey(
                keyEvent,
                actions)) {
            return true;
        }

        if (keyEvent.type() == KeyType.ENTER) {
            executeSelectedAction(actions);
        }

        return true;
    }

    private void executeSelectedAction(
            List<ActionItem> actions) {

        if (actions.isEmpty()) {
            return;
        }

        int selectedIndex =
                uiState.selectedProjectActionIndex();

        if (selectedIndex < 0
                || selectedIndex >= actions.size()) {
            return;
        }

        ActionItem selectedAction =
                actions.get(selectedIndex);

        if (!projectActionExecutor.canExecute(
                selectedAction)) {
            return;
        }

        SpringProject project =
                uiState.selectedProject();

        if (project == null) {
            uiState.stopProjectActions();
            uiState.showErrorMessage(
                    "No project selected");
            return;
        }

        projectActionExecutor.execute(
                project,
                selectedAction);
    }
}