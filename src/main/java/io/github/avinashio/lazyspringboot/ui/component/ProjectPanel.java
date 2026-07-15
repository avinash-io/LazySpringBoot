package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectPanel {

    private final GetProjectProcessUseCase
            getProjectProcessUseCase;

    private final StatusFormatter
            statusFormatter;

    private final ProjectBadgeFormatter
            projectBadgeFormatter;

    public ProjectPanel(
            GetProjectProcessUseCase getProjectProcessUseCase,
            StatusFormatter statusFormatter,
            ProjectBadgeFormatter projectBadgeFormatter) {

        this.getProjectProcessUseCase =
                getProjectProcessUseCase;

        this.statusFormatter =
                statusFormatter;

        this.projectBadgeFormatter =
                projectBadgeFormatter;
    }

    public List<String> render(
            UiState state) {

        List<String> lines =
                new ArrayList<>();

        if (state.projects().isEmpty()) {
            lines.add(
                    " No Spring Boot projects found.");

            return lines;
        }

        for (int index = 0;
             index < state.projects().size();
             index++) {

            SpringProject project =
                    state.projects().get(index);

            String prefix =
                    index
                            == state.selectedProjectIndex()
                            ? " > "
                            : "   ";

            lines.add(
                    prefix
                            + statusIcon(project)
                            + " "
                            + project.name()
                            + badges(project));
        }

        return lines;
    }

    private String statusIcon(
            SpringProject project) {

        return getProjectProcessUseCase
                .get(project)
                .map(process ->
                        statusFormatter.icon(
                                process.status()))
                .orElseGet(
                        () ->
                                statusFormatter.icon(
                                        ProjectProcessStatus.STOPPED));
    }

    private String badges(
            SpringProject project) {

        String badges =
                projectBadgeFormatter.format(
                        project);

        if (badges.isBlank()) {
            return "";
        }

        return "  " + badges;
    }
}