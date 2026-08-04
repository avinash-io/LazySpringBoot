package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.ui.controller.CreateProjectController;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class CreateProjectInputHandler
		implements InputHandler {

private static final int BUILD_TOOL_FIELD = 0;

private static final int SPRING_BOOT_FIELD = 1;

private static final int FIRST_TEXT_FIELD = 2;

private static final int LAST_TEXT_FIELD = 5;

private static final int PACKAGING_FIELD = 6;

private static final int CONFIG_FIELD = 7;

private static final int JAVA_FIELD = 8;

private static final int DEPENDENCIES_FIELD = 9;

private static final int GENERATE_FIELD = 10;

private final CreateProjectController
		createProjectController;

public CreateProjectInputHandler(
		CreateProjectController createProjectController) {
	
	this.createProjectController =
			createProjectController;
}

@Override
public boolean handle(
		KeyEvent keyEvent) {
	
	CreateProjectState state =
			createProjectController.state();
	
	if (!state.active()) {
		return false;
	}
	
	if (state.dependenciesPaneActive()) {
		
		handleDependenciesPane(
				state,
				keyEvent);
		
		return true;
	}
	
	handleFormPane(
			state,
			keyEvent);
	
	return true;
}

private void handleFormPane(
		CreateProjectState state,
		KeyEvent keyEvent) {
	
	if (state.editing()) {
		
		handleMetadataEditing(
				state,
				keyEvent);
		
		return;
	}
	
	switch (keyEvent.type()) {
		
		case ESCAPE -> createProjectController.close();
		
		case DOWN -> state.nextField();
		
		case UP -> state.previousField();
		
		case LEFT -> selectPreviousOption(
				state);
		
		case RIGHT -> {
			
			if (state.selectedField()
						== DEPENDENCIES_FIELD) {
				
				state.activateDependenciesPane();
				
			} else {
				
				selectNextOption(
						state);
			}
		}
		
		case ENTER -> activateSelectedField(
				state);
		
		default -> {
			// No action.
		}
	}
}

private void selectPreviousOption(
		CreateProjectState state) {
	
	switch (state.selectedField()) {
		
		case BUILD_TOOL_FIELD -> state.selectPreviousBuildToolInline();
		
		case SPRING_BOOT_FIELD,
			 JAVA_FIELD -> state.selectPreviousVersionInline();
		
		case PACKAGING_FIELD -> state.selectPreviousPackagingInline();
		
		case CONFIG_FIELD -> state.selectPreviousConfigurationFileFormatInline();
		
		default -> {
			// Current row has no inline options.
		}
	}
}

private void selectNextOption(
		CreateProjectState state) {
	
	switch (state.selectedField()) {
		
		case BUILD_TOOL_FIELD -> state.selectNextBuildToolInline();
		
		case SPRING_BOOT_FIELD,
			 JAVA_FIELD -> state.selectNextVersionInline();
		
		case PACKAGING_FIELD -> state.selectNextPackagingInline();
		
		case CONFIG_FIELD -> state.selectNextConfigurationFileFormatInline();
		
		default -> {
			// Current row has no inline options.
		}
	}
}

private void activateSelectedField(
		CreateProjectState state) {
	
	int selectedField =
			state.selectedField();
	
	if (selectedField >= FIRST_TEXT_FIELD
				&& selectedField <= LAST_TEXT_FIELD) {
		
		state.startEditing();
		
		return;
	}
	
	if (selectedField
				== DEPENDENCIES_FIELD) {
		
		state.activateDependenciesPane();
		
		return;
	}
	
	if (selectedField
				== GENERATE_FIELD
				&& state.readyToGenerate()) {
		
		createProjectController.generate(
				Path.of("")
						.toAbsolutePath());
	}
}

private void handleMetadataEditing(
		CreateProjectState state,
		KeyEvent keyEvent) {
	
	switch (keyEvent.type()) {
		
		case ENTER -> state.stopEditing();
		
		case BACKSPACE -> state.backspace();
		
		case CHARACTER -> {
			
			if (keyEvent.hasCharacter()) {
				
				state.append(
						keyEvent.character());
			}
		}
		
		case ESCAPE -> state.stopEditing();
		
		default -> {
			// No action.
		}
	}
}

private void handleDependenciesPane(
		CreateProjectState state,
		KeyEvent keyEvent) {
	
	if (state.dependencySearchActive()) {
		
		handleDependencySearch(
				state,
				keyEvent);
		
		return;
	}
	
	switch (keyEvent.type()) {
		
		case UP -> state.selectPreviousDependency();
		
		case DOWN -> state.selectNextDependency();
		
		case SPACE,
			 ENTER -> state.toggleSelectedDependency();
		
		case SEARCH -> state.startDependencySearch();
		
		case LEFT,
			 ESCAPE -> state.activateFormPane();
		
		default -> {
			// No action.
		}
	}
}

private void handleDependencySearch(
		CreateProjectState state,
		KeyEvent keyEvent) {
	
	switch (keyEvent.type()) {
		
		case UP -> state.selectPreviousDependency();
		
		case DOWN -> state.selectNextDependency();
		
		case SPACE,
			 ENTER -> state.toggleSelectedDependency();
		
		case BACKSPACE -> state.backspaceDependencySearch();
		
		case ESCAPE -> state.stopDependencySearch();
		
		case CHARACTER -> {
			
			if (!keyEvent.hasCharacter()) {
				break;
			}
			
			if (keyEvent.character()
						== ' ') {
				
				state.toggleSelectedDependency();
				
			} else {
				
				state.appendDependencySearch(
						keyEvent.character());
			}
		}
		
		case QUIT -> state.appendDependencySearch(
				'q');
		
		case UNDO -> state.appendDependencySearch(
				'u');
		
		case ACTIONS -> state.appendDependencySearch(
				'a');
		
		case GO_TO_TOP -> state.appendDependencySearch(
				'g');
		
		case GO_TO_BOTTOM -> state.appendDependencySearch(
				'G');
		
		default -> {
			// No action.
		}
	}
}
}