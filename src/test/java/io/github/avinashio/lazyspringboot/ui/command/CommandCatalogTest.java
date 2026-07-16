package io.github.avinashio.lazyspringboot.ui.command;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CommandCatalogTest {

    @Test
    void shouldContainCreateProjectCommand() {

        CommandCatalog catalog =
                new CommandCatalog();

        assertThat(catalog.commands())
                .extracting(Command::id)
                .contains("create-project");
    }
}