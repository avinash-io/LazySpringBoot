package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.environment.EnvironmentInfo;
import io.github.avinashio.lazyspringboot.ui.component.ModalRenderer;
import io.github.avinashio.lazyspringboot.ui.controller.EnvironmentController;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EnvironmentScreen {

private static final int WIDTH = 90;

private static final int MINIMUM_WIDTH = 70;

private static final int PADDING = 4;

private static final String FOOTER =
		"Esc Back";

private final ModalRenderer
		modalRenderer;

private final EnvironmentController
		controller;

public EnvironmentScreen(
		ModalRenderer modalRenderer,
		EnvironmentController controller) {
	
	this.modalRenderer =
			modalRenderer;
	
	this.controller =
			controller;
}

public void render() {
	
	modalRenderer.renderFixedWidth(
			"Developer Environment",
			buildContent(),
			FOOTER,
			WIDTH,
			MINIMUM_WIDTH,
			PADDING);
}

private List<String> buildContent() {
	
	EnvironmentInfo info =
			controller.environmentInfo();
	
	if (info == null) {
		
		return List.of(
				"Loading...");
	}
	
	List<String> lines =
			new ArrayList<>();
	
	addProperty(
			lines,
			"Java",
			info.javaVersion());
	
	addProperty(
			lines,
			"Maven",
			info.mavenVersion());
	
	addProperty(
			lines,
			"Git",
			info.gitVersion());
	
	addProperty(
			lines,
			"IntelliJ",
			info.hasIntelliJ()
					? "Installed"
					: "Not Installed");
	
	addProperty(
			lines,
			"VS Code",
			info.hasVSCode()
					? "Installed"
					: "Not Installed");
	
	lines.add("");
	
	lines.add("Workspace");
	
	lines.add(
			info.workspace());
	
	return lines;
}

private void addProperty(
		List<String> lines,
		String label,
		String value) {
	
	lines.add(
			String.format(
					"%-15s %s",
					label,
					value));
}
}