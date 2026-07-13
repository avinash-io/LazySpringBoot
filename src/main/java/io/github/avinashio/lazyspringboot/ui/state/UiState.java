package io.github.avinashio.lazyspringboot.ui.state;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;

@Component
public class UiState {

    private List<SpringProject> projects = new ArrayList<>();
    private PanelFocus panelFocus = PanelFocus.PROJECTS;
    private List<SpringDependency> dependencies = List.of();

    private int selectedDependencyIndex;

    private int selectedProjectIndex;

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

    public List<SpringDependency> dependencies() {
        return dependencies;
    }

    public int selectedDependencyIndex() {
        return selectedDependencyIndex;
    }

    public void setDependencies(
            List<SpringDependency> dependencies) {
        this.dependencies = List.copyOf(dependencies);
        selectedDependencyIndex = 0;
        dependencyViewport.reset();
    }

    public SpringDependency selectedDependency() {
        if (dependencies.isEmpty()) {
            return null;
        }

        return dependencies.get(selectedDependencyIndex);
    }

    public void selectNextDependency() {
        if (selectedDependencyIndex < dependencies.size() - 1) {
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