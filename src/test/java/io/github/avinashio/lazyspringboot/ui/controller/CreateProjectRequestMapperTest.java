package io.github.avinashio.lazyspringboot.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.project.NewProjectRequest;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import org.junit.jupiter.api.Test;

class CreateProjectRequestMapperTest {

    @Test
    void shouldMapState() {

        CreateProjectState state =
                new CreateProjectState();

        state.setName("demo");
        state.setGroupId("com.example");
        state.setArtifactId("demo");
        state.setPackageName("com.example.demo");

        CreateProjectRequestMapper mapper =
                new CreateProjectRequestMapper();

        NewProjectRequest request =
                mapper.map(state);

        assertThat(request.name())
                .isEqualTo("demo");

        assertThat(request.groupId())
                .isEqualTo("com.example");

        assertThat(request.artifactId())
                .isEqualTo("demo");
    }
}