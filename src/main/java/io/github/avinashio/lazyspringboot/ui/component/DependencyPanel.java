package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyAvailability;
import io.github.avinashio.lazyspringboot.domain.dependency.DependencyItem;

@Component
public class DependencyPanel {

    public List<String> render(
            UiState state,
            int visibleHeight) {
        List<String> lines = new ArrayList<>();

        if (state.dependencyItems().isEmpty()) {
            lines.add(" No dependencies available.");
            return lines;
        }

        state
                .dependencyViewport()
                .update(
                        state.selectedDependencyIndex(),
                        state.dependencyItems().size(),
                        visibleHeight);

        int start = state.dependencyViewport().offset();

        int end =
                Math.min(
                        start + visibleHeight,
                        state.dependencyItems().size());

        for (int index = start; index < end; index++) {
            DependencyItem item =
                    state.dependencyItems().get(index);

            String cursor =
                    index == state.selectedDependencyIndex()
                            ? ">"
                            : " ";

            String marker =
                    item.availability()
                            == DependencyAvailability.ALREADY_PRESENT
                            ? "*"
                            : " ";

            lines.add(
                    " "
                            + cursor
                            + marker
                            + " "
                            + item.dependency().name());
        }

        return lines;
    }
}