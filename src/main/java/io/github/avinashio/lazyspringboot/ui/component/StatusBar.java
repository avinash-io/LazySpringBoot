package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.ui.state.UiMessage;
import io.github.avinashio.lazyspringboot.ui.state.UiMessageType;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.springframework.stereotype.Component;

@Component
public class StatusBar {

    public String render(
            UiState state) {

        if (state.hasMessage()) {
            return renderMessage(
                    state.message());
        }

        if (state.dependencySearchActive()) {
            return renderDependencySearch(
                    state);
        }

        return switch (state.panelFocus()) {

            case PROJECTS ->
                    " ↑↓ Navigate"
                            + "    a Project Actions"
                            + "    r Refresh"
                            + "    u Undo"
                            + "    Ctrl+P Commands"
                            + "    ←→ Switch Panel"
                            + "    q Quit";

            case DEPENDENCIES ->
                    " ↑↓ Navigate"
                            + "    Space Toggle"
                            + "    / Search"
                            + "    Enter Apply"
                            + "    a Project Actions"
                            + "    r Refresh"
                            + "    u Undo"
                            + "    Ctrl+P Commands"
                            + "    ←→ Switch Panel"
                            + "    q Quit";

            case PROJECT_DETAILS ->
                    " a Project Actions"
                            + "    r Refresh"
                            + "    Ctrl+P Commands"
                            + "    ←→ Switch Panel"
                            + "    q Quit";
        };
    }

    private String renderMessage(
            UiMessage message) {

        String marker =
                message.type()
                        == UiMessageType.SUCCESS
                        ? "✓"
                        : "✗";

        return " "
                + marker
                + " "
                + message.text();
    }

    private String renderDependencySearch(
            UiState state) {

        return " Search: "
                + state.dependencySearchQuery()
                + "_"
                + "    ↑↓ Navigate"
                + "    Backspace Delete"
                + "    Esc Close";
    }
}