package io.github.avinashio.lazyspringboot.ui.command;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CommandCatalog {

    public List<Command> commands() {

        return List.of(
                new Command(
                        "create-project",
                        "Create Spring Boot Project"),
                new Command(
                        "refresh-projects",
                        "Refresh Projects"),
                new Command(
                        "start-project",
                        "Start Project"),
                new Command(
                        "stop-project",
                        "Stop Project"),
                new Command(
                        "view-logs",
                        "View Logs"),
                new Command(
                        "add-dependencies",
                        "Add Dependencies"),
                new Command(
                        "undo-dependencies",
                        "Undo Dependency Changes"));
    }
}