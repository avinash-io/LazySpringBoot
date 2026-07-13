package io.github.avinashio.lazyspringboot.ui;

import io.github.avinashio.lazyspringboot.application.project.DiscoverProjectsUseCase;
import io.github.avinashio.lazyspringboot.ui.input.Key;
import io.github.avinashio.lazyspringboot.ui.input.KeyReader;
import io.github.avinashio.lazyspringboot.ui.screen.MainScreen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.nio.file.Path;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import io.github.avinashio.lazyspringboot.ui.state.PanelFocus;
import io.github.avinashio.lazyspringboot.application.dependency.GetDependenciesUseCase;

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

    public TuiApplication(
            Terminal terminal,
            KeyReader keyReader,
            MainScreen mainScreen,
            UiState uiState,
            DiscoverProjectsUseCase discoverProjectsUseCase,
            GetDependenciesUseCase getDependenciesUseCase) {
        this.terminal = terminal;
        this.keyReader = keyReader;
        this.mainScreen = mainScreen;
        this.uiState = uiState;
        this.discoverProjectsUseCase = discoverProjectsUseCase;
        this.getDependenciesUseCase = getDependenciesUseCase;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var originalAttributes = terminal.getAttributes();

        try {
            terminal.enterRawMode();
            terminal.puts(InfoCmp.Capability.enter_ca_mode);
            terminal.puts(InfoCmp.Capability.cursor_invisible);
            terminal.flush();

            Path currentDirectory = Path.of("").toAbsolutePath();

            uiState.setProjects(
                    discoverProjectsUseCase.discover(currentDirectory));

            uiState.setDependencies(
                    getDependenciesUseCase.getDependencies());

            mainScreen.render(uiState);

            runEventLoop();
        } finally {
            terminal.puts(InfoCmp.Capability.cursor_visible);
            terminal.puts(InfoCmp.Capability.exit_ca_mode);
            terminal.setAttributes(originalAttributes);
            terminal.flush();
        }
    }

    private void runEventLoop() throws Exception {
        while (true) {
            Key key = keyReader.read();

            if (key == Key.QUIT) {
                return;
            }

            handleKey(key);
            mainScreen.render(uiState);
        }
    }

    private void handleKey(Key key) {
        switch (key) {
            case LEFT -> uiState.focusPreviousPanel();
            case RIGHT -> uiState.focusNextPanel();
            case UP -> handleUp();
            case DOWN -> handleDown();
            default -> {
                // No action.
            }
        }
    }

    private void handleUp() {
        switch (uiState.panelFocus()) {
            case PROJECTS ->
                    uiState.selectPreviousProject();
            case DEPENDENCIES ->
                    uiState.selectPreviousDependency();
            case PROJECT_DETAILS -> {
                // No action.
            }
        }
    }

    private void handleDown() {
        switch (uiState.panelFocus()) {
            case PROJECTS ->
                    uiState.selectNextProject();
            case DEPENDENCIES ->
                    uiState.selectNextDependency();
            case PROJECT_DETAILS -> {
                // No action.
            }
        }
    }
}