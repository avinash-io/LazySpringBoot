package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.NewProjectRequest;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CreateProjectRequestMapper {

    public NewProjectRequest map(
            CreateProjectState state) {

        return new NewProjectRequest(
                state.groupId(),
                state.artifactId(),
                state.name(),
                state.packageName(),
                state.javaVersion(),
                state.springBootVersion(),
                BuildTool.MAVEN,
                List.of());
    }
}