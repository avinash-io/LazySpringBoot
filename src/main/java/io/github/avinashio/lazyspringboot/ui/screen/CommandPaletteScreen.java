package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.ui.command.Command;
import java.io.PrintWriter;
import java.util.List;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.stereotype.Component;

@Component
public class CommandPaletteScreen {

    private final Terminal terminal;

    public CommandPaletteScreen(
            Terminal terminal) {

        this.terminal = terminal;
    }

    public void render(
            List<Command> commands,
            int selectedIndex,
            String searchQuery) {

        PrintWriter writer =
                terminal.writer();

        terminal.puts(
                InfoCmp.Capability.clear_screen);

        terminal.puts(
                InfoCmp.Capability.cursor_address,
                0,
                0);

        writer.println(
                "LazySpringBoot");

        writer.println();

        writer.println(
                "Command Palette");

        writer.println();

        writer.println(
                "Search: "
                        + searchQuery
                        + "_");

        writer.println();

        for (int index = 0;
             index < commands.size();
             index++) {

            String prefix =
                    index == selectedIndex
                            ? "> "
                            : "  ";

            writer.println(
                    prefix
                            + commands
                            .get(index)
                            .title());
        }

        writer.println();

        writer.println(
                "Type to Search    ↑↓ Navigate"
                        + "    Enter Execute"
                        + "    Backspace Delete"
                        + "    Esc Close");

        writer.flush();
    }
}