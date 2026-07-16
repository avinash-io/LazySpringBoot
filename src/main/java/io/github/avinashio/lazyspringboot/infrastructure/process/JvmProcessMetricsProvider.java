package io.github.avinashio.lazyspringboot.infrastructure.process;

import io.github.avinashio.lazyspringboot.application.process.GetProcessMetricsUseCase;
import io.github.avinashio.lazyspringboot.domain.process.ProcessMetrics;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class JvmProcessMetricsProvider
        implements GetProcessMetricsUseCase {

    private final ProjectProcessManager
            processManager;

    public JvmProcessMetricsProvider(
            ProjectProcessManager processManager) {
        this.processManager =
                processManager;
    }

    @Override
    public Optional<ProcessMetrics> get(
            SpringProject project) {

        return processManager
                .find(project)
                .filter(process ->
                        process.running())
                .map(process ->
                        new ProcessMetrics(
                                -1L,
                                -1.0,
                                -1));
    }
}