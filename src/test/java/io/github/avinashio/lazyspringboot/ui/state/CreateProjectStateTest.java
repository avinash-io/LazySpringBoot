package io.github.avinashio.lazyspringboot.ui.state;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ConfigurationFileFormat;
import io.github.avinashio.lazyspringboot.domain.project.ProjectPackaging;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CreateProjectStateTest {

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

@Test
void shouldOpenFormAtBuildToolField() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	assertThat(state.active())
			.isTrue();
	
	assertThat(state.metadataStage())
			.isTrue();
	
	assertThat(state.selectedField())
			.isEqualTo(
					BUILD_TOOL_FIELD);
}

@Test
void shouldOpenWithDefaultProjectOptions() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	assertThat(state.packaging())
			.isEqualTo(
					ProjectPackaging.JAR);
	
	assertThat(state.configurationFileFormat())
			.isEqualTo(
					ConfigurationFileFormat.YAML);
}

@Test
void shouldNotNavigateToGenerateWhenProjectIsIncomplete() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	moveToField(
			state,
			DEPENDENCIES_FIELD);
	
	assertThat(state.readyToGenerate())
			.isFalse();
	
	state.nextField();
	
	assertThat(state.selectedField())
			.isEqualTo(
					DEPENDENCIES_FIELD);
}

@Test
void shouldNavigateToGenerateWhenProjectIsReady() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	state.setArtifactId(
			"demo");
	
	state.setPackageName(
			"com.example.demo");
	
	moveToField(
			state,
			DEPENDENCIES_FIELD);
	
	assertThat(state.readyToGenerate())
			.isTrue();
	
	state.nextField();
	
	assertThat(state.selectedField())
			.isEqualTo(
					GENERATE_FIELD);
	
	state.previousField();
	
	assertThat(state.selectedField())
			.isEqualTo(
					DEPENDENCIES_FIELD);
}

@Test
void shouldSwitchBetweenFormAndDependencyStages() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	moveToField(
			state,
			DEPENDENCIES_FIELD);
	
	state.showDependencyStage();
	
	assertThat(state.dependencyStage())
			.isTrue();
	
	state.showMetadataStage();
	
	assertThat(state.metadataStage())
			.isTrue();
	
	assertThat(state.selectedField())
			.isEqualTo(
					DEPENDENCIES_FIELD);
}

@Test
void shouldSynchronizeArtifactAndPackageFromName() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	moveToField(
			state,
			NAME_FIELD);
	
	editCurrentField(
			state,
			"my-awesome-app");
	
	assertThat(state.name())
			.isEqualTo(
					"my-awesome-app");
	
	assertThat(state.artifactId())
			.isEqualTo(
					"my-awesome-app");
	
	assertThat(state.packageName())
			.isEqualTo(
					"com.example.myawesomeapp");
}

@Test
void shouldNormalizeArtifactFromProjectName() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	moveToField(
			state,
			NAME_FIELD);
	
	editCurrentField(
			state,
			"My Awesome App");
	
	assertThat(state.artifactId())
			.isEqualTo(
					"my-awesome-app");
	
	assertThat(state.packageName())
			.isEqualTo(
					"com.example.myawesomeapp");
}

@Test
void shouldSynchronizePackageWhenGroupChanges() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	moveToField(
			state,
			NAME_FIELD);
	
	editCurrentField(
			state,
			"my-awesome-app");
	
	moveToField(
			state,
			GROUP_FIELD);
	
	editCurrentField(
			state,
			"io.github.avinashio");
	
	assertThat(state.groupId())
			.isEqualTo(
					"io.github.avinashio");
	
	assertThat(state.packageName())
			.isEqualTo(
					"io.github.avinashio.myawesomeapp");
}

@Test
void shouldPreserveManuallyEditedArtifact() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	moveToField(
			state,
			NAME_FIELD);
	
	editCurrentField(
			state,
			"first-app");
	
	moveToField(
			state,
			ARTIFACT_FIELD);
	
	editCurrentField(
			state,
			"custom-backend");
	
	moveToField(
			state,
			NAME_FIELD);
	
	editCurrentField(
			state,
			"second-app");
	
	assertThat(state.name())
			.isEqualTo(
					"second-app");
	
	assertThat(state.artifactId())
			.isEqualTo(
					"custom-backend");
}

