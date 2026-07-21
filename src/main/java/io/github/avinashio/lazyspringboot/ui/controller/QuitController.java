package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;
import io.github.avinashio.lazyspringboot.ui.input.KeyType;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.springframework.stereotype.Controller;

@Controller
public class QuitController {

    private final UiState uiState;

    private final GetProjectProcessUseCase
            getProjectProcessUseCase;

    private boolean confirmationPending;

    public QuitController(
            UiState uiState,
            GetProjectProcessUseCase getProjectProcessUseCase) {

        this.uiState =
                uiState;

        this.getProjectProcessUseCase =
                getProjectProcessUseCase;
    }

    public QuitDecision requestQuit() {

        if (confirmationPending) {
            return QuitDecision.QUIT;
        }

        int activeProjectCount =
                activeProjectCount();

        if (activeProjectCount == 0) {
            return QuitDecision.QUIT;
        }

        confirmationPending = true;

        uiState.showWarningMessage(
                buildWarningMessage(
                        activeProjectCount));

        return QuitDecision.WARNING;
    }

    public QuitDecision handleConfirmation(
            KeyEvent keyEvent) {

        if (!confirmationPending) {
            return QuitDecision.CONTINUE;
        }

        if (keyEvent.type()
                == KeyType.ESCAPE) {

            cancel();

            return QuitDecision.CONTINUE;
        }

        if (keyEvent.type()
                != KeyType.CHARACTER
                || !keyEvent.hasCharacter()) {

            return QuitDecision.CONTINUE;
        }

        char character =
                keyEvent.character();

        if (character == 'q'
                || character == 'y') {

            return QuitDecision.QUIT;
        }

        if (character == 'n') {

            cancel();
        }

        return QuitDecision.CONTINUE;
    }

    public boolean confirmationPending() {
        return confirmationPending;
    }

    public void cancel() {

        confirmationPending = false;

        uiState.clearMessage();
    }

    private int activeProjectCount() {

        int count = 0;

        for (SpringProject project :
                uiState.projects()) {

            boolean active =
                    getProjectProcessUseCase
                            .get(project)
                            .map(ProjectProcess::running)
                            .orElse(false);

            if (active) {
                count++;
            }
        }

        return count;
    }

    private String buildWarningMessage(
            int activeProjectCount) {

        String projectLabel =
                activeProjectCount == 1
                        ? "project is"
                        : "projects are";

        return activeProjectCount
                + " "
                + projectLabel
                + " still running. "
                + "Press q/y to quit anyway "
                + "or Esc/n to cancel.";
    }
}