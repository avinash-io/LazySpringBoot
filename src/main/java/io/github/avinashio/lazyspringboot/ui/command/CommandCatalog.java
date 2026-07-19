package io.github.avinashio.lazyspringboot.ui.command;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class CommandCatalog {

    private final UiState uiState;

    private final GetProjectProcessUseCase
            getProjectProcessUseCase;

    public CommandCatalog(
            UiState uiState,
            GetProjectProcessUseCase getProjectProcessUseCase) {

        this.uiState =
                uiState;

        this.getProjectProcessUseCase =
                getProjectProcessUseCase;
    }

    public List<Command> commands() {

        List<Command> commands =
                new ArrayList<>();

        commands.add(
                new Command(
                        "create-project",
                        "Create Spring Boot Project"));

        SpringProject project =
                uiState.selectedProject();

        if (project == null) {
            return List.copyOf(
                    commands);
        }

        commands.add(
                new Command(
                        "refresh-project",
                        "Refresh Selected Project"));

        Optional<ProjectProcess> projectProcess =
                getProjectProcessUseCase.get(
                        project);

        boolean running =
                projectProcess
                        .map(ProjectProcess::running)
                        .orElse(false);

        if (running) {

            commands.add(
                    new Command(
                            "stop-project",
                            "Stop Project"));

            commands.add(
                    new Command(
                            "view-logs",
                            "View Logs"));

        } else {

            commands.add(
                    new Command(
                            "start-project",
                            "Start Project"));
        }

        commands.add(
                new Command(
                        "add-dependencies",
                        "Add Dependencies"));

        commands.add(
                new Command(
                        "undo-dependencies",
                        "Undo Dependency Changes"));

        return List.copyOf(
                commands);
    }
}