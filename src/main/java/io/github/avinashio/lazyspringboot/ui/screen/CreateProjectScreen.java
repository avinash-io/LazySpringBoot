package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ConfigurationFileFormat;
import io.github.avinashio.lazyspringboot.domain.project.ProjectPackaging;
import io.github.avinashio.lazyspringboot.ui.component.ModalRenderer;
import io.github.avinashio.lazyspringboot.ui.component.TerminalStyle;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateProjectScreen {

private static final int WIDTH_PERCENTAGE = 58;

private static final int HEIGHT_PERCENTAGE = 58;

private static final int MINIMUM_POPUP_WIDTH = 110;

private static final int MINIMUM_POPUP_HEIGHT = 28;

private static final int LEFT_PANE_PERCENTAGE = 48;

private static final int PANE_SEPARATOR_WIDTH = 3;

private static final int MAX_VISIBLE_DEPENDENCIES = 15;

private static final int BUILD_TOOL_FIELD = 0;

private static final int SPRING_BOOT_FIELD = 1;

private static final int GROUP_FIELD = 2;

private static final int ARTIFACT_FIELD = 3;

private static final int NAME_FIELD = 4;

private static final int PACKAGE_FIELD = 5;

private static final int PACKAGING_FIELD = 6;

private static final int CONFIG_FIELD = 7;

private static final int JAVA_FIELD = 8;

private static final int DEPENDENCIES_FIELD = 9;

private static final int GENERATE_FIELD = 10;

private static final int LABEL_WIDTH = 11;

private final ModalRenderer modalRenderer;

private final TerminalStyle terminalStyle;

public CreateProjectScreen(
		ModalRenderer modalRenderer,
		TerminalStyle terminalStyle) {
	
	this.modalRenderer =
			modalRenderer;
	
	this.terminalStyle =
			terminalStyle;
}

public void render(
		CreateProjectState state) {
	
	int modalWidth =
			modalRenderer.percentageWidth(
					WIDTH_PERCENTAGE,
					MINIMUM_POPUP_WIDTH);
	
	int modalHeight =
			modalRenderer.percentageHeight(
					HEIGHT_PERCENTAGE,
					MINIMUM_POPUP_HEIGHT);
	
	modalRenderer.renderPercentageSize(
			"Create Spring Boot Project",
			buildContent(
					state,
					modalWidth,
					modalHeight),
			" " + buildNavigationText(state),
			WIDTH_PERCENTAGE,
			HEIGHT_PERCENTAGE,
			MINIMUM_POPUP_WIDTH,
			MINIMUM_POPUP_HEIGHT);
}

private List<String> buildContent(
		CreateProjectState state,
		int modalWidth,
		int modalHeight) {
	
	int contentWidth =
			Math.max(
					1,
					modalWidth - 2);
	
	int availablePaneWidth =
			Math.max(
					1,
					contentWidth - PANE_SEPARATOR_WIDTH);
	
	int leftPaneWidth =
			Math.max(
					1,
					availablePaneWidth
							* LEFT_PANE_PERCENTAGE
							/ 100);
	
	int rightPaneWidth =
			Math.max(
					1,
					availablePaneWidth
							- leftPaneWidth);
	
	int availableContentHeight =
			Math.max(
					1,
					modalHeight - 4);
	
	List<String> left =
			buildFormContent(
					state);
	
	List<String> right =
			buildDependencyContent(
					state,
					rightPaneWidth,
					availableContentHeight);
	
	int rows =
			Math.max(
					availableContentHeight,
					Math.max(
							left.size(),
							right.size()));
	
	List<String> lines =
			new ArrayList<>();
	
	for (int row = 0;
		 row < rows;
		 row++) {
		
		String leftLine =
				row < left.size()
						? left.get(row)
						: "";
		
		String rightLine =
				row < right.size()
						? right.get(row)
						: "";
		
		lines.add(
				combinePanes(
						leftLine,
						rightLine,
						leftPaneWidth,
						rightPaneWidth));
	}
	
	return lines;
}

private List<String> buildFormContent(
		CreateProjectState state) {
	
	List<String> lines =
			new ArrayList<>();
	
	lines.add(
			paneTitle(
					state.formPaneActive(),
					"PROJECT"));
	
	lines.add("");
	
	lines.add(
			section(
					"Build Tool"));
	
	lines.add(
			choiceRow(
					state,
					BUILD_TOOL_FIELD,
					buildToolOptions(
							state)));
	
	lines.add("");
	
	lines.add(
			section(
					"Spring Boot"));
	
	lines.addAll(
			springBootVersionRows(
					state));
	
	lines.add("");
	
	lines.add(
			sectionHeading(
					"PROJECT METADATA"));
	
	lines.add("");
	
	lines.add(
			textField(
					state,
					GROUP_FIELD,
					"Group",
					state.groupId()));
	
	lines.add(
			textField(
					state,
					ARTIFACT_FIELD,
					"Artifact",
					state.artifactId()));
	
	lines.add(
			textField(
					state,
					NAME_FIELD,
					"Name",
					state.name()));
	
	lines.add(
			textField(
					state,
					PACKAGE_FIELD,
					"Package",
					state.packageName()));
	
	lines.add("");
	
	lines.add(
			sectionHeading(
					"OPTIONS"));
	
	lines.add("");
	
	lines.add(
			optionField(
					state,
					PACKAGING_FIELD,
					"Packaging",
					packagingOptions(
							state)));
	
	lines.add(
			optionField(
					state,
					CONFIG_FIELD,
					"Config",
					configurationOptions(
							state)));
	
	lines.add(
			optionField(
					state,
					JAVA_FIELD,
					"Java",
					versionOptions(
							state.availableJavaVersions(),
							state.javaVersion())));
	
	lines.add("");
	
	lines.add(
			generateField(
					state));
	
	if (state.hasErrorMessage()) {
		
		lines.add("");
		
		lines.add(
				"   Error: "
						+ state.errorMessage());
	}
	
	return lines;
}

private List<String> springBootVersionRows(
		CreateProjectState state) {
	
	List<String> versions =
			state.availableSpringBootVersions();
	
	if (versions.isEmpty()) {
		
		return List.of(
				choiceRow(
						state,
						SPRING_BOOT_FIELD,
						radioOption(
								true,
								state.springBootVersion())));
	}
	
	List<String> options =
			new ArrayList<>();
	
	for (String version : versions) {
		
		options.add(
				radioOption(
						version.equals(
								state.springBootVersion()),
						version));
	}
	
	/*
	 * Keep Spring Boot versions compact and Initializr-like.
	 * Two options per row prevents long snapshot versions from
	 * colliding with the center separator.
	 */
	List<String> rows =
			new ArrayList<>();
	
	for (int index = 0;
		 index < options.size();
		 index += 2) {
		
		int end =
				Math.min(
						index + 2,
						options.size());
		
		String value =
				String.join(
						"   ",
						options.subList(
								index,
								end));
		
		boolean focused =
				state.formPaneActive()
						&& state.selectedField()
								   == SPRING_BOOT_FIELD;
		
		rows.add(
				simpleChoiceRow(
						focused
								&& index == 0,
						value));
	}
	
	return rows;
}

private List<String> buildDependencyContent(
		CreateProjectState state,
		int rightPaneWidth,
		int availableContentHeight) {
	
	List<String> lines =
			new ArrayList<>();
	
	lines.add(
			paneTitle(
					state.dependenciesPaneActive(),
					"DEPENDENCIES"));
	
	lines.add("");
	
	lines.add(
			dependencySearch(
					state));
	
	lines.add(
			dependencyDivider(
					rightPaneWidth));
	
	lines.add("");
	
	List<SpringDependency> dependencies =
			state.filteredDependencies();
	
	if (dependencies.isEmpty()) {
		
		lines.add(
				"   No matching dependencies");
		
	} else {
		
		buildGroupedDependencyLines(
				lines,
				state,
				dependencies,
				rightPaneWidth);
	}
	
	int footerRows = 4;
	
	while (lines.size()
				   < Math.max(
			0,
			availableContentHeight
					- footerRows)) {
		
		lines.add("");
	}
	
	lines.add(
			dependencyDivider(
					rightPaneWidth));
	
	lines.add(
			dependencyRangeText(
					state,
					dependencies));
	
	lines.add(
			selectedDependenciesText(
					state));
	
	return lines;
}

private void buildGroupedDependencyLines(
		List<String> lines,
		CreateProjectState state,
		List<SpringDependency> dependencies,
		int rightPaneWidth) {
	
	int selectedIndex =
			state.selectedDependencyIndex();
	
	int startIndex =
			calculateStartIndex(
					selectedIndex,
					dependencies.size());
	
	int endIndex =
			Math.min(
					startIndex
							+ MAX_VISIBLE_DEPENDENCIES,
					dependencies.size());
	
	String previousGroup =
			null;
	
	for (int index = startIndex;
		 index < endIndex;
		 index++) {
		
		SpringDependency dependency =
				dependencies.get(index);
		
		String group =
				displayGroup(
						dependency.group());
		
		if (!group.equals(
				previousGroup)) {
			
			if (previousGroup != null) {
				lines.add("");
			}
			
			lines.add(
					"   "
							+ group);
			
			previousGroup =
					group;
		}
		
		boolean selected =
				state.dependencySelected(
						dependency.id());
		
		boolean focused =
				state.dependenciesPaneActive()
						&& index == selectedIndex;
		
		String cursor =
				focused
						? ">"
						: " ";
		
		String checkbox =
				selected
						? "[x]"
						: "[ ]";
		
		int nameWidth =
				Math.max(
						10,
						rightPaneWidth - 10);
		
		lines.add(
				" "
						+ cursor
						+ " "
						+ checkbox
						+ " "
						+ truncate(
						dependency.name(),
						nameWidth));
	}
}

private String dependencySearch(
		CreateProjectState state) {
	
	if (state.dependencySearchActive()) {
		
		return "   Search: "
					   + state.dependencySearchQuery()
					   + "_";
	}
	
	return "   Search: /";
}

private String dependencyDivider(
		int rightPaneWidth) {
	
	return "   "
				   + "─".repeat(
			Math.max(
					1,
					rightPaneWidth - 6));
}

private String dependencyRangeText(
		CreateProjectState state,
		List<SpringDependency> dependencies) {
	
	if (dependencies.isEmpty()) {
		
		return "   Showing 0 of 0";
	}
	
	int startIndex =
			calculateStartIndex(
					state.selectedDependencyIndex(),
					dependencies.size());
	
	int endIndex =
			Math.min(
					startIndex
							+ MAX_VISIBLE_DEPENDENCIES,
					dependencies.size());
	
	return String.format(
			"   Showing %d–%d of %d",
			startIndex + 1,
			endIndex,
			dependencies.size());
}

private String selectedDependenciesText(
		CreateProjectState state) {
	
	int count =
			state.selectedDependencies()
					.size();
	
	if (count == 0) {
		
		return "   Selected: None";
	}
	
	if (count == 1) {
		
		return "   Selected: 1 dependency";
	}
	
	return "   Selected: "
				   + count
				   + " dependencies";
}

private String paneTitle(
		boolean active,
		String title) {
	
	if (active) {
		
		return "  > "
					   + title;
	}
	
	return "    "
				   + title;
}

private String sectionHeading(
		String title) {
	
	return "   "
				   + title;
}

private String section(
		String title) {
	
	return "   "
				   + title;
}

private String choiceRow(
		CreateProjectState state,
		int field,
		String options) {
	
	boolean focused =
			state.formPaneActive()
					&& state.selectedField()
							   == field;
	
	return simpleChoiceRow(
			focused,
			options);
}

private String simpleChoiceRow(
		boolean focused,
		String options) {
	
	return " "
				   + (focused
							  ? ">"
							  : " ")
				   + " "
				   + options;
}

private String textField(
		CreateProjectState state,
		int field,
		String label,
		String value) {
	
	boolean focused =
			state.formPaneActive()
					&& state.selectedField()
							   == field;
	
	boolean editing =
			focused
					&& state.editing();
	
	return formRow(
			focused,
			label,
			displayValue(
					value)
					+ (editing
							   ? "_"
							   : ""));
}

private String optionField(
		CreateProjectState state,
		int field,
		String label,
		String options) {
	
	boolean focused =
			state.formPaneActive()
					&& state.selectedField()
							   == field;
	
	return formRow(
			focused,
			label,
			options);
}

private String generateField(
		CreateProjectState state) {
	
	boolean ready =
			state.readyToGenerate();
	
	boolean focused =
			state.formPaneActive()
					&& ready
					&& state.selectedField()
							   == GENERATE_FIELD;
	
	String value =
			"[ Generate Project ]";
	
	if (!ready) {
		
		value =
				terminalStyle.dim(
						value);
	}
	
	return " "
				   + (focused
							  ? ">"
							  : " ")
				   + " "
				   + value;
}

private String formRow(
		boolean focused,
		String label,
		String value) {
	
	return String.format(
			" %s %-"
					+ LABEL_WIDTH
					+ "s %s",
			focused
					? ">"
					: " ",
			label,
			value);
}

private String buildToolOptions(
		CreateProjectState state) {
	
	List<String> options =
			new ArrayList<>();
	
	for (BuildTool buildTool :
			state.availableBuildTools()) {
		
		options.add(
				radioOption(
						buildTool
								== state.buildTool(),
						buildToolLabel(
								buildTool)));
	}
	
	return String.join(
			"   ",
			options);
}

private String packagingOptions(
		CreateProjectState state) {
	
	List<String> options =
			new ArrayList<>();
	
	for (ProjectPackaging packaging :
			state.availablePackaging()) {
		
		options.add(
				radioOption(
						packaging
								== state.packaging(),
						packaging.label()));
	}
	
	return String.join(
			"   ",
			options);
}

private String configurationOptions(
		CreateProjectState state) {
	
	List<String> options =
			new ArrayList<>();
	
	for (ConfigurationFileFormat format :
			state.availableConfigurationFileFormats()) {
		
		options.add(
				radioOption(
						format
								== state.configurationFileFormat(),
						format.label()));
	}
	
	return String.join(
			"   ",
			options);
}

private String versionOptions(
		List<String> versions,
		String selectedVersion) {
	
	if (versions.isEmpty()) {
		
		return radioOption(
				true,
				selectedVersion);
	}
	
	List<String> options =
			new ArrayList<>();
	
	for (String version : versions) {
		
		options.add(
				radioOption(
						version.equals(
								selectedVersion),
						version));
	}
	
	return String.join(
			" ",
			options);
}

private String radioOption(
		boolean selected,
		String label) {
	
	return (selected
					? "(*) "
					: "( ) ")
				   + label;
}

private String buildToolLabel(
		BuildTool buildTool) {
	
	return switch (buildTool) {
		
		case MAVEN -> "Maven";
		
		case GRADLE -> "Gradle";
		
		case GRADLE_KOTLIN -> "Gradle Kotlin";
		
		case UNKNOWN -> "Unknown";
	};
}

private String displayGroup(
		String group) {
	
	if (group == null
				|| group.isBlank()) {
		
		return "Other";
	}
	
	return group;
}

private String displayValue(
		String value) {
	
	if (value == null
				|| value.isBlank()) {
		
		return "";
	}
	
	return value;
}

private String truncate(
		String value,
		int width) {
	
	if (value == null) {
		
		return "";
	}
	
	if (value.length()
				<= width) {
		
		return value;
	}
	
	if (width <= 1) {
		
		return "…";
	}
	
	return value.substring(
			0,
			width - 1)
				   + "…";
}

private int calculateStartIndex(
		int selectedIndex,
		int dependencyCount) {
	
	if (dependencyCount
				<= MAX_VISIBLE_DEPENDENCIES) {
		
		return 0;
	}
	
	int halfWindow =
			MAX_VISIBLE_DEPENDENCIES
					/ 2;
	
	int start =
			Math.max(
					0,
					selectedIndex
							- halfWindow);
	
	return Math.min(
			start,
			dependencyCount
					- MAX_VISIBLE_DEPENDENCIES);
}

private String combinePanes(
		String left,
		String right,
		int leftPaneWidth,
		int rightPaneWidth) {
	
	return fitPlain(
			left,
			leftPaneWidth)
				   + " │ "
				   + fitPlain(
			right,
			rightPaneWidth);
}

private String fitPlain(
		String value,
		int width) {
	
	if (value == null) {
		
		return " ".repeat(
				width);
	}
	
	int visibleLength =
			visibleLength(
					value);
	
	if (visibleLength > width) {
		
		String stripped =
				stripAnsi(
						value);
		
		if (stripped.length()
					> width) {
			
			stripped =
					stripped.substring(
							0,
							Math.max(
									0,
									width - 1))
							+ "…";
		}
		
		return String.format(
				"%-"
						+ width
						+ "s",
				stripped);
	}
	
	return value
				   + " ".repeat(
			Math.max(
					0,
					width
							- visibleLength));
}

private int visibleLength(
		String value) {
	
	return stripAnsi(
			value)
				   .length();
}

private String stripAnsi(
		String value) {
	
	return value.replaceAll(
			"\\u001B\\[[;\\d]*m",
			"");
}

private String buildNavigationText(
		CreateProjectState state) {
	
	if (state.dependencySearchActive()) {
		
		return "Type to Search"
					   + "   ↑↓ Navigate"
					   + "   Space/Enter Toggle"
					   + "   Esc Close Search";
	}
	
	if (state.dependenciesPaneActive()) {
		
		return "↑↓ Navigate"
					   + "   Space/Enter Toggle"
					   + "   / Search"
					   + "   ← Form"
					   + "   Esc Form";
	}
	
	return "↑↓ Navigate"
				   + "   ←→ Select"
				   + "   Enter Edit/Open"
				   + "   / Search"
				   + "   Space Toggle"
				   + "   Esc Cancel";
}
}