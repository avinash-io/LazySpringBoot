package io.github.avinashio.lazyspringboot.ui.state;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UiState {

    private final List<String> projects =
            List.of("cv-api", "payment-service", "demo");

    private int selectedProjectIndex;

    public List<String> projects() {
        return projects;
    }

    public int selectedProjectIndex() {
        return selectedProjectIndex;
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
}