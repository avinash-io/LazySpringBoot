package io.github.avinashio.lazyspringboot.application.action;

import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectCommand;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.nio.file.Path;
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
        String executable =
                resolveMavenExecutable(project);

        return switch (action) {
            case BUILD ->
                    List.of(
                            executable,
                            "clean",
                            "package");

            case TEST ->
                    List.of(
                            executable,
                            "test");

            case RUN, VIEW_LOGS, STOP ->
                    throw new IllegalArgumentException(
                            "Action is not a command action: "
                                    + action);
        };
    }

    private String resolveMavenExecutable(
            SpringProject project) {
        Path mavenWrapper =
                project
                        .path()
                        .resolve("mvnw");

        if (mavenWrapper.toFile().isFile()) {
            return mavenWrapper.toString();
        }

        return "mvn";
    }
}