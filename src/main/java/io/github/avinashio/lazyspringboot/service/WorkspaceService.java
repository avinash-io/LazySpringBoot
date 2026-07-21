package io.github.avinashio.lazyspringboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.avinashio.lazyspringboot.config.WorkspaceConfiguration;
import io.github.avinashio.lazyspringboot.ui.state.ProjectSortMode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceService {

    private static final String CONFIG_DIRECTORY =
            ".lazyspringboot";

    private static final String CONFIG_FILE =
            "config.json";

    private final ObjectMapper objectMapper;

    private final Path configurationDirectory;

    private final Path configurationFile;

    private WorkspaceConfiguration configuration;

    public WorkspaceService(
            ObjectMapper objectMapper)
            throws IOException {

        this.objectMapper =
                objectMapper;

        this.configurationDirectory =
                Path.of(
                        System.getProperty(
                                "user.home"),
                        CONFIG_DIRECTORY);

        this.configurationFile =
                configurationDirectory.resolve(
                        CONFIG_FILE);

        initialize();
    }

    public Path workspace() {

        return Path.of(
                configuration.workspace());
    }

    public ProjectSortMode projectSortMode() {

        ProjectSortMode projectSortMode =
                configuration.projectSortMode();

        if (projectSortMode == null) {
            return ProjectSortMode.NAME_ASC;
        }

        return projectSortMode;
    }

    public void changeWorkspace(
            Path workspace)
            throws IOException {

        configuration =
                new WorkspaceConfiguration(
                        workspace.toAbsolutePath()
                                .normalize()
                                .toString(),
                        projectSortMode());

        save();
    }

    public void changeProjectSortMode(
            ProjectSortMode projectSortMode)
            throws IOException {

        configuration =
                new WorkspaceConfiguration(
                        configuration.workspace(),
                        projectSortMode);

        save();
    }

    private void initialize()
            throws IOException {

        Files.createDirectories(
                configurationDirectory);

        if (Files.exists(
                configurationFile)) {

            configuration =
                    objectMapper.readValue(
                            configurationFile.toFile(),
                            WorkspaceConfiguration.class);

            normalizeConfiguration();

            return;
        }

        configuration =
                WorkspaceConfiguration
                        .defaultConfiguration(
                                Path.of("")
                                        .toAbsolutePath()
                                        .normalize()
                                        .toString());

        save();
    }

    private void normalizeConfiguration()
            throws IOException {

        if (configuration.projectSortMode()
                != null) {

            return;
        }

        configuration =
                new WorkspaceConfiguration(
                        configuration.workspace(),
                        ProjectSortMode.NAME_ASC);

        save();
    }

    private void save()
            throws IOException {

        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(
                        configurationFile.toFile(),
                        configuration);
    }
}