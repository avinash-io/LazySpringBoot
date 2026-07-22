package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.StopProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.state.QuitFocus;
import io.github.avinashio.lazyspringboot.ui.state.QuitOption;
import io.github.avinashio.lazyspringboot.ui.state.QuitState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Controller;

@Controller
public class QuitController {

    private final QuitState quitState;

    private final UiState uiState;

    private final GetProjectProcessUseCase
            getProjectProcessUseCase;

    private final StopProjectProcessUseCase
            stopProjectProcessUseCase;

    private final ProjectRefreshController
            projectRefreshController;

    public QuitController(
            QuitState quitState,
            UiState uiState,
            GetProjectProcessUseCase
                    getProjectProcessUseCase,
            StopProjectProcessUseCase
                    stopProjectProcessUseCase,
            ProjectRefreshController
                    projectRefreshController) {

        this.quitState =
                quitState;

        this.uiState =
                uiState;

        this.getProjectProcessUseCase =
                getProjectProcessUseCase;

        this.stopProjectProcessUseCase =
                stopProjectProcessUseCase;

        this.projectRefreshController =
                projectRefreshController;
    }

    public QuitDecision requestQuit() {

        List<SpringProject> runningProjects =
                runningProjects();

        if (runningProjects.isEmpty()) {
            return QuitDecision.QUIT;
        }

        quitState.open(
                runningProjects.stream()
                        .map(SpringProject::name)
                        .toList());

        return QuitDecision.OPEN_POPUP;
    }

    public boolean active() {

        return quitState.active();
    }

    public QuitState state() {

        return quitState;
    }

    public void moveNext() {

        quitState.moveNext();
    }

    public void movePrevious() {

        quitState.movePrevious();
    }

    public void cancel() {

        quitState.close();
    }

    public QuitDecision executeSelection() {

        return switch (
                quitState.selectedOption()) {

            case STOP_RUNNING -> {

                if (stopRunningProjects()) {

                    quitState.showReadyToQuit();
                }

                yield QuitDecision.CONTINUE;
            }

            case QUIT_ANYWAY -> {

                quitState.close();

                yield QuitDecision.QUIT;
            }

            case QUIT -> {

                quitState.close();

                yield QuitDecision.QUIT;
            }

            case CANCEL -> {

                quitState.close();

                yield QuitDecision.CONTINUE;
            }
        };
    }

    private boolean stopRunningProjects() {

        Set<String> selectedProjects =
                quitState.selectedRunningProjects();

        if (selectedProjects.isEmpty()) {

            uiState.showWarningMessage(
                    "Select at least one project.");

            return false;
        }

        for (SpringProject project :
                runningProjects()) {

            if (!selectedProjects.contains(
                    project.name())) {

                continue;
            }

            stopProjectProcessUseCase.stop(
                    project);
        }

        try {

            projectRefreshController.refresh();

            return true;

        } catch (IOException exception) {

            uiState.showErrorMessage(
                    "Failed to refresh projects.");

            return false;
        }
    }

    private List<SpringProject> runningProjects() {

        List<SpringProject> runningProjects =
                new ArrayList<>();

        for (SpringProject project :
                uiState.projects()) {

            boolean running =
                    getProjectProcessUseCase
                            .get(project)
                            .map(ProjectProcess::running)
                            .orElse(false);

            if (running) {

                runningProjects.add(
                        project);
            }
        }

        return runningProjects;
    }

    public void focusNext() {

        if (quitState.focus()
                == QuitFocus.PROJECTS) {

            quitState.selectNextProject();

            return;
        }

        quitState.moveNext();
    }

    public void focusPrevious() {

        if (quitState.focus()
                == QuitFocus.PROJECTS) {

            quitState.selectPreviousProject();

            return;
        }

        quitState.movePrevious();
    }

    public void focusFirst() {

        if (quitState.focus()
                == QuitFocus.PROJECTS) {

            quitState.selectFirstProject();

            return;
        }

        while (quitState.selectedOptionIndex() > 0) {

            quitState.movePrevious();
        }
    }

    public void focusLast() {

        if (quitState.focus()
                == QuitFocus.PROJECTS) {

            quitState.selectLastProject();

            return;
        }

        while (quitState.selectedOptionIndex()
                < quitState.options().size() - 1) {

            quitState.moveNext();
        }
    }
}