@Test
void shouldPreserveManuallyEditedPackage() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	moveToField(
			state,
			NAME_FIELD);
	
	editCurrentField(
			state,
			"first-app");
	
	moveToField(
			state,
			PACKAGE_FIELD);
	
	editCurrentField(
			state,
			"io.github.custom.app");
	
	moveToField(
			state,
			NAME_FIELD);
	
	editCurrentField(
			state,
			"second-app");
	
	moveToField(
			state,
			GROUP_FIELD);
	
	editCurrentField(
			state,
			"org.example");
	
	assertThat(state.packageName())
			.isEqualTo(
					"io.github.custom.app");
}

@Test
void shouldResetProjectMetadataWhenFormReopens() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	state.setName(
			"first-app");
	
	state.setArtifactId(
			"first-app");
	
	state.setPackageName(
			"com.example.firstapp");
	
	state.selectDependency(
			"web");
	
	state.setPackaging(
			ProjectPackaging.WAR);
	
	state.setConfigurationFileFormat(
			ConfigurationFileFormat.PROPERTIES);
	
	state.close();
	state.open();
	
	assertThat(state.name())
			.isEmpty();
	
	assertThat(state.groupId())
			.isEqualTo(
					"com.example");
	
	assertThat(state.artifactId())
			.isEmpty();
	
	assertThat(state.packageName())
			.isEmpty();
	
	assertThat(state.selectedDependencies())
			.isEmpty();
	
	assertThat(state.packaging())
			.isEqualTo(
					ProjectPackaging.JAR);
	
	assertThat(state.configurationFileFormat())
			.isEqualTo(
					ConfigurationFileFormat.YAML);
	
	assertThat(state.selectedField())
			.isEqualTo(
					BUILD_TOOL_FIELD);
}

@Test
void shouldPreserveInitializrVersionsWhenFormReopens() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.setAvailableJavaVersions(
			List.of(
					"26",
					"21",
					"17"));
	
	state.setJavaVersion(
			"26");
	
	state.setAvailableSpringBootVersions(
			List.of(
					"4.1.0",
					"4.0.0"));
	
	state.setSpringBootVersion(
			"4.1.0");
	
	state.open();
	state.close();
	state.open();
	
	assertThat(state.availableJavaVersions())
			.containsExactly(
					"26",
					"21",
					"17");
	
	assertThat(state.javaVersion())
			.isEqualTo(
					"26");
	
	assertThat(
			state.availableSpringBootVersions())
			.containsExactly(
					"4.1.0",
					"4.0.0");
	
	assertThat(state.springBootVersion())
			.isEqualTo(
					"4.1.0");
}

@Test
void shouldSelectNextAndPreviousBuildToolInline() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	assertThat(state.buildTool())
			.isEqualTo(
					BuildTool.MAVEN);
	
	state.selectNextBuildToolInline();
	
	assertThat(state.buildTool())
			.isEqualTo(
					BuildTool.GRADLE);
	
	state.selectNextBuildToolInline();
	
	assertThat(state.buildTool())
			.isEqualTo(
					BuildTool.GRADLE_KOTLIN);
	
	state.selectNextBuildToolInline();
	
	assertThat(state.buildTool())
			.isEqualTo(
					BuildTool.GRADLE_KOTLIN);
	
	state.selectPreviousBuildToolInline();
	
	assertThat(state.buildTool())
			.isEqualTo(
					BuildTool.GRADLE);
}

@Test
void shouldSelectSpringBootVersionInline() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.setAvailableSpringBootVersions(
			List.of(
					"4.1.1-SNAPSHOT",
					"4.1.0",
					"4.0.8"));
	
	state.setSpringBootVersion(
			"4.1.0");
	
	state.open();
	
	moveToField(
			state,
			SPRING_BOOT_FIELD);
	
	state.selectNextVersionInline();
	
	assertThat(state.springBootVersion())
			.isEqualTo(
					"4.0.8");
	
	state.selectPreviousVersionInline();
	
	assertThat(state.springBootVersion())
			.isEqualTo(
					"4.1.0");
	
	state.selectPreviousVersionInline();
	
	assertThat(state.springBootVersion())
			.isEqualTo(
					"4.1.1-SNAPSHOT");
}

