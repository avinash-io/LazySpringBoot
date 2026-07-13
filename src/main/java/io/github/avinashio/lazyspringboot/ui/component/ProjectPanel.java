package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectPanel {

    public List<String> render(UiState state) {
        List<String> lines = new ArrayList<>();

        if (state.projects().isEmpty()) {
            lines.add(" No Spring Boot projects found.");
            return lines;
        }

        for (int index = 0; index < state.projects().size(); index++) {
            SpringProject project = state.projects().get(index);

            if (index == state.selectedProjectIndex()) {
                lines.add(" > " + project.name());
            } else {
                lines.add("   " + project.name());
            }
        }

        return lines;
    }
}