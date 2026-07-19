package io.github.avinashio.lazyspringboot.ui.state;

import io.github.avinashio.lazyspringboot.domain.action.ProjectActionOutput;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class UiState {

    private UiMessage message;

    private List<SpringProject> projects =
            new ArrayList<>();

    private List<DependencyItem> dependencyItems =
            List.of();

    private Set<String> selectedDependencyIds =
            Set.of();

    private PanelFocus panelFocus =
            PanelFocus.PROJECTS;

    private InputMode inputMode =
            InputMode.NAVIGATION;

    private String dependencySearchQuery = "";

    private int selectedProjectIndex;

    private int selectedDependencyIndex;

    private boolean projectActionsActive;

    private int selectedProjectActionIndex;

    private ProjectActionOutput projectActionOutput;

    private final Viewport dependencyViewport =
            new Viewport();

    private final OutputViewport outputViewport =
            new OutputViewport();

    public List<SpringProject> projects() {
        return projects;
    }

    public void setProjects(
            List<SpringProject> projects) {
        this.projects =
                List.copyOf(projects);

        selectedProjectIndex = 0;
    }

    public int selectedProjectIndex() {
        return selectedProjectIndex;
    }

    public void selectNextProject() {
        if (selectedProjectIndex
                < projects.size() - 1) {
            selectedProjectIndex++;
        }
    }

    public void selectPreviousProject() {
        if (selectedProjectIndex > 0) {
            selectedProjectIndex--;
        }
    }

    public SpringProject selectedProject() {
        if (projects.isEmpty()) {
            return null;
        }

        return projects.get(
                selectedProjectIndex);
    }

    public void replaceSelectedProject(
            SpringProject project) {
        if (project == null
                || projects.isEmpty()) {
            return;
        }

        List<SpringProject> updatedProjects =
                new ArrayList<>(projects);

        updatedProjects.set(
                selectedProjectIndex,
                project);

        projects =
                List.copyOf(updatedProjects);
    }

    public PanelFocus panelFocus() {
        return panelFocus;
    }

    public void focusPanel(
            PanelFocus panelFocus) {

        this.panelFocus =
                panelFocus;
    }

    public void focusNextPanel() {
        panelFocus =
                switch (panelFocus) {
                    case PROJECTS ->
                            PanelFocus.DEPENDENCIES;

                    case DEPENDENCIES ->
                            PanelFocus.PROJECT_DETAILS;

                    case PROJECT_DETAILS ->
                            PanelFocus.PROJECTS;
                };
    }

    public void focusPreviousPanel() {
        panelFocus =
                switch (panelFocus) {
                    case PROJECTS ->
                            PanelFocus.PROJECT_DETAILS;

                    case DEPENDENCIES ->
                            PanelFocus.PROJECTS;

                    case PROJECT_DETAILS ->
                            PanelFocus.DEPENDENCIES;
                };
    }

    public List<DependencyItem> dependencyItems() {
        return dependencyItems;
    }

    public void setDependencyItems(
            List<DependencyItem> dependencyItems) {
        this.dependencyItems =
                dependencyItems.stream()
                        .map(
                                item ->
                                        new DependencyItem(
                                                item.dependency(),
                                                item.availability(),
                                                selectedDependencyIds
                                                        .contains(
                                                                item
                                                                        .dependency()
                                                                        .id())
                                                        && item.selectable()))
                        .toList();

        selectedDependencyIndex = 0;

        dependencyViewport.reset();
    }

    public int selectedDependencyIndex() {
        return selectedDependencyIndex;
    }

    public DependencyItem selectedDependencyItem() {
        if (dependencyItems.isEmpty()) {
            return null;
        }

        return dependencyItems.get(
                selectedDependencyIndex);
    }

    public void selectNextDependency() {
        if (selectedDependencyIndex
                < dependencyItems.size() - 1) {
            selectedDependencyIndex++;
        }
    }

    public void selectPreviousDependency() {
        if (selectedDependencyIndex > 0) {
            selectedDependencyIndex--;
        }
    }

    public void selectDependency(
            int dependencyIndex) {
        if (dependencyIndex < 0
                || dependencyIndex
                >= dependencyItems.size()) {
            return;
        }

        selectedDependencyIndex =
                dependencyIndex;
    }

    public Viewport dependencyViewport() {
        return dependencyViewport;
    }

    public Set<String> selectedDependencyIds() {
        return selectedDependencyIds;
    }

    public void toggleSelectedDependency() {
        if (dependencyItems.isEmpty()) {
            return;
        }

        DependencyItem current =
                dependencyItems.get(
                        selectedDependencyIndex);

        if (!current.selectable()) {
            return;
        }

        String dependencyId =
                current.dependency().id();

        Set<String> updatedIds =
                new HashSet<>(
                        selectedDependencyIds);

        if (updatedIds.contains(
                dependencyId)) {
            updatedIds.remove(
                    dependencyId);
        } else {
            updatedIds.add(
                    dependencyId);
        }

        selectedDependencyIds =
                Set.copyOf(updatedIds);

        updateDependencySelection();
    }

    private void updateDependencySelection() {
        dependencyItems =
                dependencyItems.stream()
                        .map(
                                item ->
                                        new DependencyItem(
                                                item.dependency(),
                                                item.availability(),
                                                selectedDependencyIds
                                                        .contains(
                                                                item
                                                                        .dependency()
                                                                        .id())
                                                        && item.selectable()))
                        .toList();
    }

    public InputMode inputMode() {
        return inputMode;
    }

    public String dependencySearchQuery() {
        return dependencySearchQuery;
    }

    public boolean dependencySearchActive() {
        return inputMode
                == InputMode.DEPENDENCY_SEARCH;
    }

    public void startDependencySearch() {
        inputMode =
                InputMode.DEPENDENCY_SEARCH;

        dependencySearchQuery = "";
    }

    public void stopDependencySearch() {
        inputMode =
                InputMode.NAVIGATION;

        dependencySearchQuery = "";
    }

    public void appendDependencySearchCharacter(
            char character) {
        if (!dependencySearchActive()) {
            return;
        }

        dependencySearchQuery += character;
    }

    public void removeLastDependencySearchCharacter() {
        if (!dependencySearchActive()
                || dependencySearchQuery.isEmpty()) {
            return;
        }

        dependencySearchQuery =
                dependencySearchQuery.substring(
                        0,
                        dependencySearchQuery.length() - 1);
    }

    public boolean dependencyConfirmationActive() {
        return inputMode
                == InputMode.DEPENDENCY_CONFIRMATION;
    }

    public void startDependencyConfirmation() {
        if (selectedDependencyIds.isEmpty()) {
            return;
        }

        inputMode =
                InputMode.DEPENDENCY_CONFIRMATION;
    }

    public void stopDependencyConfirmation() {
        if (!dependencyConfirmationActive()) {
            return;
        }

        inputMode =
                InputMode.NAVIGATION;
    }

    public List<DependencyItem>
    selectedDependencyItems() {
        return dependencyItems.stream()
                .filter(
                        item ->
                                selectedDependencyIds.contains(
                                        item
                                                .dependency()
                                                .id()))
                .filter(
                        DependencyItem::selectable)
                .toList();
    }

    public void clearDependencySelections() {
        selectedDependencyIds = Set.of();

        updateDependencySelection();
    }

    public UiMessage message() {
        return message;
    }

    public void showSuccessMessage(
            String text) {
        message =
                new UiMessage(
                        UiMessageType.SUCCESS,
                        text);
    }

    public void showErrorMessage(
            String text) {
        message =
                new UiMessage(
                        UiMessageType.ERROR,
                        text);
    }

    public void clearMessage() {
        message = null;
    }

    public boolean hasMessage() {
        return message != null;
    }

    public boolean projectActionsActive() {
        return projectActionsActive;
    }

    public void startProjectActions() {
        projectActionsActive = true;

        selectedProjectActionIndex = 0;
    }

    public void stopProjectActions() {
        projectActionsActive = false;
    }

    public int selectedProjectActionIndex() {
        return selectedProjectActionIndex;
    }

    public void selectNextProjectAction(
            int actionCount) {
        if (actionCount <= 0) {
            return;
        }

        if (selectedProjectActionIndex
                < actionCount - 1) {
            selectedProjectActionIndex++;
        }
    }

    public void selectPreviousProjectAction() {
        if (selectedProjectActionIndex > 0) {
            selectedProjectActionIndex--;
        }
    }

    public boolean projectActionOutputActive() {
        return projectActionOutput != null;
    }

    public ProjectActionOutput projectActionOutput() {
        return projectActionOutput;
    }

    public void showProjectActionOutput(
            ProjectActionOutput output,
            int visibleHeight) {
        projectActionOutput = output;

        outputViewport.moveToBottom(
                output.lines().size(),
                visibleHeight);
    }

    public void replaceProjectActionOutput(
            ProjectActionOutput output) {
        projectActionOutput = output;
    }

    public void closeProjectActionOutput() {
        projectActionOutput = null;

        outputViewport.reset();
    }

    public OutputViewport outputViewport() {
        return outputViewport;
    }


}