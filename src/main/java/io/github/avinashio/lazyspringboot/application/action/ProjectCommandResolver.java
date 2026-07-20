package io.github.avinashio.lazyspringboot.application.action;

import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectCommand;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectCommandResolver {

    public ProjectCommand resolve(
            SpringProject project,
            ProjectAction action) {

        if (project.metadata().buildTool()
                != BuildTool.MAVEN) {

            throw new IllegalArgumentException(
                    "Unsupported build tool: "
                            + project
                            .metadata()
                            .buildTool());
        }

        return new ProjectCommand(
                resolveMavenCommand(
                        project,
                        action),
                project.path());
    }

    private List<String> resolveMavenCommand(
            SpringProject project,
            ProjectAction action) {

        List<String> command =
                new ArrayList<>(
                        resolveMavenCommandPrefix(
                                project));

        switch (action) {

            case BUILD -> {
                command.add("clean");
                command.add("package");
            }

            case TEST ->
                    command.add(
                            "test");

            case RUN, VIEW_LOGS, RESTART, STOP ->
                    throw new IllegalArgumentException(
                            "Action is not a command action: "
                                    + action);
        }

        return List.copyOf(
                command);
    }

    private List<String> resolveMavenCommandPrefix(
            SpringProject project) {

        Path mavenWrapper =
                project
                        .path()
                        .resolve(
                                "mvnw");

        if (Files.isRegularFile(
                mavenWrapper)) {

            return List.of(
                    "sh",
                    "./mvnw");
        }

        return List.of(
                "mvn");
    }
}