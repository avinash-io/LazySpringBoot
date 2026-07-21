package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.ui.controller.TextInputController;
import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;
import io.github.avinashio.lazyspringboot.ui.state.UiMessage;
import io.github.avinashio.lazyspringboot.ui.state.UiMessageType;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.springframework.stereotype.Component;

@Component
public class StatusBar {

    private final TextInputController textInputController;

    public StatusBar(
            TextInputController textInputController) {

        this.textInputController =
                textInputController;
    }

    public String render(
            UiState state) {

        if (state.hasMessage()) {
            return renderMessage(
                    state.message());
        }

        if (textInputController.active(
                TextInputPurpose.PROJECT_SEARCH)) {

            return renderProjectSearch();
        }

        if (textInputController.active(
                TextInputPurpose.DEPENDENCY_SEARCH)) {
            return renderDependencySearch();
        }

        return switch (state.panelFocus()) {

            case PROJECTS ->
                    " ↑↓ Navigate"
                            + "    Enter Start"
                            + "    x Stop"
                            + "    R Restart"
                            + "    l Logs"
                            + "    / Search"
                            + "    s Sort"
                            + "    a Actions"
                            + "    n Create"
                            + "    w Workspace"
                            + "    r Refresh"
                            + "    u Undo"
                            + "    Ctrl+P Commands"
                            + "    ←→ Panel"
                            + "    q Quit";

            case DEPENDENCIES ->
                    " ↑↓ Navigate"
                            + "    Space Toggle"
                            + "    / Search"
                            + "    Enter Apply"
                            + "    a Project Actions"
                            + "    c Create"
                            + "    w Workspace"
                            + "    r Refresh"
                            + "    u Undo"
                            + "    Ctrl+P Command Palette"
                            + "    ←→ Switch Panel"
                            + "    q Quit";

            case PROJECT_DETAILS ->
                    " a Project Actions"
                            + "    c Create"
                            + "    w Workspace"
                            + "    r Refresh"
                            + "    u Undo"
                            + "    Ctrl+P Command Palette"
                            + "    ←→ Switch Panel"
                            + "    q Quit";
        };
    }

    private String renderProjectSearch() {

        return " Search Projects: "
                + textInputController.value()
                + "_"
                + "    ↑↓ Navigate"
                + "    Enter Select"
                + "    Backspace Delete"
                + "    Esc Close";
    }

    private String renderMessage(
            UiMessage message) {

        String marker =
                switch (message.type()) {

                    case SUCCESS -> "✓";

                    case WARNING -> "⚠";

                    case ERROR -> "✗";
                };

        return " "
                + marker
                + " "
                + message.text();
    }

    private String renderDependencySearch() {

        return " Search: "
                + textInputController.value()
                + "_"
                + "    ↑↓ Navigate"
                + "    Backspace Delete"
                + "    Enter Apply"
                + "    Esc Close";
    }
}