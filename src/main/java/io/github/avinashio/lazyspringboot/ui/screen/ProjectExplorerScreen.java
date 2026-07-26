package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.controller.ProjectExplorerController;
import java.io.PrintWriter;
import java.util.List;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.stereotype.Component;

@Component
public class ProjectExplorerScreen {

    private final Terminal terminal;

    private final ProjectExplorerController
            controller;

    public ProjectExplorerScreen(
            Terminal terminal,
            ProjectExplorerController controller) {

        this.terminal =
                terminal;

        this.controller =
                controller;
    }

    public void render() {

        terminal.puts(
                InfoCmp.Capability.clear_screen);

        terminal.puts(
                InfoCmp.Capability.cursor_address,
                0,
                0);

        PrintWriter writer =
                terminal.writer();

        writer.println(
                "Project Explorer");

        writer.println(
                "────────────────────────────────────────");

        List<SpringProject> projects =
                controller.projects();

        SpringProject selected =
                controller.selectedProject();

        for (SpringProject project : projects) {

            String prefix =
                    project.equals(selected)
                            ? "► "
                            : "  ";

            writer.println(
                    prefix + project.name());
        }

        writer.println();

        writer.println(
                "────────────────────────────────────────");

        writer.println(
                "Enter  Open Folder");

        writer.println(
                "I      IntelliJ");

        writer.println(
                "V      VS Code");

        writer.println(
                "C      Copy Path");

        writer.println(
                "Esc    Close");

        writer.flush();
    }
}