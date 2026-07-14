package io.github.avinashio.lazyspringboot.ui;

import io.github.avinashio.lazyspringboot.application.dependency.BuildDependencyItemsUseCase;
import io.github.avinashio.lazyspringboot.application.dependency.GetDependenciesUseCase;
import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;
import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.ui.component.DependencyFilter;
import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;
import io.github.avinashio.lazyspringboot.ui.input.KeyReader;
import io.github.avinashio.lazyspringboot.ui.input.KeyType;
import io.github.avinashio.lazyspringboot.ui.screen.ConfirmationScreen;
import io.github.avinashio.lazyspringboot.ui.screen.MainScreen;
import io.github.avinashio.lazyspringboot.ui.state.InputMode;
import io.github.avinashio.lazyspringboot.ui.state.PanelFocus;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.nio.file.Path;
import java.util.List;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "lazyspringboot.tui.enabled",
        havingValue = "true",
        matchIfMissing = true)
public class TuiApplication implements ApplicationRunner {

    private final Terminal terminal;
    private final KeyReader keyReader;
    private final MainScreen mainScreen;
    private final UiState uiState;
    private final DiscoverProjectsUseCase discoverProjectsUseCase;
    private final GetDependenciesUseCase getDependenciesUseCase;
    private final BuildDependencyItemsUseCase
            buildDependencyItemsUseCase;
    private final DependencyFilter dependencyFilter;
    private final ConfirmationScreen confirmationScreen;

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
            DependencyFilter dependencyFilter) {
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
        this.dependencyFilter = dependencyFilter;
        this.confirmationScreen = confirmationScreen;
    }

    @Override
    public void run(ApplicationArguments args)
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
                    Path.of("").toAbsolutePath();

            uiState.setProjects(
                    discoverProjectsUseCase.discover(
                            currentDirectory));

            dependencyCatalog =
                    getDependenciesUseCase.getDependencies();

            refreshDependencyItems();

            render();

            runEventLoop();
        } finally {
            terminal.puts(
                    InfoCmp.Capability.cursor_visible);
            terminal.puts(
                    InfoCmp.Capability.exit_ca_mode);
            terminal.setAttributes(originalAttributes);
            terminal.flush();
        }
    }

    private void render() {
        if (uiState.dependencyConfirmationActive()) {
            confirmationScreen.render(uiState);
            return;
        }

        mainScreen.render(uiState);
    }

    private void runEventLoop()
            throws Exception {
        while (true) {
            KeyEvent keyEvent =
                    keyReader.read();

            if (shouldQuit(keyEvent)) {
                return;
            }

            handleKey(keyEvent);
            render();
        }
    }

    private boolean shouldQuit(
            KeyEvent keyEvent) {
        return uiState.inputMode()
                == InputMode.NAVIGATION
                && keyEvent.type()
                == KeyType.QUIT;
    }

    private void handleKey(
            KeyEvent keyEvent) {
        if (uiState.dependencyConfirmationActive()) {
            handleDependencyConfirmationKey(
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
        uiState.stopDependencyConfirmation();
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

            default -> {
                // No action.
            }
        }
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

            case CHARACTER -> {
                if (keyEvent.hasCharacter()) {
                    uiState
                            .appendDependencySearchCharacter(
                                    keyEvent.character());

                    selectFirstVisibleDependency();
                }
            }

            case QUIT -> {
                uiState
                        .appendDependencySearchCharacter('q');

                selectFirstVisibleDependency();
            }

            case SEARCH -> {
                uiState
                        .appendDependencySearchCharacter('/');

                selectFirstVisibleDependency();
            }

            case SPACE -> {
                uiState
                        .appendDependencySearchCharacter(' ');

                selectFirstVisibleDependency();
            }

            case UP ->
                    selectPreviousVisibleDependency();

            case DOWN ->
                    selectNextVisibleDependency();

            default -> {
                // No action.
            }
        }
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

    private void refreshDependencyItems() {
        uiState.setDependencyItems(
                buildDependencyItemsUseCase.build(
                        dependencyCatalog,
                        uiState.selectedProject()));
    }
}