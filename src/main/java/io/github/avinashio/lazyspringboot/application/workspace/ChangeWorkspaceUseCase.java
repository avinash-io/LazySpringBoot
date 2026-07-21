package io.github.avinashio.lazyspringboot.application.workspace;

import io.github.avinashio.lazyspringboot.service.WorkspaceService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Component;

@Component
public class ChangeWorkspaceUseCase {

    private final WorkspaceService workspaceService;

    public ChangeWorkspaceUseCase(
            WorkspaceService workspaceService) {

        this.workspaceService =
                workspaceService;
    }

    public void changeWorkspace(
            String workspace)
            throws IOException {

        try {

            Path path =
                    Path.of(workspace)
                            .toAbsolutePath()
                            .normalize();

            validate(path);

            workspaceService.changeWorkspace(path);

        } catch (IOException exception) {

            throw exception;

        } catch (Exception exception) {

            throw new IOException(
                    "Unable to access workspace.",
                    exception);
        }
    }

    private void validate(
            Path workspace)
            throws IOException {

        if (!Files.exists(workspace)) {
            throw new IOException(
                    "Directory does not exist.");
        }

        if (!Files.isDirectory(workspace)) {
            throw new IOException(
                    "Path is not a directory.");
        }
    }
}