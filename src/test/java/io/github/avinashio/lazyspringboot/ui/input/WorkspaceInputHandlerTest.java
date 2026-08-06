package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.ui.controller.WorkspaceController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class WorkspaceInputHandlerTest {

private WorkspaceController
		workspaceController;

private WorkspaceInputHandler
		handler;

@BeforeEach
void setUp() {
	
	workspaceController =
			mock(
					WorkspaceController.class);
	
	handler =
			new WorkspaceInputHandler(
					workspaceController);
}

@Test
void shouldIgnoreInputWhenWorkspaceIsClosed() {
	
	when(workspaceController.isOpen())
			.thenReturn(
					false);
	
	boolean handled =
			handler.handle(
					character(
							'c'));
	
	assertThat(handled)
			.isFalse();
	
	verify(
			workspaceController,
			never())
			.copyWorkspacePath();
}

@Test
void shouldCopyWorkspacePath() {
	
	openWorkspace();
	
	boolean handled =
			handler.handle(
					character(
							'c'));
	
	assertThat(handled)
			.isTrue();
	
	verify(workspaceController)
			.copyWorkspacePath();
}

@Test
void shouldCopyWorkspacePathWithUppercaseShortcut() {
	
	openWorkspace();
	
	handler.handle(
			character(
					'C'));
	
	verify(workspaceController)
			.copyWorkspacePath();
}

@Test
void shouldOpenWorkspaceFolder() {
	
	openWorkspace();
	
	handler.handle(
			character(
					'o'));
	
	verify(workspaceController)
			.openWorkspace();
}

@Test
void shouldStartWorkspaceChange() {
	
	openWorkspace();
	
	handler.handle(
			character(
					'e'));
	
	verify(workspaceController)
			.startWorkspaceChange();
}

@Test
void shouldCloseWorkspaceOnEscape() {
	
	openWorkspace();
	
	handler.handle(
			key(
					KeyType.ESCAPE));
	
	verify(workspaceController)
			.close();
}

@Test
void shouldAppendCharactersWhileChangingWorkspace() {
	
	changingWorkspace();
	
	handler.handle(
			character(
					'/'));
	
	verify(workspaceController)
			.appendWorkspaceCharacter(
					'/');
}

@Test
void shouldAppendShortcutCharactersAsPathInputWhileEditing() {
	
	changingWorkspace();
	
	handler.handle(
			character(
					'c'));
	
	verify(workspaceController)
			.appendWorkspaceCharacter(
					'c');
	
	verify(
			workspaceController,
			never())
			.copyWorkspacePath();
}

@Test
void shouldBackspaceWorkspaceInput() {
	
	changingWorkspace();
	
	handler.handle(
			key(
					KeyType.BACKSPACE));
	
	verify(workspaceController)
			.backspaceWorkspaceInput();
}

@Test
void shouldSubmitWorkspaceChangeOnEnter() {
	
	changingWorkspace();
	
	handler.handle(
			key(
					KeyType.ENTER));
	
	verify(workspaceController)
			.submitWorkspaceChange();
}

@Test
void shouldCancelWorkspaceChangeOnEscape() {
	
	changingWorkspace();
	
	handler.handle(
			key(
					KeyType.ESCAPE));
	
	verify(workspaceController)
			.cancelWorkspaceChange();
	
	verify(
			workspaceController,
			never())
			.close();
}

@Test
void shouldIgnoreUnsupportedKeysWhileWorkspaceIsOpen() {
	
	openWorkspace();
	
	boolean handled =
			handler.handle(
					key(
							KeyType.UP));
	
	assertThat(handled)
			.isTrue();
	
	verify(
			workspaceController,
			never())
			.close();
}

@Test
void shouldIgnoreUnsupportedKeysWhileChangingWorkspace() {
	
	changingWorkspace();
	
	boolean handled =
			handler.handle(
					key(
							KeyType.UP));
	
	assertThat(handled)
			.isTrue();
	
	verify(
			workspaceController,
			never())
			.submitWorkspaceChange();
	
	verify(
			workspaceController,
			never())
			.cancelWorkspaceChange();
}

private void openWorkspace() {
	
	when(workspaceController.isOpen())
			.thenReturn(
					true);
	
	when(workspaceController.changingWorkspace())
			.thenReturn(
					false);
}

private void changingWorkspace() {
	
	when(workspaceController.isOpen())
			.thenReturn(
					true);
	
	when(workspaceController.changingWorkspace())
			.thenReturn(
					true);
}

private KeyEvent character(
		char character) {
	
	return new KeyEvent(
			KeyType.CHARACTER,
			character);
}

private KeyEvent key(
		KeyType type) {
	
	return new KeyEvent(
			type,
			null);
}

@Test
void shouldAppendSlashWhileChangingWorkspace() {
	
	changingWorkspace();
	
	handler.handle(
			key(
					KeyType.SEARCH));
	
	verify(workspaceController)
			.appendWorkspaceCharacter(
					'/');
}
}