package io.github.avinashio.lazyspringboot.ui.command;

import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;
import io.github.avinashio.lazyspringboot.ui.state.CommandPaletteState;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CommandPaletteController {

    private final CommandCatalog commandCatalog;

    private final CommandPaletteState state;

    public CommandPaletteController(
            CommandCatalog commandCatalog,
            CommandPaletteState state) {

        this.commandCatalog = commandCatalog;
        this.state = state;
    }

    public void open() {
        state.openPalette();
    }

    public void close() {
        state.closePalette();
    }

    public boolean active() {
        return state.open();
    }

    public CommandPaletteState state() {
        return state;
    }

    public List<Command> commands() {
        return commandCatalog.commands();
    }

    public Command selectedCommand() {

        return commands().get(
                state.selectedCommandIndex());
    }

    public boolean handleKey(
            KeyEvent keyEvent) {

        switch (keyEvent.type()) {

            case UP ->
                    state.selectPrevious();

            case DOWN ->
                    state.selectNext(
                            commands().size());

            case ESCAPE -> {

                close();

                return true;
            }

            default -> {
            }
        }

        return false;
    }
}