package io.github.avinashio.lazyspringboot.ui.command;

import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;
import io.github.avinashio.lazyspringboot.ui.state.CommandPaletteState;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CommandPaletteController {

    private final CommandCatalog commandCatalog;

    private final CommandPaletteState state;

    private final CommandPaletteExecutor commandPaletteExecutor;

    public CommandPaletteController(
            CommandCatalog commandCatalog,
            CommandPaletteState state,
            CommandPaletteExecutor commandPaletteExecutor) {

        this.commandCatalog =
                commandCatalog;

        this.state =
                state;

        this.commandPaletteExecutor =
                commandPaletteExecutor;
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

        String query =
                state.searchQuery()
                        .trim()
                        .toLowerCase();

        if (query.isEmpty()) {
            return commandCatalog.commands();
        }

        return commandCatalog
                .commands()
                .stream()
                .filter(
                        command ->
                                command.title()
                                        .toLowerCase()
                                        .contains(query))
                .toList();
    }

    public Command selectedCommand() {

        List<Command> commands =
                commands();

        if (commands.isEmpty()) {
            return null;
        }

        return commands.get(
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

            case CHARACTER -> {

                if (keyEvent.hasCharacter()) {

                    state.appendSearchCharacter(
                            keyEvent.character());
                }
            }

            case BACKSPACE ->
                    state.removeLastSearchCharacter();

            case ENTER -> {

                Command command =
                        selectedCommand();

                if (command == null) {
                    return true;
                }

                close();

                commandPaletteExecutor.execute(
                        command);

                return true;
            }

            case ESCAPE -> {

                close();

                return true;
            }

            default -> {
            }
        }

        return false;
    }

    public String searchQuery() {
        return state.searchQuery();
    }
}