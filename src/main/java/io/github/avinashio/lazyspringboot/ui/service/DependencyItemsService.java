package io.github.avinashio.lazyspringboot.ui.service;

import io.github.avinashio.lazyspringboot.application.dependency.BuildDependencyItemsUseCase;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DependencyItemsService {

    private final UiState uiState;

    private final BuildDependencyItemsUseCase
            buildDependencyItemsUseCase;

    private List<SpringDependency> dependencyCatalog =
            List.of();

    public DependencyItemsService(
            UiState uiState,
            BuildDependencyItemsUseCase buildDependencyItemsUseCase) {

        this.uiState =
                uiState;

        this.buildDependencyItemsUseCase =
                buildDependencyItemsUseCase;
    }

    public void initialize(
            List<SpringDependency> dependencyCatalog) {

        this.dependencyCatalog =
                List.copyOf(
                        dependencyCatalog);
    }

    public List<SpringDependency> catalog() {

        return dependencyCatalog;
    }

    public void refresh() {

        uiState.setDependencyItems(
                buildDependencyItemsUseCase.build(
                        dependencyCatalog,
                        uiState.selectedProject()));
    }
}