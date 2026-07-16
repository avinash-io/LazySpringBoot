package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CreateProjectValidator {

    public List<String> validate(
            CreateProjectState state) {

        List<String> errors =
                new ArrayList<>();

        if (state.name().isBlank()) {
            errors.add(
                    "Project name is required");
        }

        if (state.groupId().isBlank()) {
            errors.add(
                    "Group ID is required");
        }

        if (state.artifactId().isBlank()) {
            errors.add(
                    "Artifact ID is required");
        }

        if (state.packageName().isBlank()) {
            errors.add(
                    "Package name is required");
        }

        return errors;
    }
}