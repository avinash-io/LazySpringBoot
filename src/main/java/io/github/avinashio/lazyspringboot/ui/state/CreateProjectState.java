package io.github.avinashio.lazyspringboot.ui.state;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ConfigurationFileFormat;
import io.github.avinashio.lazyspringboot.domain.project.ProjectPackaging;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class CreateProjectState {

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

private static final List<BuildTool> AVAILABLE_BUILD_TOOLS =
		List.of(
				BuildTool.MAVEN,
				BuildTool.GRADLE,
				BuildTool.GRADLE_KOTLIN);

private static final List<ProjectPackaging> AVAILABLE_PACKAGING =
		List.of(
				ProjectPackaging.JAR,
				ProjectPackaging.WAR);

private static final List<ConfigurationFileFormat>
		AVAILABLE_CONFIGURATION_FILE_FORMATS =
		List.of(
				ConfigurationFileFormat.PROPERTIES,
				ConfigurationFileFormat.YAML);

private final StringBuilder inputBuffer =
		new StringBuilder();

private final Set<String> selectedDependencies =
		new LinkedHashSet<>();

private CreateProjectPane activePane =
		CreateProjectPane.FORM;

private BuildTool buildTool =
		BuildTool.MAVEN;

private ProjectPackaging packaging =
		ProjectPackaging.JAR;

private ConfigurationFileFormat configurationFileFormat =
		ConfigurationFileFormat.YAML;

private boolean active;

private CreateProjectStage stage =
		CreateProjectStage.METADATA;

private int selectedField;

private String name = "";

private String groupId = "com.example";

private String artifactId = "";

private String packageName = "";

private String javaVersion = "21";

private String springBootVersion = "4.1.0";

private boolean editing;

private List<SpringDependency> dependencies =
		List.of();

private int selectedDependencyIndex;

private String dependencySearchQuery = "";

private boolean dependencySearchActive;

private String errorMessage = "";

private List<String> availableJavaVersions =
		List.of();

private List<String> availableSpringBootVersions =
		List.of();

private boolean versionSelecting;

private int selectedVersionIndex;

private boolean artifactManuallyEdited;

private boolean packageManuallyEdited;

private boolean buildToolSelecting;

private int selectedBuildToolIndex;

public String errorMessage() {
	return errorMessage;
}

public boolean hasErrorMessage() {
	return !errorMessage.isBlank();
}

public void showErrorMessage(
		String message) {
	
	errorMessage = message;
}

public void clearErrorMessage() {
	
	errorMessage = "";
}

public boolean active() {
	return active;
}

public void open() {
	
	active = true;
	
	stage =
			CreateProjectStage.METADATA;
	
	selectedField =
			BUILD_TOOL_FIELD;
	
	name = "";
	
	groupId = "com.example";
	
	artifactId = "";
	
	packageName = "";
	
	artifactManuallyEdited = false;
	
	packageManuallyEdited = false;
	
	editing = false;
	
	versionSelecting = false;
	
	selectedDependencyIndex = 0;
	
	dependencySearchQuery = "";
	
	dependencySearchActive = false;
	
	selectedDependencies.clear();
	
	clearErrorMessage();
	
	buildTool =
			BuildTool.MAVEN;
	
	buildToolSelecting = false;
	
	selectedBuildToolIndex = 0;
	
	packaging =
			ProjectPackaging.JAR;
	
	configurationFileFormat =
			ConfigurationFileFormat.YAML;
	
	activePane =
			CreateProjectPane.FORM;
}

public CreateProjectPane activePane() {
	
	return activePane;
}

public boolean formPaneActive() {
	
	return activePane
				   == CreateProjectPane.FORM;
}

public boolean dependenciesPaneActive() {
	
	return activePane
				   == CreateProjectPane.DEPENDENCIES;
}

public void activateFormPane() {
	
	activePane =
			CreateProjectPane.FORM;
	
	dependencySearchActive = false;
}

public void activateDependenciesPane() {
	
	activePane =
			CreateProjectPane.DEPENDENCIES;
	
	editing = false;
}

public void close() {
	
	active = false;
	
	editing = false;
	
	dependencySearchActive = false;
}

public CreateProjectStage stage() {
	return stage;
}

public boolean metadataStage() {
	
	return stage
				   == CreateProjectStage.METADATA;
}

public boolean dependencyStage() {
	
	return stage
				   == CreateProjectStage.DEPENDENCIES;
}

public void showMetadataStage() {
	
	stage =
			CreateProjectStage.METADATA;
	
	editing = false;
	
	dependencySearchActive = false;
}

public void showDependencyStage() {
	
	stage =
			CreateProjectStage.DEPENDENCIES;
	
	editing = false;
	
	selectedDependencyIndex = 0;
	
	dependencySearchQuery = "";
	
	dependencySearchActive = false;
}

public int selectedField() {
	return selectedField;
}

public void nextField() {
	
	if (selectedField
				< DEPENDENCIES_FIELD) {
		
		selectedField++;
		return;
	}
	
	if (selectedField
				== DEPENDENCIES_FIELD
				&& readyToGenerate()) {
		
		selectedField =
				GENERATE_FIELD;
	}
}

public void previousField() {
	
	if (selectedField > 0) {
		
		selectedField--;
	}
}

public boolean readyToGenerate() {
	
	return buildTool != null
				   && buildTool != BuildTool.UNKNOWN
				   && packaging != null
				   && configurationFileFormat != null
				   && !groupId.isBlank()
				   && !artifactId.isBlank()
				   && !packageName.isBlank()
				   && !javaVersion.isBlank()
				   && !springBootVersion.isBlank();
}

public String name() {
	return name;
}

public void setName(
		String name) {
	
	this.name = name;
}

public String groupId() {
	return groupId;
}

public void setGroupId(
		String groupId) {
	
	this.groupId = groupId;
}

public String artifactId() {
	return artifactId;
}

public void setArtifactId(
		String artifactId) {
	
	this.artifactId = artifactId;
}

public String packageName() {
	return packageName;
}

public void setPackageName(
		String packageName) {
	
	this.packageName = packageName;
}

public String javaVersion() {
	return javaVersion;
}

public void setJavaVersion(
		String javaVersion) {
	
	this.javaVersion = javaVersion;
}

public String springBootVersion() {
	return springBootVersion;
}

public void setSpringBootVersion(
		String springBootVersion) {
	
	this.springBootVersion =
			springBootVersion;
}

public ProjectPackaging packaging() {
	return packaging;
}

public void setPackaging(
		ProjectPackaging packaging) {
	
	this.packaging = packaging;
}

public List<ProjectPackaging> availablePackaging() {
	
	return AVAILABLE_PACKAGING;
}

public void selectNextPackagingInline() {
	
	int currentIndex =
			AVAILABLE_PACKAGING.indexOf(
					packaging);
	
	if (currentIndex < 0
				|| currentIndex
						   >= AVAILABLE_PACKAGING.size() - 1) {
		
		return;
	}
	
	packaging =
			AVAILABLE_PACKAGING.get(
					currentIndex + 1);
}

public void selectPreviousPackagingInline() {
	
	int currentIndex =
			AVAILABLE_PACKAGING.indexOf(
					packaging);
	
	if (currentIndex <= 0) {
		return;
	}
	
	packaging =
			AVAILABLE_PACKAGING.get(
					currentIndex - 1);
}

public ConfigurationFileFormat configurationFileFormat() {
	
	return configurationFileFormat;
}

public void setConfigurationFileFormat(
		ConfigurationFileFormat configurationFileFormat) {
	
	this.configurationFileFormat =
			configurationFileFormat;
}

public List<ConfigurationFileFormat>
availableConfigurationFileFormats() {
	
	return AVAILABLE_CONFIGURATION_FILE_FORMATS;
}

public void selectNextConfigurationFileFormatInline() {
	
	int currentIndex =
			AVAILABLE_CONFIGURATION_FILE_FORMATS.indexOf(
					configurationFileFormat);
	
	if (currentIndex < 0
				|| currentIndex
						   >= AVAILABLE_CONFIGURATION_FILE_FORMATS.size() - 1) {
		
		return;
	}
	
	configurationFileFormat =
			AVAILABLE_CONFIGURATION_FILE_FORMATS.get(
					currentIndex + 1);
}

public void selectPreviousConfigurationFileFormatInline() {
	
	int currentIndex =
			AVAILABLE_CONFIGURATION_FILE_FORMATS.indexOf(
					configurationFileFormat);
	
	if (currentIndex <= 0) {
		return;
	}
	
	configurationFileFormat =
			AVAILABLE_CONFIGURATION_FILE_FORMATS.get(
					currentIndex - 1);
}

public boolean editing() {
	return editing;
}

public void startEditing() {
	
	editing = true;
	
	inputBuffer.setLength(0);
	
	inputBuffer.append(
			currentValue());
}

public void stopEditing() {
	
	editing = false;
}

public void append(
		char character) {
	
	inputBuffer.append(
			character);
	
	updateCurrentField();
}

public void backspace() {
	
	if (inputBuffer.isEmpty()) {
		return;
	}
	
	inputBuffer.deleteCharAt(
			inputBuffer.length() - 1);
	
	updateCurrentField();
}

public void setDependencies(
		List<SpringDependency> dependencies) {
	
	this.dependencies =
			List.copyOf(
					dependencies);
	
	selectedDependencyIndex = 0;
}

public List<SpringDependency> dependencies() {
	
	return dependencies;
}

public List<SpringDependency> filteredDependencies() {
	
	String query =
			dependencySearchQuery
					.trim()
					.toLowerCase();
	
	if (query.isEmpty()) {
		return dependencies;
	}
	
	return dependencies
				   .stream()
				   .filter(
						   dependency ->
								   matchesDependency(
										   dependency,
										   query))
				   .toList();
}

public int selectedDependencyIndex() {
	return selectedDependencyIndex;
}

public SpringDependency selectedDependency() {
	
	List<SpringDependency> filtered =
			filteredDependencies();
	
	if (filtered.isEmpty()) {
		return null;
	}
	
	if (selectedDependencyIndex
				>= filtered.size()) {
		
		selectedDependencyIndex =
				filtered.size() - 1;
	}
	
	return filtered.get(
			selectedDependencyIndex);
}

public void selectNextDependency() {
	
	int dependencyCount =
			filteredDependencies()
					.size();
	
	if (dependencyCount <= 0) {
		return;
	}
	
	if (selectedDependencyIndex
				< dependencyCount - 1) {
		
		selectedDependencyIndex++;
	}
}

public void selectPreviousDependency() {
	
	if (selectedDependencyIndex > 0) {
		
		selectedDependencyIndex--;
	}
}

public List<String> selectedDependencies() {
	
	return List.copyOf(
			selectedDependencies);
}

public boolean dependencySelected(
		String dependencyId) {
	
	return selectedDependencies.contains(
			dependencyId);
}

public void selectDependency(
		String dependencyId) {
	
	if (dependencyId == null
				|| dependencyId.isBlank()) {
		
		return;
	}
	
	selectedDependencies.add(
			dependencyId);
}

public void removeDependency(
		String dependencyId) {
	
	selectedDependencies.remove(
			dependencyId);
}

public void toggleDependency(
		String dependencyId) {
	
	if (dependencySelected(
			dependencyId)) {
		
		removeDependency(
				dependencyId);
		
		return;
	}
	
	selectDependency(
			dependencyId);
}

public void toggleSelectedDependency() {
	
	SpringDependency dependency =
			selectedDependency();
	
	if (dependency == null) {
		return;
	}
	
	toggleDependency(
			dependency.id());
}

public void clearDependencies() {
	
	selectedDependencies.clear();
}

public boolean dependencySearchActive() {
	
	return dependencySearchActive;
}

public String dependencySearchQuery() {
	
	return dependencySearchQuery;
}

public void startDependencySearch() {
	
	dependencySearchActive = true;
	
	dependencySearchQuery = "";
	
	selectedDependencyIndex = 0;
}

public void stopDependencySearch() {
	
	dependencySearchActive = false;
	
	dependencySearchQuery = "";
	
	selectedDependencyIndex = 0;
}

public void appendDependencySearch(
		char character) {
	
	dependencySearchQuery +=
			character;
	
	selectedDependencyIndex = 0;
}

public void backspaceDependencySearch() {
	
	if (dependencySearchQuery.isEmpty()) {
		return;
	}
	
	dependencySearchQuery =
			dependencySearchQuery.substring(
					0,
					dependencySearchQuery.length() - 1);
	
	selectedDependencyIndex = 0;
}

private boolean matchesDependency(
		SpringDependency dependency,
		String query) {
	
	return containsIgnoreCase(
			dependency.id(),
			query)
				   || containsIgnoreCase(
			dependency.name(),
			query)
				   || containsIgnoreCase(
			dependency.description(),
			query)
				   || containsIgnoreCase(
			dependency.group(),
			query);
}

private boolean containsIgnoreCase(
		String value,
		String query) {
	
	return value != null
				   && value
							  .toLowerCase()
							  .contains(query);
}

private String currentValue() {
	
	return switch (selectedField) {
		
		case GROUP_FIELD -> groupId;
		
		case ARTIFACT_FIELD -> artifactId;
		
		case NAME_FIELD -> name;
		
		case PACKAGE_FIELD -> packageName;
		
		default -> "";
	};
}

private void updateCurrentField() {
	
	String value =
			inputBuffer.toString();
	
	switch (selectedField) {
		
		case GROUP_FIELD -> {
			
			groupId =
					value;
			
			if (!packageManuallyEdited) {
				
				packageName =
						buildPackageName();
			}
		}
		
		case ARTIFACT_FIELD -> {
			
			artifactId =
					value;
			
			artifactManuallyEdited =
					true;
			
			if (!packageManuallyEdited) {
				
				packageName =
						buildPackageName();
			}
		}
		
		case NAME_FIELD -> {
			
			name =
					value;
			
			if (!artifactManuallyEdited) {
				
				artifactId =
						toArtifactId(
								value);
			}
			
			if (!packageManuallyEdited) {
				
				packageName =
						buildPackageName();
			}
		}
		
		case PACKAGE_FIELD -> {
			
			packageName =
					value;
			
			packageManuallyEdited =
					true;
		}
		
		default -> {
			// No action.
		}
	}
}

private String toArtifactId(
		String projectName) {
	
	return projectName
				   .trim()
				   .toLowerCase()
				   .replaceAll(
						   "[^a-z0-9]+",
						   "-")
				   .replaceAll(
						   "^-|-$",
						   "");
}

private String buildPackageName() {
	
	String packageSuffix =
			artifactId
					.replaceAll(
							"[^a-zA-Z0-9]",
							"")
					.toLowerCase();
	
	if (groupId.isBlank()) {
		
		return packageSuffix;
	}
	
	if (packageSuffix.isBlank()) {
		
		return groupId;
	}
	
	return groupId
				   + "."
				   + packageSuffix;
}

public List<String> availableJavaVersions() {
	
	return availableJavaVersions;
}

public void setAvailableJavaVersions(
		List<String> versions) {
	
	availableJavaVersions =
			List.copyOf(
					versions);
}

public List<String> availableSpringBootVersions() {
	
	return availableSpringBootVersions;
}

public void setAvailableSpringBootVersions(
		List<String> versions) {
	
	availableSpringBootVersions =
			List.copyOf(
					versions);
}

public boolean versionSelecting() {
	
	return versionSelecting;
}

public void startVersionSelection() {
	
	versionSelecting =
			true;
	
	List<String> versions =
			currentVersionOptions();
	
	String currentVersion =
			selectedField == JAVA_FIELD
					? javaVersion
					: springBootVersion;
	
	int currentIndex =
			versions.indexOf(
					currentVersion);
	
	selectedVersionIndex =
			Math.max(
					currentIndex,
					0);
}

public void stopVersionSelection() {
	
	versionSelecting =
			false;
}

public int selectedVersionIndex() {
	
	return selectedVersionIndex;
}

public List<String> currentVersionOptions() {
	
	if (selectedField
				== JAVA_FIELD) {
		
		return availableJavaVersions;
	}
	
	if (selectedField
				== SPRING_BOOT_FIELD) {
		
		return availableSpringBootVersions;
	}
	
	return List.of();
}

public void selectNextVersion() {
	
	List<String> versions =
			currentVersionOptions();
	
	if (selectedVersionIndex
				< versions.size() - 1) {
		
		selectedVersionIndex++;
	}
}

public void selectPreviousVersion() {
	
	if (selectedVersionIndex > 0) {
		
		selectedVersionIndex--;
	}
}

public void confirmVersionSelection() {
	
	List<String> versions =
			currentVersionOptions();
	
	if (versions.isEmpty()) {
		
		versionSelecting =
				false;
		
		return;
	}
	
	String selectedVersion =
			versions.get(
					selectedVersionIndex);
	
	if (selectedField
				== JAVA_FIELD) {
		
		javaVersion =
				selectedVersion;
		
	} else if (selectedField
					   == SPRING_BOOT_FIELD) {
		
		springBootVersion =
				selectedVersion;
	}
	
	versionSelecting =
			false;
}

public BuildTool buildTool() {
	return buildTool;
}

public boolean buildToolSelecting() {
	return buildToolSelecting;
}

public void startBuildToolSelection() {
	
	buildToolSelecting =
			true;
	
	int currentIndex =
			AVAILABLE_BUILD_TOOLS.indexOf(
					buildTool);
	
	selectedBuildToolIndex =
			Math.max(
					currentIndex,
					0);
}

public void stopBuildToolSelection() {
	
	buildToolSelecting =
			false;
}

public int selectedBuildToolIndex() {
	
	return selectedBuildToolIndex;
}

public List<BuildTool> availableBuildTools() {
	
	return AVAILABLE_BUILD_TOOLS;
}

public void selectNextBuildTool() {
	
	if (selectedBuildToolIndex
				< AVAILABLE_BUILD_TOOLS.size() - 1) {
		
		selectedBuildToolIndex++;
	}
}

public void selectPreviousBuildTool() {
	
	if (selectedBuildToolIndex > 0) {
		
		selectedBuildToolIndex--;
	}
}

public void confirmBuildToolSelection() {
	
	buildTool =
			AVAILABLE_BUILD_TOOLS.get(
					selectedBuildToolIndex);
	
	buildToolSelecting =
			false;
}

public void selectNextBuildToolInline() {
	
	int currentIndex =
			AVAILABLE_BUILD_TOOLS.indexOf(
					buildTool);
	
	if (currentIndex < 0
				|| currentIndex
						   >= AVAILABLE_BUILD_TOOLS.size() - 1) {
		
		return;
	}
	
	buildTool =
			AVAILABLE_BUILD_TOOLS.get(
					currentIndex + 1);
}

public void selectPreviousBuildToolInline() {
	
	int currentIndex =
			AVAILABLE_BUILD_TOOLS.indexOf(
					buildTool);
	
	if (currentIndex <= 0) {
		return;
	}
	
	buildTool =
			AVAILABLE_BUILD_TOOLS.get(
					currentIndex - 1);
}

public void selectNextVersionInline() {
	
	List<String> versions =
			currentInlineVersionOptions();
	
	String currentVersion =
			currentInlineVersion();
	
	int currentIndex =
			versions.indexOf(
					currentVersion);
	
	if (currentIndex < 0
				|| currentIndex
						   >= versions.size() - 1) {
		
		return;
	}
	
	setCurrentInlineVersion(
			versions.get(
					currentIndex + 1));
}

public void selectPreviousVersionInline() {
	
	List<String> versions =
			currentInlineVersionOptions();
	
	String currentVersion =
			currentInlineVersion();
	
	int currentIndex =
			versions.indexOf(
					currentVersion);
	
	if (currentIndex <= 0) {
		return;
	}
	
	setCurrentInlineVersion(
			versions.get(
					currentIndex - 1));
}

private List<String> currentInlineVersionOptions() {
	
	if (selectedField
				== SPRING_BOOT_FIELD) {
		
		return availableSpringBootVersions;
	}
	
	if (selectedField
				== JAVA_FIELD) {
		
		return availableJavaVersions;
	}
	
	return List.of();
}

private String currentInlineVersion() {
	
	if (selectedField
				== SPRING_BOOT_FIELD) {
		
		return springBootVersion;
	}
	
	if (selectedField
				== JAVA_FIELD) {
		
		return javaVersion;
	}
	
	return "";
}

private void setCurrentInlineVersion(
		String version) {
	
	if (selectedField
				== SPRING_BOOT_FIELD) {
		
		springBootVersion =
				version;
		
	} else if (selectedField
					   == JAVA_FIELD) {
		
		javaVersion =
				version;
	}
}
}