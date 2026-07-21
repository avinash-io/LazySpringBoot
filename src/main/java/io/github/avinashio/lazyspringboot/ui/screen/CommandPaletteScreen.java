package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.ui.command.Command;
import io.github.avinashio.lazyspringboot.ui.component.ModalRenderer;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CommandPaletteScreen {

    private static final int POPUP_WIDTH = 60;

    private static final int MINIMUM_POPUP_WIDTH = 40;

    private static final int MAXIMUM_VISIBLE_COMMANDS = 10;

    private static final int POPUP_PADDING = 4;

    private static final String FOOTER =
            " ↑↓ Navigate"
                    + "  Enter Execute"
                    + "  Esc Close";

    private final ModalRenderer modalRenderer;

    public CommandPaletteScreen(
            ModalRenderer modalRenderer) {

        this.modalRenderer = modalRenderer;
    }

    public void render(
            List<Command> commands,
            int selectedIndex,
            String searchQuery) {

        int visibleCommandCount =
                Math.min(
                        commands.size(),
                        MAXIMUM_VISIBLE_COMMANDS);

        List<String> content =
                buildContent(
                        commands,
                        selectedIndex,
                        searchQuery,
                        visibleCommandCount);

        modalRenderer.renderFixedWidth(
                "Command Palette",
                content,
                FOOTER,
                POPUP_WIDTH,
                MINIMUM_POPUP_WIDTH,
                POPUP_PADDING);
    }

    private List<String> buildContent(
            List<Command> commands,
            int selectedIndex,
            String searchQuery,
            int visibleCommandCount) {

        List<String> lines =
                new ArrayList<>();

        lines.add(
                " Search: "
                        + searchQuery
                        + "_");

        lines.add("");

        int firstVisibleIndex =
                calculateFirstVisibleIndex(
                        commands.size(),
                        selectedIndex,
                        visibleCommandCount);

        for (int row = 0;
             row < visibleCommandCount;
             row++) {

            int commandIndex =
                    firstVisibleIndex
                            + row;

            if (commandIndex
                    >= commands.size()) {
                break;
            }

            String prefix =
                    commandIndex
                            == selectedIndex
                            ? " > "
                            : "   ";

            lines.add(
                    prefix
                            + commands
                            .get(commandIndex)
                            .title());
        }

        return lines;
    }

    private int calculateFirstVisibleIndex(
            int commandCount,
            int selectedIndex,
            int visibleCommandCount) {

        if (commandCount
                <= visibleCommandCount) {
            return 0;
        }

        int firstVisibleIndex =
                selectedIndex
                        - visibleCommandCount
                        + 1;

        return Math.max(
                0,
                Math.min(
                        firstVisibleIndex,
                        commandCount
                                - visibleCommandCount));
    }
}