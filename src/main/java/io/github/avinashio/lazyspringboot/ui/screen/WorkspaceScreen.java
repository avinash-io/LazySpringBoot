package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.ui.component.ModalRenderer;
import io.github.avinashio.lazyspringboot.ui.controller.WorkspaceController;
import io.github.avinashio.lazyspringboot.ui.state.WorkspaceState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkspaceScreen {

private static final int POPUP_WIDTH = 70;

private static final int MINIMUM_POPUP_WIDTH = 50;

private static final int POPUP_PADDING = 4;

private static final String DEFAULT_FOOTER =
		" C Copy Path   O Open Folder   E Change Workspace   Esc Close";

private static final String EDITING_FOOTER =
		" Enter Apply   Esc Cancel";

private final ModalRenderer modalRenderer;

private final WorkspaceState workspaceState;

private final WorkspaceController
		workspaceController;

public WorkspaceScreen(
		ModalRenderer modalRenderer,
		WorkspaceState workspaceState,
		WorkspaceController workspaceController) {
	
	this.modalRenderer =
			modalRenderer;
	
	this.workspaceState =
			workspaceState;
	
	this.workspaceController =
			workspaceController;
}

public void render() {
	
	modalRenderer.renderFixedWidth(
			title(),
			buildContent(),
			footer(),
			POPUP_WIDTH,
			MINIMUM_POPUP_WIDTH,
			POPUP_PADDING);
}

private String title() {
	
	if (workspaceController.changingWorkspace()) {
		return "Change Workspace";
	}
	
	return "Current Workspace";
}

private String footer() {
	
	if (workspaceController.changingWorkspace()) {
		return EDITING_FOOTER;
	}
	
	return DEFAULT_FOOTER;
}

private List<String> buildContent() {
	
	if (workspaceController.changingWorkspace()) {
		return buildEditingContent();
	}
	
	return buildCurrentWorkspaceContent();
}

private List<String> buildCurrentWorkspaceContent() {
	
	List<String> lines =
			new ArrayList<>();
	
	lines.add("Current Workspace");
	lines.add("");
	
	lines.add(
			workspaceState.workspace());
	
	addErrorMessage(
			lines);
	
	return lines;
}

private List<String> buildEditingContent() {
	
	List<String> lines =
			new ArrayList<>();
	
	lines.add("New Workspace");
	lines.add("");
	
	lines.add(
			"> "
					+ workspaceController
							  .workspaceInput());
	
	addErrorMessage(
			lines);
	
	return lines;
}

private void addErrorMessage(
		List<String> lines) {
	
	if (!workspaceState.hasErrorMessage()) {
		return;
	}
	
	lines.add("");
	
	lines.add(
			"Error: "
					+ workspaceState
							  .errorMessage());
}
}