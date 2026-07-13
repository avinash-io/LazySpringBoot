package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.domain.project.ProjectMetadata;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectDetailsPanel {

    private static final int LABEL_WIDTH = 14;

    private final TextFormatter textFormatter;

    public ProjectDetailsPanel(TextFormatter textFormatter) {
        this.textFormatter = textFormatter;
    }

    public List<String> render(SpringProject project) {
        if (project == null) {
            return List.of();
        }

        ProjectMetadata metadata = project.metadata();

        return List.of(
                detail("Artifact", metadata.artifactId()),
                detail("Group", metadata.groupId()),
                detail("Spring Boot", metadata.springBootVersion()),
                detail("Java", metadata.javaVersion()),
                detail("Build", metadata.buildTool().name()),
                detail("Path", project.path().toString()));
    }

    private String detail(String label, String value) {
        return " "
                + textFormatter.fit(label, LABEL_WIDTH)
                + displayValue(value);
    }

    private String displayValue(String value) {
        if (value == null || value.isBlank()) {
            return "Unknown";
        }

        return value;
    }
}