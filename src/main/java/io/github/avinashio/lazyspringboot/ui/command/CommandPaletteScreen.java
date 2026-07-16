package io.github.avinashio.lazyspringboot.ui.command;

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
            CommandPaletteState state,
            List<Command> commands) {

        PrintWriter writer =
                terminal.writer();

        terminal.puts(
                InfoCmp.Capability.clear_screen);

        terminal.puts(
                InfoCmp.Capability.cursor_address,
                0,
                0);

        writer.println(
                "Command Palette");
        writer.println();

        for (int index = 0;
             index < commands.size();
             index++) {

            Command command =
                    commands.get(index);

            String marker =
                    state.selectedIndex() == index
                            ? ">"
                            : " ";

            writer.printf(
                    "%s %s%n",
                    marker,
                    command.title());
        }

        writer.println();
        writer.println(
                "↑ ↓ Navigate");
        writer.println(
                "Enter Execute");
        writer.println(
                "Esc Close");

        writer.flush();
    }
}