@Test
void shouldSelectPackagingInline() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	moveToField(
			state,
			PACKAGING_FIELD);
	
	assertThat(state.packaging())
			.isEqualTo(
					ProjectPackaging.JAR);
	
	state.selectNextPackagingInline();
	
	assertThat(state.packaging())
			.isEqualTo(
					ProjectPackaging.WAR);
	
	state.selectNextPackagingInline();
	
	assertThat(state.packaging())
			.isEqualTo(
					ProjectPackaging.WAR);
	
	state.selectPreviousPackagingInline();
	
	assertThat(state.packaging())
			.isEqualTo(
					ProjectPackaging.JAR);
}

@Test
void shouldSelectConfigurationFileFormatInline() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	moveToField(
			state,
			CONFIG_FIELD);
	
	assertThat(state.configurationFileFormat())
			.isEqualTo(
					ConfigurationFileFormat.YAML);
	
	state.selectPreviousConfigurationFileFormatInline();
	
	assertThat(state.configurationFileFormat())
			.isEqualTo(
					ConfigurationFileFormat.PROPERTIES);
	
	state.selectPreviousConfigurationFileFormatInline();
	
	assertThat(state.configurationFileFormat())
			.isEqualTo(
					ConfigurationFileFormat.PROPERTIES);
	
	state.selectNextConfigurationFileFormatInline();
	
	assertThat(state.configurationFileFormat())
			.isEqualTo(
					ConfigurationFileFormat.YAML);
}

@Test
void shouldSelectJavaVersionInline() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.setAvailableJavaVersions(
			List.of(
					"26",
					"25",
					"21",
					"17"));
	
	state.setJavaVersion(
			"21");
	
	state.open();
	
	moveToField(
			state,
			JAVA_FIELD);
	
	state.selectNextVersionInline();
	
	assertThat(state.javaVersion())
			.isEqualTo(
					"17");
	
	state.selectPreviousVersionInline();
	
	assertThat(state.javaVersion())
			.isEqualTo(
					"21");
	
	state.selectPreviousVersionInline();
	
	assertThat(state.javaVersion())
			.isEqualTo(
					"25");
}

@Test
void shouldNotChangeVersionFromNonVersionField() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.setAvailableJavaVersions(
			List.of(
					"26",
					"21",
					"17"));
	
	state.setJavaVersion(
			"21");
	
	state.open();
	
	moveToField(
			state,
			NAME_FIELD);
	
	state.selectNextVersionInline();
	
	assertThat(state.javaVersion())
			.isEqualTo(
					"21");
}

@Test
void shouldStoreDependencyCatalog() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	SpringDependency web =
			dependency(
					"web",
					"Spring Web",
					"Web");
	
	SpringDependency actuator =
			dependency(
					"actuator",
					"Spring Boot Actuator",
					"Ops");
	
	state.setDependencies(
			List.of(
					web,
					actuator));
	
	assertThat(state.dependencies())
			.containsExactly(
					web,
					actuator);
}

@Test
void shouldNavigateDependencies() {
	
	CreateProjectState state =
			stateWithDependencies();
	
	assertThat(
			state.selectedDependency()
					.id())
			.isEqualTo(
					"web");
	
	state.selectNextDependency();
	
	assertThat(
			state.selectedDependency()
					.id())
			.isEqualTo(
					"actuator");
	
	state.selectPreviousDependency();
	
	assertThat(
			state.selectedDependency()
					.id())
			.isEqualTo(
					"web");
}

@Test
void shouldToggleSelectedDependency() {
	
	CreateProjectState state =
			stateWithDependencies();
	
	state.toggleSelectedDependency();
	
	assertThat(
			state.selectedDependencies())
			.containsExactly(
					"web");
	
	assertThat(
			state.dependencySelected(
					"web"))
			.isTrue();
	
	state.toggleSelectedDependency();
	
	assertThat(
			state.selectedDependencies())
			.isEmpty();
}

