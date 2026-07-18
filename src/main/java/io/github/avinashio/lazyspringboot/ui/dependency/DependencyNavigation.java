package io.github.avinashio.lazyspringboot.ui.dependency;

import io.github.avinashio.lazyspringboot.ui.component.DependencyFilter;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DependencyNavigation {

    private final UiState uiState;
    private final DependencyFilter dependencyFilter;

    public DependencyNavigation(
            UiState uiState,
            DependencyFilter dependencyFilter) {

        this.uiState = uiState;
        this.dependencyFilter = dependencyFilter;
    }

    public void selectNextVisible() {

        List<Integer> indexes = visibleIndexes();

        int current =
                indexes.indexOf(
                        uiState.selectedDependencyIndex());

        if (current < 0) {
            selectFirstVisible(indexes);
            return;
        }

        if (current < indexes.size() - 1) {
            uiState.selectDependency(
                    indexes.get(current + 1));
        }
    }

    public void selectPreviousVisible() {

        List<Integer> indexes = visibleIndexes();

        int current =
                indexes.indexOf(
                        uiState.selectedDependencyIndex());

        if (current < 0) {
            selectFirstVisible(indexes);
            return;
        }

        if (current > 0) {
            uiState.selectDependency(
                    indexes.get(current - 1));
        }
    }

    public void selectFirstVisible() {
        selectFirstVisible(visibleIndexes());
    }

    private void selectFirstVisible(
            List<Integer> indexes) {

        if (indexes.isEmpty()) {
            return;
        }

        uiState.selectDependency(
                indexes.getFirst());
    }

    private List<Integer> visibleIndexes() {

        return dependencyFilter.matchingIndexes(
                uiState.dependencyItems(),
                uiState.dependencySearchQuery());
    }
}