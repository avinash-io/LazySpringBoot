package io.github.avinashio.lazyspringboot.ui;

import io.github.avinashio.lazyspringboot.application.action.ExecuteProjectActionUseCase;
import io.github.avinashio.lazyspringboot.application.action.ProjectActionCatalog;
import io.github.avinashio.lazyspringboot.application.dependency.ApplyDependenciesUseCase;
import io.github.avinashio.lazyspringboot.application.dependency.BuildDependencyItemsUseCase;
import io.github.avinashio.lazyspringboot.application.dependency.GetDependenciesUseCase;
import io.github.avinashio.lazyspringboot.application.dependency.UndoDependenciesUseCase;
import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.StartProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.process.StopProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;
import io.github.avinashio.lazyspringboot.application.project.RefreshSelectedProjectUseCase;
import io.github.avinashio.lazyspringboot.domain.action.ActionItem;
import io.github.avinashio.lazyspringboot.domain.action.CommandResult;
import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.domain.action.ProjectActionOutput;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.component.DependencyFilter;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectActionExecutor;
import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;
import io.github.avinashio.lazyspringboot.ui.input.KeyReader;
import io.github.avinashio.lazyspringboot.ui.input.KeyType;
import io.github.avinashio.lazyspringboot.ui.screen.ConfirmationScreen;
import io.github.avinashio.lazyspringboot.ui.screen.MainScreen;
import io.github.avinashio.lazyspringboot.ui.screen.ProjectActionOutputScreen;
import io.github.avinashio.lazyspringboot.ui.screen.ProjectActionsScreen;
import io.github.avinashio.lazyspringboot.ui.state.InputMode;
import io.github.avinashio.lazyspringboot.ui.state.PanelFocus;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import io.github.avinashio.lazyspringboot.ui.controller.ProcessController;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectActionController;
import io.github.avinashio.lazyspringboot.ui.controller.CreateProjectController;
import io.github.avinashio.lazyspringboot.ui.screen.CreateProjectScreen;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;

@Component
@ConditionalOnProperty(
        name = "lazyspringboot.tui.enabled",
        havingValue = "true",
        matchIfMissing = true)
