package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import java.io.PrintWriter;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.stereotype.Component;

@Component
public class CreateProjectScreen {

    private final Terminal terminal;

    public CreateProjectScreen(
            Terminal terminal) {
        this.terminal = terminal;
    }

    public void render(
            CreateProjectState state) {

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
                "Create Spring Boot Project");
        writer.println();

        renderField(
                writer,
                state,
                0,
                "Name",
                state.name());

        renderField(
                writer,
                state,
                1,
                "Group",
                state.groupId());

        renderField(
                writer,
                state,
                2,
                "Artifact",
                state.artifactId());

        renderField(
                writer,
                state,
                3,
                "Package",
                state.packageName());

        renderField(
                writer,
                state,
                4,
                "Java",
                state.javaVersion());

        renderField(
                writer,
                state,
                5,
                "Spring Boot",
                state.springBootVersion());

        writer.println();
        writer.println(
                "Tab Next    Shift+Tab Previous");
        writer.println(
                "Enter Continue    Esc Cancel");

        writer.flush();
    }

    private void renderField(
            PrintWriter writer,
            CreateProjectState state,
            int index,
            String label,
            String value) {

        String marker =
                state.selectedField() == index
                        ? (state.editing()
                        ? "*"
                        : ">")
                        : " ";

        writer.printf(
                "%s %-15s %s%n",
                marker,
                label,
                value);
    }
}