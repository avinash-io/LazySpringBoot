package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.command.CommandPaletteItem;
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
            List<CommandPaletteItem> commands,
            int selectedIndex) {

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

        for (int index = 0;
             index < commands.size();
             index++) {

            String prefix =
                    index == selectedIndex
                            ? "> "
                            : "  ";

            writer.println(
                    prefix
                            + commands.get(index)
                            .title());
        }

        writer.println();
        writer.println(
                "↑↓ Navigate    Enter Execute    Esc Close");

        writer.flush();
    }
}