public class TuiApplication
        implements ApplicationRunner {

    private static final long
            UI_REFRESH_INTERVAL_MILLIS = 150;

    private final Terminal terminal;
    private final KeyReader keyReader;
    private final MainScreen mainScreen;
    private final UiState uiState;

    private final DiscoverProjectsUseCase
            discoverProjectsUseCase;

    private final GetDependenciesUseCase
            getDependenciesUseCase;

    private final BuildDependencyItemsUseCase
            buildDependencyItemsUseCase;

    private final DependencyFilter dependencyFilter;

    private final ConfirmationScreen
            confirmationScreen;

    private final ApplyDependenciesUseCase
            applyDependenciesUseCase;

    private final RefreshSelectedProjectUseCase
            refreshSelectedProjectUseCase;

    private final UndoDependenciesUseCase
            undoDependenciesUseCase;

    private final ProjectActionCatalog
            projectActionCatalog;

    private final ProjectActionsScreen
            projectActionsScreen;

    private final ProjectActionOutputScreen
            projectActionOutputScreen;

    private final ExecuteProjectActionUseCase
            executeProjectActionUseCase;

    private final StartProjectProcessUseCase
            startProjectProcessUseCase;

    private final GetProjectProcessUseCase
            getProjectProcessUseCase;

    private final StopProjectProcessUseCase
            stopProjectProcessUseCase;

    private final ProcessController
            processController;

    private final ProjectActionController
            projectActionController;

    private final ProjectActionExecutor
            projectActionExecutor;

    private final CreateProjectController
            createProjectController;

    private final CreateProjectScreen
            createProjectScreen;

    private List<SpringDependency> dependencyCatalog =
            List.of();

    public TuiApplication(
            Terminal terminal,
            KeyReader keyReader,
            MainScreen mainScreen,
            UiState uiState,
            DiscoverProjectsUseCase discoverProjectsUseCase,
            GetDependenciesUseCase getDependenciesUseCase,
            BuildDependencyItemsUseCase buildDependencyItemsUseCase,
            ConfirmationScreen confirmationScreen,
            DependencyFilter dependencyFilter,
            ApplyDependenciesUseCase applyDependenciesUseCase,
            RefreshSelectedProjectUseCase refreshSelectedProjectUseCase,
            UndoDependenciesUseCase undoDependenciesUseCase,
            ProjectActionCatalog projectActionCatalog,
            ProjectActionsScreen projectActionsScreen,
            ProjectActionOutputScreen projectActionOutputScreen,
            ProcessController processController,
            ProjectActionController projectActionController,
            ProjectActionExecutor projectActionExecutor,
            CreateProjectScreen createProjectScreen,
            CreateProjectController createProjectController,
            ExecuteProjectActionUseCase executeProjectActionUseCase,
            StartProjectProcessUseCase startProjectProcessUseCase,
            GetProjectProcessUseCase getProjectProcessUseCase,
            StopProjectProcessUseCase stopProjectProcessUseCase) {
        this.terminal = terminal;
        this.keyReader = keyReader;
        this.mainScreen = mainScreen;
        this.uiState = uiState;
        this.discoverProjectsUseCase =
                discoverProjectsUseCase;
        this.getDependenciesUseCase =
                getDependenciesUseCase;
        this.buildDependencyItemsUseCase =
                buildDependencyItemsUseCase;
        this.confirmationScreen =
                confirmationScreen;
        this.dependencyFilter =
                dependencyFilter;
        this.applyDependenciesUseCase =
                applyDependenciesUseCase;
        this.refreshSelectedProjectUseCase =
                refreshSelectedProjectUseCase;
        this.undoDependenciesUseCase =
                undoDependenciesUseCase;
        this.projectActionCatalog =
                projectActionCatalog;
        this.projectActionsScreen =
                projectActionsScreen;
        this.projectActionOutputScreen =
                projectActionOutputScreen;
        this.executeProjectActionUseCase =
                executeProjectActionUseCase;
        this.startProjectProcessUseCase =
                startProjectProcessUseCase;
        this.getProjectProcessUseCase =
                getProjectProcessUseCase;
        this.stopProjectProcessUseCase =
                stopProjectProcessUseCase;
        this.processController =
                processController;
        this.projectActionController =
                projectActionController;
        this.projectActionExecutor =
                projectActionExecutor;
        this.createProjectScreen =
                createProjectScreen;
        this.createProjectController =
                createProjectController;
    }

    @Override
    public void run(
            ApplicationArguments args)
            throws Exception {
        var originalAttributes =
                terminal.getAttributes();

        try {
            terminal.enterRawMode();

            terminal.puts(
                    InfoCmp.Capability.enter_ca_mode);

            terminal.puts(
                    InfoCmp.Capability.cursor_invisible);

            terminal.flush();

            Path currentDirectory =
                    Path.of("")
                            .toAbsolutePath();

            uiState.setProjects(
                    discoverProjectsUseCase.discover(
                            currentDirectory));

            dependencyCatalog =
                    getDependenciesUseCase
                            .getDependencies();

            refreshDependencyItems();

            render();

            runEventLoop();
        } finally {
            terminal.puts(
                    InfoCmp.Capability.cursor_visible);

            terminal.puts(
                    InfoCmp.Capability.exit_ca_mode);

            terminal.setAttributes(
                    originalAttributes);

            terminal.flush();
        }
    }

    private void render() {
        if (uiState
                .dependencyConfirmationActive()) {
            confirmationScreen.render(uiState);
            return;
        }


        if (createProjectController
                .state()
                .active()) {

            createProjectScreen.render(
                    createProjectController.state());

            return;
        }

        if (uiState.projectActionOutputActive()) {

            SpringProject project =
                    uiState.selectedProject();

            if (project != null) {
                processController.refreshLogs(
                        project);
            }

            projectActionOutputScreen.render(
                    uiState);
            return;
        }

        if (uiState.projectActionsActive()) {
            projectActionsScreen.render(
                    uiState,
                    projectActionController.actions(
                            uiState.selectedProject()));
            return;
        }

        mainScreen.render(uiState);
    }

    private void runEventLoop()
            throws Exception {
        while (true) {
            KeyEvent keyEvent =
                    readNextKeyEvent();

            if (keyEvent.type()
                    == KeyType.TIMEOUT) {
                handleTimeout();
                continue;
            }

            if (shouldQuit(keyEvent)) {
                return;
            }

            handleKey(keyEvent);
            render();
        }
    }

    private KeyEvent readNextKeyEvent()
            throws IOException {

        if (shouldUseRefreshTimeout()) {
            return keyReader.read(
                    UI_REFRESH_INTERVAL_MILLIS);
        }

        return keyReader.read();
    }

    private boolean shouldUseRefreshTimeout() {

        return isLiveProcessOutputVisible()
                || isProjectStarting();
    }

    private boolean isProjectStarting() {

        SpringProject project =
                uiState.selectedProject();

        if (project == null) {
            return false;
        }

        return getProjectProcessUseCase
                .get(project)
                .map(ProjectProcess::status)
                .filter(status ->
                        status
                                == ProjectProcessStatus.STARTING)
                .isPresent();
    }

    private boolean isLiveProcessOutputVisible() {
        ProjectActionOutput output =
                uiState.projectActionOutput();

        return uiState.projectActionOutputActive()
                && output != null
                && output.action()
                == ProjectAction.VIEW_LOGS;
    }

    private void handleTimeout() {

        if (!shouldUseRefreshTimeout()) {
            return;
        }

        render();
    }

    private boolean shouldQuit(
            KeyEvent keyEvent) {
        return uiState.inputMode()
                == InputMode.NAVIGATION
                && !uiState.projectActionsActive()
                && !uiState.projectActionOutputActive()
                && keyEvent.type()
                == KeyType.QUIT;
    }

    private void handleKey(
            KeyEvent keyEvent) {
        if (uiState
                .dependencyConfirmationActive()) {
            handleDependencyConfirmationKey(
                    keyEvent);
            return;
        }

        if (createProjectController
                .state()
                .active()) {

            handleCreateProjectKey(
                    keyEvent);

            return;
        }

        if (uiState.projectActionOutputActive()) {
            handleProjectActionOutputKey(
                    keyEvent);
            return;
        }

        if (uiState.projectActionsActive()) {
            handleProjectActionsKey(
                    keyEvent);
            return;
        }

        if (uiState.dependencySearchActive()) {
            handleDependencySearchKey(
                    keyEvent);
            return;
        }

        handleNavigationKey(keyEvent);
    }

    private void handleCreateProjectKey(
            KeyEvent keyEvent) {

        CreateProjectState state =
                createProjectController.state();

        if (state.editing()) {

            switch (keyEvent.type()) {

                case ENTER -> {

                    if (state.selectedField() == 5) {

                        createProjectController.generate(
                                Path.of("")
                                        .toAbsolutePath());

                    } else {

                        state.startEditing();
                    }
                }

                case BACKSPACE ->
                        state.backspace();

                case CHARACTER -> {

                    if (keyEvent.hasCharacter()) {
                        state.append(
                                keyEvent.character());
                    }
                }

                case ESCAPE -> {

                    state.stopEditing();

                    createProjectController.close();
                }

                default -> {
                }
            }

            return;
        }

        switch (keyEvent.type()) {

            case ESCAPE ->
                    createProjectController.close();

            case DOWN ->
                    state.nextField();

            case UP ->
                    state.previousField();

            case ENTER ->
                    state.startEditing();

            default -> {
            }
        }
    }

    private void handleProjectActionOutputKey(
            KeyEvent keyEvent) {
        ProjectActionOutput output =
                uiState.projectActionOutput();

        if (output == null) {
            return;
        }

        int visibleHeight =
                projectActionOutputScreen
                        .visibleHeight();

        switch (keyEvent.type()) {
            case ESCAPE ->
                    uiState.closeProjectActionOutput();

            case UP ->
                    uiState
                            .outputViewport()
                            .scrollUp();

            case DOWN ->
                    uiState
                            .outputViewport()
                            .scrollDown(
                                    output.lines().size(),
                                    visibleHeight);

            case PAGE_UP ->
                    uiState
                            .outputViewport()
                            .pageUp(
                                    visibleHeight);

            case PAGE_DOWN ->
                    uiState
                            .outputViewport()
                            .pageDown(
                                    output.lines().size(),
                                    visibleHeight);

            case GO_TO_TOP ->
                    uiState
                            .outputViewport()
                            .moveToTop();

            case GO_TO_BOTTOM ->
                    uiState
                            .outputViewport()
                            .moveToBottom(
                                    output.lines().size(),
                                    visibleHeight);

            default -> {
                // No action.
            }
        }
    }

    private void handleDependencyConfirmationKey(
            KeyEvent keyEvent) {
        switch (keyEvent.type()) {
            case ESCAPE ->
                    uiState.stopDependencyConfirmation();

            case ENTER ->
                    handleDependencyApply();

            default -> {
                // No action.
            }
        }
    }

    private void handleDependencyApply() {
        SpringProject project =
                uiState.selectedProject();

        int dependencyCount =
                uiState
                        .selectedDependencyItems()
                        .size();

        try {
            applyDependenciesUseCase.apply(
                    project,
                    uiState.selectedDependencyItems());

            refreshSelectedProject();

            uiState.clearDependencySelections();

            uiState.stopDependencyConfirmation();

            uiState.showSuccessMessage(
                    buildDependencyApplySuccessMessage(
                            dependencyCount,
                            project.name()));
        } catch (IOException exception) {
            handleDependencyApplyFailure(exception);
        }
    }

    private void handleDependencyApplyFailure(
            IOException exception) {
        uiState.stopDependencyConfirmation();

        uiState.showErrorMessage(
                buildDependencyApplyErrorMessage(
                        exception));
    }

    private String
    buildDependencyApplySuccessMessage(
            int dependencyCount,
            String projectName) {
        String dependencyLabel =
                dependencyCount == 1
                        ? "dependency"
                        : "dependencies";

        return "Added "
                + dependencyCount
                + " "
                + dependencyLabel
                + " to "
                + projectName;
    }

    private String buildDependencyApplyErrorMessage(
            IOException exception) {
        String message =
                exception.getMessage();

        if (message == null
                || message.isBlank()) {
            return "Failed to apply dependencies";
        }

        return "Failed to apply dependencies: "
                + message;
    }

    private void handleProjectActionsKey(
            KeyEvent keyEvent) {

        List<ActionItem> actions =
                projectActionController.actions(
                        uiState.selectedProject());

        if (projectActionController.handleKey(
                keyEvent,
                actions)) {
            return;
        }

        if (keyEvent.type()
                == KeyType.ENTER) {
            executeSelectedProjectAction(actions);
        }
    }

    private void executeSelectedProjectAction(
            List<ActionItem> actions) {

        if (actions.isEmpty()) {
            return;
        }

        int selectedIndex =
                uiState.selectedProjectActionIndex();

        if (selectedIndex < 0
                || selectedIndex >= actions.size()) {
            return;
        }

        ActionItem selectedAction =
                actions.get(selectedIndex);

        if (!projectActionExecutor.canExecute(
                selectedAction)) {
            return;
        }

        SpringProject project =
                uiState.selectedProject();

        if (project == null) {
            uiState.stopProjectActions();
            uiState.showErrorMessage(
                    "No project selected");
            return;
        }

        projectActionExecutor.execute(
                project,
                selectedAction);
    }

    private void executeBlockingProjectAction(
            SpringProject project,
            ActionItem selectedAction) {
        try {
            CommandResult result =
                    executeProjectActionUseCase.execute(
                            project,
                            selectedAction.action());

            uiState.stopProjectActions();

            ProjectActionOutput output =
                    new ProjectActionOutput(
                            project.name(),
                            selectedAction.action(),
                            result.exitCode(),
                            result.output());

            uiState.showProjectActionOutput(
                    output,
                    projectActionOutputScreen
                            .visibleHeight());
        } catch (IOException exception) {
            uiState.stopProjectActions();

            uiState.showErrorMessage(
                    buildProjectActionErrorMessage(
                            selectedAction,
                            exception));
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();

            uiState.stopProjectActions();

            uiState.showErrorMessage(
                    selectedAction
                            .action()
                            .displayName()
                            + " interrupted for "
                            + project.name());
        }
    }

    private void showProjectProcessOutput(
            SpringProject project) {
        Optional<ProjectProcess> process =
                getProjectProcessUseCase.get(
                        project);

        if (process.isEmpty()) {
            uiState.stopProjectActions();

            uiState.showErrorMessage(
                    "No process found for "
                            + project.name());

            return;
        }

        uiState.stopProjectActions();

        showProcessOutput(
                process.get());
    }

    private void showProcessOutput(
            ProjectProcess process) {
        int exitCode =
                process.exitCode() == null
                        ? -1
                        : process.exitCode();

        ProjectActionOutput output =
                new ProjectActionOutput(
                        process.projectName(),
                        ProjectAction.VIEW_LOGS,
                        exitCode,
                        process.output());

        uiState.showProjectActionOutput(
                output,
                projectActionOutputScreen
                        .visibleHeight());
    }

    private void refreshProcessOutput() {
        ProjectActionOutput output =
                uiState.projectActionOutput();

        SpringProject project =
                uiState.selectedProject();

        if (output == null
                || project == null
                || output.action()
                != ProjectAction.VIEW_LOGS) {
            return;
        }

        getProjectProcessUseCase
                .get(project)
                .ifPresent(
                        this::replaceProcessOutput);
    }

    private void replaceProcessOutput(
            ProjectProcess process) {
        int visibleHeight =
                projectActionOutputScreen
                        .visibleHeight();

        int previousContentSize =
                uiState
                        .projectActionOutput()
                        .lines()
                        .size();

        int maximumPreviousOffset =
                Math.max(
                        0,
                        previousContentSize
                                - visibleHeight);

        boolean followingOutput =
                uiState
                        .outputViewport()
                        .offset()
                        >= maximumPreviousOffset;

        int exitCode =
                process.exitCode() == null
                        ? -1
                        : process.exitCode();

        ProjectActionOutput output =
                new ProjectActionOutput(
                        process.projectName(),
                        ProjectAction.VIEW_LOGS,
                        exitCode,
                        process.output());

        uiState.replaceProjectActionOutput(
                output);

        if (followingOutput) {
            uiState
                    .outputViewport()
                    .moveToBottom(
                            output.lines().size(),
                            visibleHeight);
        }
    }

    private String buildProcessErrorMessage(
            String prefix,
            IOException exception) {
        String message =
                exception.getMessage();

        if (message == null
                || message.isBlank()) {
            return prefix;
        }

        return prefix
                + ": "
                + message;
    }

    private String buildProjectActionErrorMessage(
            ActionItem actionItem,
            IOException exception) {
        String actionName =
                actionItem
                        .action()
                        .displayName();

        String message =
                exception.getMessage();

        if (message == null
                || message.isBlank()) {
            return actionName + " failed";
        }

        return actionName
                + " failed: "
                + message;
    }

    private void handleEnter() {
        if (uiState.panelFocus()
                != PanelFocus.DEPENDENCIES) {
            return;
        }

        uiState.startDependencyConfirmation();
    }

    private void handleNavigationKey(
            KeyEvent keyEvent) {
        uiState.clearMessage();

        switch (keyEvent.type()) {
            case LEFT ->
                    uiState.focusPreviousPanel();

            case RIGHT ->
                    uiState.focusNextPanel();

            case UP ->
                    handleUp();

            case DOWN ->
                    handleDown();

            case SPACE ->
                    handleSpace();

            case SEARCH ->
                    handleSearch();

            case ENTER ->
                    handleEnter();

            case UNDO ->
                    handleUndo();

            case ACTIONS ->
                    projectActionController.openActions(
                            uiState.selectedProject());

            case CHARACTER -> {

                if (!keyEvent.hasCharacter()) {
                    break;
                }

                switch (keyEvent.character()) {

                    case 'n' ->
                            createProjectController.open();

                    default -> {
                        // No action.
                    }
                }
            }

            default -> {
                // No action.
            }
        }
    }

    private void handleUndo() {
        SpringProject project =
                uiState.selectedProject();

        if (!undoDependenciesUseCase.canUndo(
                project)) {
            uiState.showErrorMessage(
                    "No dependency changes to undo");

            return;
        }

        try {
            undoDependenciesUseCase.undo(project);

            refreshSelectedProject();

            uiState.clearDependencySelections();

            uiState.showSuccessMessage(
                    "Restored pom.xml for "
                            + project.name());
        } catch (IOException exception) {
            uiState.showErrorMessage(
                    buildDependencyUndoErrorMessage(
                            exception));
        }
    }

    private String buildDependencyUndoErrorMessage(
            IOException exception) {
        String message =
                exception.getMessage();

        if (message == null
                || message.isBlank()) {
            return "Failed to restore pom.xml";
        }

        return "Failed to restore pom.xml: "
                + message;
    }

    private void handleDependencySearchKey(
            KeyEvent keyEvent) {
        switch (keyEvent.type()) {
            case ESCAPE ->
                    stopDependencySearch();

            case BACKSPACE -> {
                uiState
                        .removeLastDependencySearchCharacter();

                selectFirstVisibleDependency();
            }

            case QUIT ->
                    appendDependencySearchCharacter('q');

            case UNDO ->
                    appendDependencySearchCharacter('u');

            case ACTIONS ->
                    appendDependencySearchCharacter('a');

            case GO_TO_TOP ->
                    appendDependencySearchCharacter('g');

            case GO_TO_BOTTOM ->
                    appendDependencySearchCharacter('G');

            case SEARCH ->
                    appendDependencySearchCharacter('/');

            case SPACE ->
                    appendDependencySearchCharacter(' ');

            case UP ->
                    selectPreviousVisibleDependency();

            case DOWN ->
                    selectNextVisibleDependency();

            default -> {
                // No action.
            }
        }
    }

    private void appendDependencySearchCharacter(
            char character) {
        uiState.appendDependencySearchCharacter(
                character);

        selectFirstVisibleDependency();
    }

    private void handleSearch() {
        if (uiState.panelFocus()
                != PanelFocus.DEPENDENCIES) {
            return;
        }

        uiState.startDependencySearch();

        selectFirstVisibleDependency();
    }

    private void stopDependencySearch() {
        uiState.stopDependencySearch();
    }

    private void handleUp() {
        switch (uiState.panelFocus()) {
            case PROJECTS -> {
                int previousIndex =
                        uiState.selectedProjectIndex();

                uiState.selectPreviousProject();

                if (previousIndex
                        != uiState.selectedProjectIndex()) {
                    refreshDependencyItems();
                }
            }

            case DEPENDENCIES ->
                    selectPreviousVisibleDependency();

            case PROJECT_DETAILS -> {
                // No action.
            }
        }
    }

    private void handleDown() {
        switch (uiState.panelFocus()) {
            case PROJECTS -> {
                int previousIndex =
                        uiState.selectedProjectIndex();

                uiState.selectNextProject();

                if (previousIndex
                        != uiState.selectedProjectIndex()) {
                    refreshDependencyItems();
                }
            }

            case DEPENDENCIES ->
                    selectNextVisibleDependency();

            case PROJECT_DETAILS -> {
                // No action.
            }
        }
    }

    private void handleSpace() {
        if (uiState.panelFocus()
                == PanelFocus.DEPENDENCIES) {
            uiState.toggleSelectedDependency();
        }
    }

    private void selectNextVisibleDependency() {
        List<Integer> indexes =
                visibleDependencyIndexes();

        int currentPosition =
                indexes.indexOf(
                        uiState.selectedDependencyIndex());

        if (currentPosition < 0) {
            selectFirstVisibleDependency(indexes);
            return;
        }

        if (currentPosition
                < indexes.size() - 1) {
            uiState.selectDependency(
                    indexes.get(currentPosition + 1));
        }
    }

    private void selectPreviousVisibleDependency() {
        List<Integer> indexes =
                visibleDependencyIndexes();

        int currentPosition =
                indexes.indexOf(
                        uiState.selectedDependencyIndex());

        if (currentPosition < 0) {
            selectFirstVisibleDependency(indexes);
            return;
        }

        if (currentPosition > 0) {
            uiState.selectDependency(
                    indexes.get(currentPosition - 1));
        }
    }

    private void selectFirstVisibleDependency() {
        selectFirstVisibleDependency(
                visibleDependencyIndexes());
    }

    private void selectFirstVisibleDependency(
            List<Integer> indexes) {
        if (indexes.isEmpty()) {
            return;
        }

        uiState.selectDependency(
                indexes.getFirst());
    }

    private List<Integer>
    visibleDependencyIndexes() {
        return dependencyFilter.matchingIndexes(
                uiState.dependencyItems(),
                uiState.dependencySearchQuery());
    }

    private void refreshSelectedProject()
            throws IOException {
        SpringProject refreshedProject =
                refreshSelectedProjectUseCase.refresh(
                        uiState.selectedProject());

        uiState.replaceSelectedProject(
                refreshedProject);

        refreshDependencyItems();
    }

    private void refreshDependencyItems() {
        uiState.setDependencyItems(
                buildDependencyItemsUseCase.build(
                        dependencyCatalog,
                        uiState.selectedProject()));
    }
}