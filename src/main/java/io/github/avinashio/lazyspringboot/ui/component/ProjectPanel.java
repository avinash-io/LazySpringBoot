package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.controller.TextInputController;
import io.github.avinashio.lazyspringboot.ui.service.VisibleProjectService;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import io.github.avinashio.lazyspringboot.ui.model.ProjectRuntimeInfo;
import io.github.avinashio.lazyspringboot.ui.service.ProjectRuntimeInfoFactory;

@Component
public class ProjectPanel {

    private final StatusFormatter
            statusFormatter;

    private final ProjectBadgeFormatter
            projectBadgeFormatter;

    private final VisibleProjectService
            visibleProjectService;

    private final TextInputController
            textInputController;

    private final ProjectRuntimeInfoFactory
            projectRuntimeInfoFactory;

    private static final int
            SELECTION_WIDTH = 2;

    private static final int
            NAME_WIDTH = 24;

    private static final int
            STATUS_WIDTH = 10;

    private static final int
            PORT_WIDTH = 6;

    private static final int
            UPTIME_WIDTH = 8;

    private static final String
            HEADER_FORMAT =
            "%-" + SELECTION_WIDTH + "s"
                    + "%-" + NAME_WIDTH + "s "
                    + "%-" + STATUS_WIDTH + "s "
                    + "%-" + PORT_WIDTH + "s "
                    + "%-" + UPTIME_WIDTH + "s";

    private static final String
            PROJECT_ROW_FORMAT =
            "%s%-" + NAME_WIDTH + "s "
                    + "%-" + STATUS_WIDTH + "s "
                    + "%-" + PORT_WIDTH + "s "
                    + "%-" + UPTIME_WIDTH + "s%s";

    private static final char
            SEPARATOR_CHARACTER =
            '─';

    public ProjectPanel(
            StatusFormatter statusFormatter,
            ProjectBadgeFormatter projectBadgeFormatter,
            VisibleProjectService visibleProjectService,
            TextInputController textInputController,
            ProjectRuntimeInfoFactory projectRuntimeInfoFactory) {

        this.projectRuntimeInfoFactory =
                projectRuntimeInfoFactory;

        this.statusFormatter =
                statusFormatter;

        this.projectBadgeFormatter =
                projectBadgeFormatter;

        this.visibleProjectService =
                visibleProjectService;

        this.textInputController =
                textInputController;
    }

    public List<String> render(
            UiState state,
            int visibleHeight) {

        List<String> lines =
                new ArrayList<>();

        if (state.projects().isEmpty()) {

            lines.add(
                    " No Spring Boot projects found.");

            return lines;
        }

        List<SpringProject> visibleProjects =
                visibleProjectService.visibleProjects(
                        state.projects());

        if (visibleProjects.isEmpty()) {

            lines.add(
                    " No projects match \""
                            + textInputController.value()
                            + "\"");

            return lines;
        }

        int selectedVisibleIndex =
                selectedVisibleIndex(
                        state,
                        visibleProjects);

        state.projectViewport()
                .update(
                        selectedVisibleIndex,
                        visibleProjects.size(),
                        visibleHeight);

        int start =
                state.projectViewport()
                        .offset();

        int end =
                Math.min(
                        start + visibleHeight,
                        visibleProjects.size());

        lines.add(
                String.format(
                        HEADER_FORMAT,
                        "",
                        "NAME",
                        "STATUS",
                        "PORT",
                        "UPTIME"));

        lines.add(
                separator(
                        lines.getFirst()
                                .length()));

        for (int visibleIndex = start;
             visibleIndex < end;
             visibleIndex++) {

            SpringProject project =
                    visibleProjects.get(
                            visibleIndex);

            int projectIndex =
                    state.projects()
                            .indexOf(project);

            String prefix =
                    projectIndex
                            == state.selectedProjectIndex()
                            ? " > "
                            : "   ";

            lines.add(
                    projectLine(
                            prefix,
                            project));
        }

        return lines;
    }

    private String separator(
            int width) {

        return String.valueOf(
                        SEPARATOR_CHARACTER)
                .repeat(
                        Math.max(
                                width,
                                1));
    }

    private int selectedVisibleIndex(
            UiState state,
            List<SpringProject> visibleProjects) {

        SpringProject selectedProject =
                state.selectedProject();

        if (selectedProject == null) {
            return 0;
        }

        int index =
                visibleProjects.indexOf(
                        selectedProject);

        return Math.max(
                index,
                0);
    }

    private String projectLine(
            String prefix,
            SpringProject project) {

        ProjectRuntimeInfo runtime =
                projectRuntimeInfoFactory.create(
                        project);

        String name =
                project.name()
                        + badges(project);

        return String.format(
                PROJECT_ROW_FORMAT,
                prefix,
                fitName(
                        name),
                statusFormatter.format(
                        runtime.status()),
                runtime.port(),
                runtime.uptime(),
                "");
    }

    private String fitName(
            String name) {

        if (name.length()
                <= NAME_WIDTH) {

            return name;
        }

        return name.substring(
                0,
                NAME_WIDTH - 1)
                + "…";
    }

    private String badges(
            SpringProject project) {

        String badges =
                projectBadgeFormatter.format(
                        project);

        if (badges.isBlank()) {
            return "";
        }

        return " " + badges;
    }
}