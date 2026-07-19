package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.project.CreateSpringProjectUseCase;
import io.github.avinashio.lazyspringboot.domain.project.NewProjectRequest;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Component;
import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.service.DependencyItemsService;

@Component
public class CreateProjectController {

    private final CreateProjectState
            createProjectState;

    private final CreateProjectRequestMapper
            requestMapper;

    private final CreateSpringProjectUseCase
            createSpringProjectUseCase;

    private final UiState uiState;

    private final CreateProjectValidator
            validator;

    private final DiscoverProjectsUseCase
            discoverProjectsUseCase;

    private final DependencyItemsService
            dependencyItemsService;

    public CreateProjectController(
            CreateProjectState createProjectState,
            CreateProjectRequestMapper requestMapper,
            CreateSpringProjectUseCase createSpringProjectUseCase,
            DiscoverProjectsUseCase discoverProjectsUseCase,
            UiState uiState,
            CreateProjectValidator validator,
            DependencyItemsService dependencyItemsService) {

        this.createProjectState =
                createProjectState;
        this.requestMapper =
                requestMapper;
        this.createSpringProjectUseCase =
                createSpringProjectUseCase;
        this.discoverProjectsUseCase =
                discoverProjectsUseCase;
        this.uiState =
                uiState;
        this.validator =
                validator;
        this.dependencyItemsService =
                dependencyItemsService;
    }

    public void open() {

        createProjectState.setDependencies(
                dependencyItemsService.catalog());

        createProjectState.open();
    }

    public void close() {
        createProjectState.close();
    }

    public CreateProjectState state() {
        return createProjectState;
    }



    public boolean generate(
            Path destination) {

        createProjectState.clearErrorMessage();

        List<String> errors =
                validator.validate(
                        createProjectState);

        if (!errors.isEmpty()) {

            createProjectState.showErrorMessage(
                    errors.getFirst());

            return false;
        }

        try {

            NewProjectRequest request =
                    requestMapper.map(
                            createProjectState);

            createSpringProjectUseCase.create(
                    request,
                    destination);

            List<SpringProject> projects =
                    discoverProjectsUseCase.discover(
                            destination);

            uiState.setProjects(
                    projects);

            createProjectState.close();

            uiState.showSuccessMessage(
                    "Project created successfully");

            return true;

        } catch (IOException
                 | InterruptedException exception) {

            if (exception
                    instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }

            createProjectState.showErrorMessage(
                    "Failed to create project: "
                            + exception.getMessage());

            return false;
        }
    }

    public boolean continueToDependencies() {

        createProjectState.clearErrorMessage();

        List<String> errors =
                validator.validate(
                        createProjectState);

        if (!errors.isEmpty()) {

            createProjectState.showErrorMessage(
                    errors.getFirst());

            return false;
        }

        createProjectState.showDependencyStage();

        return true;
    }
}