package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.ui.component.*;
import io.github.avinashio.lazyspringboot.ui.controller.TextInputController;
import io.github.avinashio.lazyspringboot.ui.service.ProjectFilterService;
import io.github.avinashio.lazyspringboot.ui.state.PanelFocus;
import io.github.avinashio.lazyspringboot.ui.state.ProjectSortState;
import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.util.List;

@Component
public class DashboardScreen {

private static final int MINIMUM_TERMINAL_WIDTH = 80;

private static final int PROJECT_PANEL_PERCENTAGE = 55;

private static final int DETAILS_PANEL_PERCENTAGE = 40;

private static final int CONTEXT_LEFT_PADDING = 2;

private static final int DEPENDENCY_TOP_PADDING = 1;

private static final int DETAILS_TOP_PADDING = 2;

private final Terminal terminal;

private final ProjectPanel projectPanel;

private final ProjectDetailsPanel projectDetailsPanel;

private final StatusBar statusBar;

private final TextFormatter textFormatter;

private final DependencyPanel dependencyPanel;

private final ProjectFilterService projectFilterService;

private final TextInputController textInputController;

private final ProjectSortState projectSortState;

public DashboardScreen(
		Terminal terminal,
		ProjectPanel projectPanel,
		DependencyPanel dependencyPanel,
		ProjectDetailsPanel projectDetailsPanel,
		StatusBar statusBar,
		TextFormatter textFormatter,
		ProjectFilterService projectFilterService,
		TextInputController textInputController,
		ProjectSortState projectSortState) {
	
	this.terminal = terminal;
	this.projectPanel = projectPanel;
	this.dependencyPanel = dependencyPanel;
	this.projectDetailsPanel = projectDetailsPanel;
	this.statusBar = statusBar;
	this.textFormatter = textFormatter;
	this.projectFilterService = projectFilterService;
	this.textInputController = textInputController;
	this.projectSortState = projectSortState;
}

public void render(UiState state) {
	
	PrintWriter writer = terminal.writer();
	
	terminal.puts(InfoCmp.Capability.clear_screen);
	
	terminal.puts(
			InfoCmp.Capability.cursor_address,
			0,
			0);
	
	int width = terminal.getWidth();
	
	if (width < MINIMUM_TERMINAL_WIDTH) {
		
		renderTerminalTooSmall(
				writer,
				width);
		
		writer.flush();
		
		return;
	}
	
	int height = terminal.getHeight();
	
	int contentHeight =
			Math.max(
					1,
					height - 4);
	
	int projectPanelWidth =
			width
					* PROJECT_PANEL_PERCENTAGE
					/ 100;
	
	int contextPanelWidth =
			width
					- projectPanelWidth
					- 1;
	
	int detailsHeight =
			Math.max(
					1,
					contentHeight
							* DETAILS_PANEL_PERCENTAGE
							/ 100);
	
	int dependenciesHeight =
			Math.max(
					1,
					contentHeight
							- detailsHeight
							- 1);
	
	int dependencyContentHeight =
			Math.max(
					1,
					dependenciesHeight
							- DEPENDENCY_TOP_PADDING);
	
	List<String> projectLines =
			projectPanel.render(
					state,
					contentHeight,
					projectPanelWidth);
	
	List<String> dependencyLines =
			dependencyPanel.render(
					state,
					dependencyContentHeight);
	
	List<String> detailLines =
			projectDetailsPanel.render(
					state.selectedProject());
	
	renderHeader(
			writer,
			state,
			projectPanelWidth,
			contextPanelWidth);
	
	renderPanels(
			writer,
			state,
			projectLines,
			dependencyLines,
			detailLines,
			projectPanelWidth,
			contextPanelWidth,
			contentHeight,
			dependenciesHeight);
	
	renderFooter(
			writer,
			state,
			width);
	
	writer.flush();
}

private void renderHeader(
		PrintWriter writer,
		UiState state,
		int projectPanelWidth,
		int contextPanelWidth) {
	
	PanelFocus panelFocus =
			state.panelFocus();
	
	String projectsTitle =
			projectTitle(state);
	
	if (panelFocus == PanelFocus.PROJECTS) {
		
		projectsTitle =
				"["
						+ projectsTitle
						+ "]";
	}
	
	String dependenciesTitle =
			panelFocus
					== PanelFocus.DEPENDENCIES
					? "[Dependencies]"
					: "Dependencies";
	
	writer.print(
			panelHeader(
					"┌",
					projectsTitle,
					projectPanelWidth));
	
	writer.print(
			panelHeader(
					"┬",
					dependenciesTitle,
					contextPanelWidth));
	
	writer.println("┐");
}

private String projectTitle(
		UiState state) {
	
	int totalProjects =
			state.projects()
					.size();
	
	String count;
	
	if (textInputController.active(
			TextInputPurpose.PROJECT_SEARCH)) {
		
		int visibleProjects =
				projectFilterService
						.filter(
								state.projects(),
								textInputController.value())
						.size();
		
		count =
				visibleProjects
						+ "/"
						+ totalProjects;
		
	} else {
		
		count =
				String.valueOf(
						totalProjects);
	}
	
	return "Projects ("
				   + count
				   + ") · "
				   + projectSortState
							 .mode()
							 .label();
}

private String panelHeader(
		String border,
		String title,
		int width) {
	
	return border
				   + "─ "
				   + title
				   + " "
				   + "─".repeat(
			Math.max(
					0,
					width
							- title.length()
							- 4));
}

private void renderPanels(
		PrintWriter writer,
		UiState state,
		List<String> projectLines,
		List<String> dependencyLines,
		List<String> detailLines,
		int projectPanelWidth,
		int contextPanelWidth,
		int contentHeight,
		int dependenciesHeight) {
	
	for (int row = 0;
		 row < contentHeight;
		 row++) {
		
		writer.print("│");
		
		writer.print(
				textFormatter.fit(
						lineAt(
								projectLines,
								row),
						projectPanelWidth - 1));
		
		if (row == dependenciesHeight) {
			
			String detailsTitle =
					state.panelFocus()
							== PanelFocus.PROJECT_DETAILS
							? "[Project Details]"
							: "Project Details";
			
			writer.print(
					panelHeader(
							"├",
							detailsTitle,
							contextPanelWidth));
			
			writer.println("┤");
			
			continue;
		}
		
		writer.print("│");
		
		if (row < dependenciesHeight) {
			
			int dependencyRow =
					row
							- DEPENDENCY_TOP_PADDING;
			
			renderContextLine(
					writer,
					dependencyRow < 0
							? ""
							: lineAt(
							dependencyLines,
							dependencyRow),
					contextPanelWidth);
			
		} else {
			
			int detailRow =
					row
							- dependenciesHeight
							- 1
							- DETAILS_TOP_PADDING;
			
			renderContextLine(
					writer,
					detailRow < 0
							? ""
							: lineAt(
							detailLines,
							detailRow),
					contextPanelWidth);
		}
		
		writer.println("│");
	}
}

private void renderContextLine(
		PrintWriter writer,
		String line,
		int contextPanelWidth) {
	
	String padding =
			" ".repeat(
					CONTEXT_LEFT_PADDING);
	
	writer.print(
			textFormatter.fit(
					padding + line,
					contextPanelWidth - 1));
}

private String lineAt(
		List<String> lines,
		int index) {
	
	if (index < 0
				|| index >= lines.size()) {
		
		return "";
	}
	
	return lines.get(index);
}

private void renderFooter(
		PrintWriter writer,
		UiState state,
		int width) {
	
	writer.print("├");
	
	writer.print(
			"─".repeat(
					width - 2));
	
	writer.print("┤");
	
	writer.println();
	
	writer.print("│");
	
	writer.print(
			textFormatter.fit(
					statusBar.render(
							state),
					width - 2));
	
	writer.print("│");
	
	writer.println();
	
	writer.print("└");
	
	writer.print(
			"─".repeat(
					width - 2));
	
	writer.print("┘");
}

private void renderTerminalTooSmall(
		PrintWriter writer,
		int width) {
	
	writer.println("LazySpringBoot");
	
	writer.println();
	
	writer.println(
			"Terminal width is too small: "
					+ width);
	
	writer.println(
			"Minimum required width: "
					+ MINIMUM_TERMINAL_WIDTH);
}
}