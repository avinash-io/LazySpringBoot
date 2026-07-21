package io.github.avinashio.lazyspringboot.ui.service;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProjectFilterService {

    public List<SpringProject> filter(
            List<SpringProject> projects,
            String query) {

        if (query == null || query.isBlank()) {
            return projects;
        }

        String search =
                query.toLowerCase();

        return projects.stream()
                .filter(project ->
                        matches(
                                project,
                                search))
                .toList();
    }

    private boolean matches(
            SpringProject project,
            String query) {

        return project.name()
                .toLowerCase()
                .contains(query)
                || project.path()
                .toString()
                .toLowerCase()
                .contains(query);
    }
}