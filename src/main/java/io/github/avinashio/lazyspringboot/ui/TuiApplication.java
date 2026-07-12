package io.github.avinashio.lazyspringboot.ui;

import io.github.avinashio.lazyspringboot.ui.input.Key;
import io.github.avinashio.lazyspringboot.ui.input.KeyReader;
import io.github.avinashio.lazyspringboot.ui.screen.MainScreen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.jline.terminal.Terminal;
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

    public TuiApplication(
            Terminal terminal,
            KeyReader keyReader,
            MainScreen mainScreen,
            UiState uiState) {
        this.terminal = terminal;
        this.keyReader = keyReader;
        this.mainScreen = mainScreen;
        this.uiState = uiState;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        terminal.enterRawMode();

        mainScreen.render(uiState);

        while (true) {
            Key key = keyReader.read();

            if (key == Key.QUIT) {
                break;
            }

            handleKey(key);
            mainScreen.render(uiState);
        }
    }

    private void handleKey(Key key) {
        switch (key) {
            case UP -> uiState.selectPreviousProject();
            case DOWN -> uiState.selectNextProject();
            default -> {
                // No action.
            }
        }
    }
}