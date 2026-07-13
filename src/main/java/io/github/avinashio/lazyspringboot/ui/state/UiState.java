package io.github.avinashio.lazyspringboot.ui.state;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UiState {

    private List<SpringProject> projects = new ArrayList<>();
    private List<DependencyItem> dependencyItems = List.of();

    private PanelFocus panelFocus = PanelFocus.PROJECTS;

    private int selectedProjectIndex;
    private int selectedDependencyIndex;

    private final Viewport dependencyViewport =
            new Viewport();

    public List<SpringProject> projects() {
        return projects;
    }

    public int selectedProjectIndex() {
        return selectedProjectIndex;
    }

    public void setProjects(List<SpringProject> projects) {
        this.projects = List.copyOf(projects);
        selectedProjectIndex = 0;
    }

    public void selectNextProject() {
        if (selectedProjectIndex < projects.size() - 1) {
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

        return projects.get(selectedProjectIndex);
    }

    public PanelFocus panelFocus() {
        return panelFocus;
    }

    public void focusNextPanel() {
        panelFocus =
                switch (panelFocus) {
                    case PROJECTS -> PanelFocus.DEPENDENCIES;
                    case DEPENDENCIES -> PanelFocus.PROJECT_DETAILS;
                    case PROJECT_DETAILS -> PanelFocus.PROJECTS;
                };
    }

    public void focusPreviousPanel() {
        panelFocus =
                switch (panelFocus) {
                    case PROJECTS -> PanelFocus.PROJECT_DETAILS;
                    case DEPENDENCIES -> PanelFocus.PROJECTS;
                    case PROJECT_DETAILS -> PanelFocus.DEPENDENCIES;
                };
    }

    public List<DependencyItem> dependencyItems() {
        return dependencyItems;
    }

    public int selectedDependencyIndex() {
        return selectedDependencyIndex;
    }

    public void setDependencyItems(
            List<DependencyItem> dependencyItems) {
        this.dependencyItems =
                List.copyOf(dependencyItems);

        selectedDependencyIndex = 0;
        dependencyViewport.reset();
    }

    public DependencyItem selectedDependencyItem() {
        if (dependencyItems.isEmpty()) {
            return null;
        }

        return dependencyItems.get(selectedDependencyIndex);
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

    public Viewport dependencyViewport() {
        return dependencyViewport;
    }
}