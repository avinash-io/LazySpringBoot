package io.github.avinashio.lazyspringboot.application.project;

import io.github.avinashio.lazyspringboot.domain.project.NewProjectRequest;
import java.io.IOException;
import java.nio.file.Path;

public interface CreateSpringProjectUseCase {

    Path create(
            NewProjectRequest request,
            Path destination)
            throws IOException,
            InterruptedException;
}