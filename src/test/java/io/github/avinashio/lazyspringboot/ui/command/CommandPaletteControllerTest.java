package io.github.avinashio.lazyspringboot.ui.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;
import io.github.avinashio.lazyspringboot.domain.project.SpringProject;
import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;
import io.github.avinashio.lazyspringboot.ui.input.KeyType;
import io.github.avinashio.lazyspringboot.ui.state.CommandPaletteState;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CommandPaletteControllerTest {

    @Test
    void shouldNavigateCommands() {

        CommandPaletteExecutor commandPaletteExecutor =
                mock(CommandPaletteExecutor.class);

        CommandCatalog commandCatalog =
                createCommandCatalogWithSelectedProject();

        CommandPaletteController controller =
                new CommandPaletteController(
                        commandCatalog,
                        new CommandPaletteState(),
                        commandPaletteExecutor);

        controller.open();

        controller.handleKey(
                KeyEvent.of(
                        KeyType.DOWN));

        assertThat(
                controller.state()
                        .selectedCommandIndex())
                .isEqualTo(1);
    }

    @Test
    void shouldClosePalette() {

        CommandPaletteExecutor commandPaletteExecutor =
                mock(CommandPaletteExecutor.class);

        CommandCatalog commandCatalog =
                createCommandCatalogWithSelectedProject();

        CommandPaletteController controller =
                new CommandPaletteController(
                        commandCatalog,
                        new CommandPaletteState(),
                        commandPaletteExecutor);

        controller.open();

        controller.handleKey(
                KeyEvent.of(
                        KeyType.ESCAPE));

        assertThat(
                controller.active())
                .isFalse();
    }

    @Test
    void shouldExecuteSelectedCommand() {

        CommandPaletteExecutor commandPaletteExecutor =
                mock(CommandPaletteExecutor.class);

        CommandCatalog commandCatalog =
                createCommandCatalogWithSelectedProject();

        CommandPaletteController controller =
                new CommandPaletteController(
                        commandCatalog,
                        new CommandPaletteState(),
                        commandPaletteExecutor);

        controller.open();

        Command selectedCommand =
                controller.selectedCommand();

        controller.handleKey(
                KeyEvent.of(
                        KeyType.ENTER));

        verify(commandPaletteExecutor)
                .execute(
                        selectedCommand);

        assertThat(
                controller.active())
                .isFalse();
    }

    private CommandCatalog
    createCommandCatalogWithSelectedProject() {

        UiState uiState =
                mock(UiState.class);

        GetProjectProcessUseCase getProjectProcessUseCase =
                mock(GetProjectProcessUseCase.class);

        SpringProject project =
                mock(SpringProject.class);

        when(uiState.selectedProject())
                .thenReturn(
                        project);

        when(getProjectProcessUseCase.get(
                project))
                .thenReturn(
                        Optional.empty());

        return new CommandCatalog(
                uiState,
                getProjectProcessUseCase);
    }

    @Test
    void shouldFilterCommandsBySearchQuery() {

        CommandPaletteExecutor commandPaletteExecutor =
                mock(CommandPaletteExecutor.class);

        CommandCatalog commandCatalog =
                createCommandCatalogWithSelectedProject();

        CommandPaletteController controller =
                new CommandPaletteController(
                        commandCatalog,
                        new CommandPaletteState(),
                        commandPaletteExecutor);

        controller.open();

        controller.state()
                .appendSearchCharacter('s');

        controller.state()
                .appendSearchCharacter('t');

        controller.state()
                .appendSearchCharacter('a');

        assertThat(controller.commands())
                .extracting(Command::id)
                .containsExactly(
                        "start-project");
    }

    @Test
    void shouldReturnNoCommandsWhenSearchDoesNotMatch() {

        CommandPaletteExecutor commandPaletteExecutor =
                mock(CommandPaletteExecutor.class);

        CommandCatalog commandCatalog =
                createCommandCatalogWithSelectedProject();

        CommandPaletteController controller =
                new CommandPaletteController(
                        commandCatalog,
                        new CommandPaletteState(),
                        commandPaletteExecutor);

        controller.open();

        controller.state()
                .appendSearchCharacter('x');

        controller.state()
                .appendSearchCharacter('y');

        controller.state()
                .appendSearchCharacter('z');

        assertThat(controller.commands())
                .isEmpty();

        assertThat(controller.selectedCommand())
                .isNull();
    }

    @Test
    void shouldNotExecuteCommandWhenSearchHasNoMatch() {

        CommandPaletteExecutor commandPaletteExecutor =
                mock(CommandPaletteExecutor.class);

        CommandCatalog commandCatalog =
                createCommandCatalogWithSelectedProject();

        CommandPaletteController controller =
                new CommandPaletteController(
                        commandCatalog,
                        new CommandPaletteState(),
                        commandPaletteExecutor);

        controller.open();

        controller.state()
                .appendSearchCharacter('x');

        controller.state()
                .appendSearchCharacter('y');

        controller.state()
                .appendSearchCharacter('z');

        controller.handleKey(
                KeyEvent.of(
                        KeyType.ENTER));

        verifyNoInteractions(
                commandPaletteExecutor);

        assertThat(controller.active())
                .isTrue();
    }
}