@Test
void shouldFilterDependenciesBySearchQuery() {
	
	CreateProjectState state =
			stateWithDependencies();
	
	state.startDependencySearch();
	
	appendSearch(
			state,
			"jpa");
	
	assertThat(
			state.filteredDependencies())
			.extracting(
					SpringDependency::id)
			.containsExactly(
					"data-jpa");
}

@Test
void shouldFilterDependenciesByGroup() {
	
	CreateProjectState state =
			stateWithDependencies();
	
	state.startDependencySearch();
	
	appendSearch(
			state,
			"ops");
	
	assertThat(
			state.filteredDependencies())
			.extracting(
					SpringDependency::id)
			.containsExactly(
					"actuator");
}

@Test
void shouldToggleDependencyFromFilteredResults() {
	
	CreateProjectState state =
			stateWithDependencies();
	
	state.startDependencySearch();
	
	appendSearch(
			state,
			"jpa");
	
	state.toggleSelectedDependency();
	
	assertThat(
			state.selectedDependencies())
			.containsExactly(
					"data-jpa");
}

@Test
void shouldPreserveSelectedDependenciesWhenSearchCloses() {
	
	CreateProjectState state =
			stateWithDependencies();
	
	state.startDependencySearch();
	
	appendSearch(
			state,
			"web");
	
	state.toggleSelectedDependency();
	
	state.stopDependencySearch();
	
	assertThat(
			state.selectedDependencies())
			.containsExactly(
					"web");
	
	assertThat(
			state.dependencySearchQuery())
			.isEmpty();
	
	assertThat(
			state.dependencySearchActive())
			.isFalse();
}

@Test
void shouldRemoveLastDependencySearchCharacter() {
	
	CreateProjectState state =
			stateWithDependencies();
	
	state.startDependencySearch();
	
	appendSearch(
			state,
			"web");
	
	state.backspaceDependencySearch();
	
	assertThat(
			state.dependencySearchQuery())
			.isEqualTo(
					"we");
}

@Test
void shouldOpenWithFormPaneActive() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	assertThat(state.activePane())
			.isEqualTo(
					CreateProjectPane.FORM);
	
	assertThat(state.formPaneActive())
			.isTrue();
	
	assertThat(state.dependenciesPaneActive())
			.isFalse();
}

@Test
void shouldSwitchBetweenCreateProjectPanes() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	state.activateDependenciesPane();
	
	assertThat(state.dependenciesPaneActive())
			.isTrue();
	
	state.activateFormPane();
	
	assertThat(state.formPaneActive())
			.isTrue();
}

private CreateProjectState stateWithDependencies() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.setDependencies(
			List.of(
					dependency(
							"web",
							"Spring Web",
							"Web"),
					dependency(
							"actuator",
							"Spring Boot Actuator",
							"Ops"),
					dependency(
							"data-jpa",
							"Spring Data JPA",
							"SQL")));
	
	state.open();
	
	moveToField(
			state,
			DEPENDENCIES_FIELD);
	
	state.showDependencyStage();
	
	return state;
}

private SpringDependency dependency(
		String id,
		String name,
		String group) {
	
	return new SpringDependency(
			id,
			name,
			"Test dependency",
			group);
}

private void appendSearch(
		CreateProjectState state,
		String query) {
	
	for (char character :
			query.toCharArray()) {
		
		state.appendDependencySearch(
				character);
	}
}

private void editCurrentField(
		CreateProjectState state,
		String value) {
	
	state.startEditing();
	
	for (int index = 0;
		 index < 100;
		 index++) {
		
		state.backspace();
	}
	
	for (char character :
			value.toCharArray()) {
		
		state.append(
				character);
	}
	
	state.stopEditing();
}

private void moveToField(
		CreateProjectState state,
		int field) {
	
	while (state.selectedField()
				   < field) {
		
		state.nextField();
	}
	
	while (state.selectedField()
				   > field) {
		
		state.previousField();
	}
}
}