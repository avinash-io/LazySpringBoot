package io.github.avinashio.lazyspringboot.infrastructure.process;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import jakarta.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;


@Component
public class ProjectProcessManager {

    private final ProjectProcessCommandFactory
            commandFactory;

    private final Map<String, ManagedProjectProcess>
            processes =
            new ConcurrentHashMap<>();

    private final ExecutorService executorService =
            Executors.newCachedThreadPool();

    private static final long PROCESS_STOP_TIMEOUT_SECONDS =
            10;

    public ProjectProcessManager(
            ProjectProcessCommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public ProjectProcess start(
            SpringProject project)
            throws IOException {
        String projectKey =
                projectKey(project);

        ManagedProjectProcess existingProcess =
                processes.get(projectKey);

        if (existingProcess != null
                && existingProcess
                .process()
                .isAlive()) {
            return existingProcess.snapshot();
        }

        ProcessBuilder processBuilder =
                new ProcessBuilder(
                        commandFactory.create(project));

        processBuilder.directory(
                project.path().toFile());

        processBuilder.redirectErrorStream(true);

        Process process =
                processBuilder.start();

        ManagedProjectProcess managedProcess =
                new ManagedProjectProcess(
                        project.name(),
                        process);

        processes.put(
                projectKey,
                managedProcess);

        executorService.submit(
                () ->
                        monitorProcess(
                                managedProcess));

        return managedProcess.snapshot();
    }

    public ProjectProcess restart(
            SpringProject project)
            throws IOException {

        ManagedProjectProcess managedProcess =
                processes.get(
                        projectKey(project));

        if (managedProcess != null
                && managedProcess
                .process()
                .isAlive()) {

            stopAndWait(
                    managedProcess);
        }

        return start(
                project);
    }

    private void stopAndWait(
            ManagedProjectProcess managedProcess)
            throws IOException {

        Process process =
                managedProcess.process();

        managedProcess.requestStop();

        process.destroy();

        try {

            boolean stopped =
                    process.waitFor(
                            PROCESS_STOP_TIMEOUT_SECONDS,
                            TimeUnit.SECONDS);

            if (stopped) {
                return;
            }

            managedProcess.addOutput(
                    "Process did not stop gracefully; "
                            + "forcing termination");

            process.destroyForcibly();

            process.waitFor();

        } catch (InterruptedException exception) {

            Thread.currentThread()
                    .interrupt();

            throw new IOException(
                    "Interrupted while waiting "
                            + "for project process to stop",
                    exception);
        }
    }

    public Optional<ProjectProcess> find(
            SpringProject project) {
        ManagedProjectProcess managedProcess =
                processes.get(
                        projectKey(project));

        if (managedProcess == null) {
            return Optional.empty();
        }

        return Optional.of(
                managedProcess.snapshot());
    }

    public boolean stop(
            SpringProject project) {
        ManagedProjectProcess managedProcess =
                processes.get(
                        projectKey(project));

        if (managedProcess == null) {
            return false;
        }

        Process process =
                managedProcess.process();

        if (!process.isAlive()) {
            return false;
        }

        managedProcess.requestStop();

        process.destroy();

        return true;
    }

    private void monitorProcess(
            ManagedProjectProcess managedProcess) {

        Process process =
                managedProcess.process();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     process.getInputStream(),
                                     StandardCharsets.UTF_8))) {

            readProcessOutput(
                    reader,
                    managedProcess);

            int exitCode =
                    process.waitFor();

            updateProcessStatus(
                    managedProcess,
                    exitCode);

        } catch (IOException exception) {

            managedProcess.addOutput(
                    "Process output error: "
                            + exception.getMessage());

            managedProcess.markFailed(-1);

        } catch (InterruptedException exception) {

            Thread.currentThread().interrupt();

            managedProcess.addOutput(
                    "Process monitoring interrupted");

            managedProcess.markFailed(-1);
        }
    }

    private void readProcessOutput(
            BufferedReader reader,
            ManagedProjectProcess managedProcess)
            throws IOException {
        String line;

        while ((line = reader.readLine())
                != null) {
            managedProcess.addOutput(line);
        }
    }

    private void updateProcessStatus(
            ManagedProjectProcess managedProcess,
            int exitCode) {
        if (managedProcessStoppedNormally(
                managedProcess,
                exitCode)) {
            managedProcess.markStopped(
                    exitCode);

            return;
        }

        managedProcess.markFailed(
                exitCode);
    }

    private boolean managedProcessStoppedNormally(
            ManagedProjectProcess managedProcess,
            int exitCode) {
        return managedProcess.stopRequested()
                || exitCode == 0;
    }

    private String projectKey(
            SpringProject project) {
        return project
                .path()
                .toAbsolutePath()
                .normalize()
                .toString();
    }

    @PreDestroy
    void stopAllProcesses() {
        processes.values()
                .stream()
                .filter(
                        managedProcess ->
                                managedProcess
                                        .process()
                                        .isAlive())
                .forEach(
                        this::stopManagedProcess);

        executorService.shutdownNow();
    }

    private void stopManagedProcess(
            ManagedProjectProcess managedProcess) {
        managedProcess.requestStop();

        managedProcess
                .process()
                .destroy();
    }
}