package io.github.avinashio.lazyspringboot.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import org.junit.jupiter.api.Test;

class CreateProjectValidatorTest {

    @Test
    void shouldReturnErrorsForEmptyFields() {

        CreateProjectState state =
                new CreateProjectState();

        CreateProjectValidator validator =
                new CreateProjectValidator();

        assertThat(
                validator.validate(state))
                .hasSize(3)
                .containsExactly(
                        "Project name is required",
                        "Artifact ID is required",
                        "Package name is required");
    }

    @Test
    void shouldAcceptValidState() {

        CreateProjectState state =
                new CreateProjectState();

        state.setName("demo");
        state.setGroupId("com.example");
        state.setArtifactId("demo");
        state.setPackageName("com.example.demo");

        CreateProjectValidator validator =
                new CreateProjectValidator();

        assertThat(
                validator.validate(state))
                .isEmpty();
    }
}