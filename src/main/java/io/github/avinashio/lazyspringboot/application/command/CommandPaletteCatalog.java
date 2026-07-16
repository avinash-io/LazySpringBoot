package io.github.avinashio.lazyspringboot.application.command;

import io.github.avinashio.lazyspringboot.domain.command.CommandPaletteItem;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CommandPaletteCatalog {

    public List<CommandPaletteItem> commands() {

        return List.of(
                CommandPaletteItem.BUILD,
                CommandPaletteItem.TEST,
                CommandPaletteItem.RUN,
                CommandPaletteItem.STOP,
                CommandPaletteItem.VIEW_LOGS,
                CommandPaletteItem.ADD_DEPENDENCY,
                CommandPaletteItem.REMOVE_DEPENDENCY,
                CommandPaletteItem.CREATE_PROJECT,
                CommandPaletteItem.REFRESH_PROJECTS);
    }
}