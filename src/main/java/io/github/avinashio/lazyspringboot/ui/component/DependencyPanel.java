package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DependencyPanel {

    public List<String> render(
            UiState state,
            int visibleHeight) {
        List<String> lines = new ArrayList<>();

        if (state.dependencies().isEmpty()) {
            lines.add(" No dependencies available.");
            return lines;
        }

        state
                .dependencyViewport()
                .update(
                        state.selectedDependencyIndex(),
                        state.dependencies().size(),
                        visibleHeight);

        int start = state.dependencyViewport().offset();

        int end =
                Math.min(
                        start + visibleHeight,
                        state.dependencies().size());

        for (int index = start; index < end; index++) {
            SpringDependency dependency =
                    state.dependencies().get(index);

            String prefix =
                    index == state.selectedDependencyIndex()
                            ? " > "
                            : "   ";

            lines.add(prefix + dependency.name());
        }

        return lines;
    }
}