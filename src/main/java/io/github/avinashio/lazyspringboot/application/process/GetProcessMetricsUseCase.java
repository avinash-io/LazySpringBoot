package io.github.avinashio.lazyspringboot.application.process;

import io.github.avinashio.lazyspringboot.domain.process.ProcessMetrics;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.util.Optional;

public interface GetProcessMetricsUseCase {

    Optional<ProcessMetrics> get(
            SpringProject project);
}