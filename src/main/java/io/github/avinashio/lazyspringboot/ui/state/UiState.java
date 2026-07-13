package io.github.avinashio.lazyspringboot.ui.state;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UiState {

    private List<SpringProject> projects = new ArrayList<>();
    private PanelFocus panelFocus = PanelFocus.PROJECTS;

    private int selectedProjectIndex;

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
                    case PROJECTS -> PanelFocus.PROJECT_DETAILS;
                    case PROJECT_DETAILS -> PanelFocus.PROJECTS;
                };
    }

    public void focusPreviousPanel() {
        panelFocus =
                switch (panelFocus) {
                    case PROJECTS -> PanelFocus.PROJECT_DETAILS;
                    case PROJECT_DETAILS -> PanelFocus.PROJECTS;
                };
    }

}