package io.github.avinashio.lazyspringboot.ui.service;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.state.ProjectSortMode;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectSortService {

    public List<SpringProject> sort(
            List<SpringProject> projects,
            ProjectSortMode mode) {

        Comparator<SpringProject> comparator =
                comparator(
                        mode);

        return projects.stream()
                .sorted(comparator)
                .toList();
    }

    private Comparator<SpringProject> comparator(
            ProjectSortMode mode) {

        return switch (mode) {

            case NAME_ASC ->
                    Comparator.comparing(
                            SpringProject::name,
                            String.CASE_INSENSITIVE_ORDER);

            case NAME_DESC ->
                    Comparator.comparing(
                                    SpringProject::name,
                                    String.CASE_INSENSITIVE_ORDER)
                            .reversed();

            case PATH_ASC ->
                    Comparator.comparing(
                            project ->
                                    project.path()
                                            .toString(),
                            String.CASE_INSENSITIVE_ORDER);
        };
    }
}