package io.github.avinashio.lazyspringboot.ui.state;

import java.util.List;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class QuitState {

    private boolean active;

    private QuitPhase phase =
            QuitPhase.RUNNING_PROJECTS;

    private int selectedOptionIndex;

    private List<String> runningProjects =
            List.of();

    private QuitFocus focus =
            QuitFocus.PROJECTS;

    private int selectedRunningProjectIndex;

    private Set<String> selectedRunningProjects =
            new HashSet<>();

    private final Viewport
            projectViewport =
            new Viewport();

    public boolean active() {

        return active;
    }

    public void open(
            List<String> runningProjects) {

        active = true;

        this.runningProjects =
                List.copyOf(
                        runningProjects);

        phase =
                QuitPhase.RUNNING_PROJECTS;

        focus =
                QuitFocus.PROJECTS;

        selectedRunningProjectIndex = 0;

        projectViewport.reset();

        selectedRunningProjects =
                new HashSet<>(runningProjects);

        selectedOptionIndex = 0;
    }

    public void showReadyToQuit() {

        phase =
                QuitPhase.READY_TO_QUIT;

        focus =
                QuitFocus.ACTIONS;

        selectedOptionIndex = 0;
    }

    public void close() {

        active = false;

        phase =
                QuitPhase.RUNNING_PROJECTS;

        runningProjects =
                List.of();

        selectedRunningProjects =
                new HashSet<>();

        selectedRunningProjectIndex = 0;

        projectViewport.reset();

        focus =
                QuitFocus.PROJECTS;

        selectedOptionIndex = 0;
    }

    public QuitPhase phase() {

        return phase;
    }

    public Viewport projectViewport() {

        return projectViewport;
    }

    public List<String> runningProjects() {

        return runningProjects;
    }

    public int runningProjectCount() {

        return runningProjects.size();
    }

    public List<QuitOption> options() {

        return switch (phase) {

            case RUNNING_PROJECTS ->
                    List.of(
                            QuitOption.STOP_RUNNING,
                            QuitOption.QUIT_ANYWAY,
                            QuitOption.CANCEL);

            case READY_TO_QUIT ->
                    List.of(
                            QuitOption.QUIT,
                            QuitOption.CANCEL);
        };
    }

    public QuitOption selectedOption() {

        return options().get(
                selectedOptionIndex);
    }

    public int selectedOptionIndex() {

        return selectedOptionIndex;
    }

    public void moveNext() {

        if (selectedOptionIndex
                < options().size() - 1) {

            selectedOptionIndex++;
        }
    }

    public void movePrevious() {

        if (selectedOptionIndex > 0) {

            selectedOptionIndex--;
        }
    }

    public boolean runningProjectsPhase() {

        return phase
                == QuitPhase.RUNNING_PROJECTS;
    }

    public boolean readyToQuitPhase() {

        return phase
                == QuitPhase.READY_TO_QUIT;
    }

    public QuitFocus focus() {

        return focus;
    }

    public int selectedRunningProjectIndex() {

        return selectedRunningProjectIndex;
    }

    public Set<String> selectedRunningProjects() {

        return Set.copyOf(
                selectedRunningProjects);
    }

    public void selectNextProject() {

        if (selectedRunningProjectIndex
                < runningProjects.size() - 1) {

            selectedRunningProjectIndex++;

            projectViewport.update(
                    selectedRunningProjectIndex,
                    runningProjects.size(),
                    8);
        }
    }

    public void selectPreviousProject() {

        if (selectedRunningProjectIndex > 0) {

            selectedRunningProjectIndex--;

            projectViewport.update(
                    selectedRunningProjectIndex,
                    runningProjects.size(),
                    8);
        }
    }
    public void toggleSelectedProject() {

        if (runningProjects.isEmpty()) {
            return;
        }

        String project =
                runningProjects.get(
                        selectedRunningProjectIndex);

        if (selectedRunningProjects.contains(
                project)) {

            selectedRunningProjects.remove(
                    project);

        } else {

            selectedRunningProjects.add(
                    project);
        }
    }

    public void selectAllProjects() {

        selectedRunningProjects =
                new HashSet<>(
                        runningProjects);
    }

    public void clearProjectSelection() {

        selectedRunningProjects.clear();
    }

    public void focusProjects() {

        focus =
                QuitFocus.PROJECTS;
    }

    public void focusActions() {

        focus =
                QuitFocus.ACTIONS;
    }

    public void selectFirstProject() {

        if (runningProjects.isEmpty()) {
            return;
        }

        selectedRunningProjectIndex = 0;

        projectViewport.update(
                selectedRunningProjectIndex,
                runningProjects.size(),
                8);
    }

    public void selectLastProject() {

        if (runningProjects.isEmpty()) {
            return;
        }

        selectedRunningProjectIndex =
                runningProjects.size() - 1;

        projectViewport.update(
                selectedRunningProjectIndex,
                runningProjects.size(),
                8);
    }

    public int selectedRunningProjectCount() {

        return selectedRunningProjects.size();
    }
}