package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.ui.controller.WorkspaceController;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceInputHandler {

private final WorkspaceController
		workspaceController;

public WorkspaceInputHandler(
		WorkspaceController workspaceController) {
	
	this.workspaceController =
			workspaceController;
}

public boolean handle(
		KeyEvent keyEvent) {
	
	if (!workspaceController.isOpen()) {
		return false;
	}
	
	if (workspaceController.changingWorkspace()) {
		
		handleWorkspaceInput(
				keyEvent);
		
		return true;
	}
	
	switch (keyEvent.type()) {
		
		case CHARACTER -> handleCharacter(
				keyEvent);
		
		case ESCAPE -> workspaceController.close();
		
		default -> {
			// Ignore unsupported keys.
		}
	}
	
	return true;
}

private void handleWorkspaceInput(
		KeyEvent keyEvent) {
	
	switch (keyEvent.type()) {
		
		case ENTER -> workspaceController
							  .submitWorkspaceChange();
		
		case ESCAPE -> workspaceController
							   .cancelWorkspaceChange();
		
		case BACKSPACE -> workspaceController
								  .backspaceWorkspaceInput();
		
		case SEARCH -> workspaceController
							   .appendWorkspaceCharacter(
									   '/');
		
		case CHARACTER -> appendWorkspaceCharacter(
				keyEvent);
		
		default -> {
			// Ignore unsupported keys while editing.
		}
	}
}

private void handleCharacter(
		KeyEvent keyEvent) {
	
	Character character =
			keyEvent.character();
	
	if (character == null) {
		return;
	}
	
	switch (Character.toLowerCase(
			character)) {
		
		case 'c' -> workspaceController
							.copyWorkspacePath();
		
		case 'o' -> workspaceController
							.openWorkspace();
		
		case 'e' -> workspaceController
							.startWorkspaceChange();
		
		default -> {
			// Ignore unsupported shortcuts.
		}
	}
}

private void appendWorkspaceCharacter(
		KeyEvent keyEvent) {
	
	Character character =
			keyEvent.character();
	
	if (character == null) {
		return;
	}
	
	workspaceController
			.appendWorkspaceCharacter(
					character);
}
}