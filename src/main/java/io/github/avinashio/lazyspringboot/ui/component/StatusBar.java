package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.springframework.stereotype.Component;

@Component
public class StatusBar {

    public String render(UiState state) {
        return switch (state.panelFocus()) {
            case PROJECTS ->
                    " ↑↓ Navigate    ←→ Switch Panel    q Quit";
            case DEPENDENCIES ->
                    " ↑↓ Navigate    ←→ Switch Panel    q Quit";
            case PROJECT_DETAILS ->
                    " ←→ Switch Panel    q Quit";
        };
    }
}