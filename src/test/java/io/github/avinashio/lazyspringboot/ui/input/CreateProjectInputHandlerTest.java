package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.ConfigurationFileFormat;
import io.github.avinashio.lazyspringboot.domain.project.ProjectPackaging;
import io.github.avinashio.lazyspringboot.ui.controller.CreateProjectController;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CreateProjectInputHandlerTest {

private static final int GROUP_FIELD = 2;
private static final int PACKAGING_FIELD = 6;
private static final int CONFIG_FIELD = 7;
private static final int JAVA_FIELD = 8;
private static final int DEPENDENCIES_FIELD = 9;
private static final int GENERATE_FIELD = 10;

@Test
void shouldIgnoreInputWhenCreateProjectIsNotActive() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	CreateProjectController controller =
			mock(
					CreateProjectController.class);
	
	when(controller.state())
			.thenReturn(
					state);
	
	CreateProjectInputHandler handler =
			new CreateProjectInputHandler(
					controller);
	
	boolean handled =
			handler.handle(
					KeyEvent.of(
							KeyType.DOWN));
	
	assertThat(handled)
			.isFalse();
}

@Test
void shouldNavigateFormRows() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	handler.handle(
			KeyEvent.of(
					KeyType.DOWN));
	
	assertThat(
			state.selectedField())
			.isEqualTo(1);
	
	handler.handle(
			KeyEvent.of(
					KeyType.UP));
	
	assertThat(
			state.selectedField())
			.isZero();
}

@Test
void shouldChangeBuildToolInline() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	assertThat(
			state.buildTool())
			.isEqualTo(
					BuildTool.MAVEN);
	
	handler.handle(
			KeyEvent.of(
					KeyType.RIGHT));
	
	assertThat(
			state.buildTool())
			.isEqualTo(
					BuildTool.GRADLE);
	
	handler.handle(
			KeyEvent.of(
					KeyType.RIGHT));
	
	assertThat(
			state.buildTool())
			.isEqualTo(
					BuildTool.GRADLE_KOTLIN);
	
	handler.handle(
			KeyEvent.of(
					KeyType.LEFT));
	
	assertThat(
			state.buildTool())
			.isEqualTo(
					BuildTool.GRADLE);
}

@Test
void shouldChangeSpringBootVersionInline() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.setAvailableSpringBootVersions(
			List.of(
					"4.1.1-SNAPSHOT",
					"4.1.0",
					"4.0.8"));
	
	state.open();
	
	state.nextField();
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	assertThat(
			state.springBootVersion())
			.isEqualTo(
					"4.1.0");
	
	handler.handle(
			KeyEvent.of(
					KeyType.RIGHT));
	
	assertThat(
			state.springBootVersion())
			.isEqualTo(
					"4.0.8");
	
	handler.handle(
			KeyEvent.of(
					KeyType.LEFT));
	
	assertThat(
			state.springBootVersion())
			.isEqualTo(
					"4.1.0");
}

@Test
void shouldChangePackagingInline() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	moveToField(
			state,
			PACKAGING_FIELD);
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	assertThat(state.packaging())
			.isEqualTo(
					ProjectPackaging.JAR);
	
	handler.handle(
			KeyEvent.of(
					KeyType.RIGHT));
	
	assertThat(state.packaging())
			.isEqualTo(
					ProjectPackaging.WAR);
	
	handler.handle(
			KeyEvent.of(
					KeyType.LEFT));
	
	assertThat(state.packaging())
			.isEqualTo(
					ProjectPackaging.JAR);
}

@Test
void shouldChangeConfigurationFileFormatInline() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	moveToField(
			state,
			CONFIG_FIELD);
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	assertThat(state.configurationFileFormat())
			.isEqualTo(
					ConfigurationFileFormat.YAML);
	
	handler.handle(
			KeyEvent.of(
					KeyType.LEFT));
	
	assertThat(state.configurationFileFormat())
			.isEqualTo(
					ConfigurationFileFormat.PROPERTIES);
	
	handler.handle(
			KeyEvent.of(
					KeyType.RIGHT));
	
	assertThat(state.configurationFileFormat())
			.isEqualTo(
					ConfigurationFileFormat.YAML);
}

@Test
void shouldChangeJavaVersionInline() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.setAvailableJavaVersions(
			List.of(
					"26",
					"25",
					"21",
					"17"));
	
	state.open();
	
	moveToField(
			state,
			JAVA_FIELD);
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	handler.handle(
			KeyEvent.of(
					KeyType.RIGHT));
	
	assertThat(
			state.javaVersion())
			.isEqualTo(
					"17");
	
	handler.handle(
			KeyEvent.of(
					KeyType.LEFT));
	
	assertThat(
			state.javaVersion())
			.isEqualTo(
					"21");
}

@Test
void shouldStartEditingWithEnterOnTextField() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	moveToField(
			state,
			GROUP_FIELD);
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	handler.handle(
			KeyEvent.of(
					KeyType.ENTER));
	
	assertThat(
			state.editing())
			.isTrue();
}

@Test
void shouldNotStartEditingOnChoiceField() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	handler.handle(
			KeyEvent.of(
					KeyType.ENTER));
	
	assertThat(
			state.editing())
			.isFalse();
}

@Test
void shouldOpenDependenciesFromDependenciesRow() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	moveToField(
			state,
			DEPENDENCIES_FIELD);
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	handler.handle(
			KeyEvent.of(
					KeyType.ENTER));
	
	assertThat(
			state.dependenciesPaneActive())
			.isTrue();
}

@Test
void shouldNotContinueToDependenciesWithTab() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	CreateProjectController controller =
			mock(
					CreateProjectController.class);
	
	when(controller.state())
			.thenReturn(
					state);
	
	CreateProjectInputHandler handler =
			new CreateProjectInputHandler(
					controller);
	
	handler.handle(
			KeyEvent.of(
					KeyType.TAB));
	
	verify(
			controller,
			never())
			.continueToDependencies();
}

@Test
void shouldGenerateProjectFromGenerateRowWhenProjectIsReady() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	state.setArtifactId(
			"demo");
	
	state.setPackageName(
			"com.example.demo");
	
	moveToField(
			state,
			GENERATE_FIELD);
	
	assertThat(state.readyToGenerate())
			.isTrue();
	
	assertThat(state.selectedField())
			.isEqualTo(
					GENERATE_FIELD);
	
	CreateProjectController controller =
			mock(
					CreateProjectController.class);
	
	when(controller.state())
			.thenReturn(
					state);
	
	CreateProjectInputHandler handler =
			new CreateProjectInputHandler(
					controller);
	
	handler.handle(
			KeyEvent.of(
					KeyType.ENTER));
	
	verify(controller)
			.generate(
					Path.of("")
							.toAbsolutePath());
}

@Test
void shouldNotReachGenerateRowWhenProjectIsIncomplete() {
	
	CreateProjectState state =
			new CreateProjectState();
	
	state.open();
	
	moveToField(
			state,
			DEPENDENCIES_FIELD);
	
	assertThat(state.readyToGenerate())
			.isFalse();
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	handler.handle(
			KeyEvent.of(
					KeyType.DOWN));
	
	assertThat(state.selectedField())
			.isEqualTo(
					DEPENDENCIES_FIELD);
}

@Test
void shouldNavigateDependencies() {
	
	CreateProjectState state =
			stateWithDependencies();
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	handler.handle(
			KeyEvent.of(
					KeyType.DOWN));
	
	assertThat(
			state.selectedDependency()
					.id())
			.isEqualTo(
					"actuator");
	
	handler.handle(
			KeyEvent.of(
					KeyType.UP));
	
	assertThat(
			state.selectedDependency()
					.id())
			.isEqualTo(
					"web");
}

@Test
void shouldToggleDependencyWithSpace() {
	
	CreateProjectState state =
			stateWithDependencies();
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	handler.handle(
			KeyEvent.of(
					KeyType.SPACE));
	
	assertThat(
			state.selectedDependencies())
			.containsExactly(
					"web");
}

@Test
void shouldStartDependencySearch() {
	
	CreateProjectState state =
			stateWithDependencies();
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	handler.handle(
			KeyEvent.of(
					KeyType.SEARCH));
	
	assertThat(
			state.dependencySearchActive())
			.isTrue();
}

@Test
void shouldToggleFilteredDependencyWithSpace() {
	
	CreateProjectState state =
			stateWithDependencies();
	
	state.startDependencySearch();
	
	appendSearch(
			state,
			"jpa");
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	handler.handle(
			KeyEvent.of(
					KeyType.SPACE));
	
	assertThat(
			state.selectedDependencies())
			.containsExactly(
					"data-jpa");
	
	assertThat(
			state.dependencySearchQuery())
			.isEqualTo(
					"jpa");
}

@Test
void shouldToggleFilteredDependencyWhenSpaceIsCharacter() {
	
	CreateProjectState state =
			stateWithDependencies();
	
	state.startDependencySearch();
	
	appendSearch(
			state,
			"jpa");
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	handler.handle(
			KeyEvent.character(
					' '));
	
	assertThat(
			state.selectedDependencies())
			.containsExactly(
					"data-jpa");
	
	assertThat(
			state.dependencySearchQuery())
			.isEqualTo(
					"jpa");
}

@Test
void shouldToggleFilteredDependencyWithEnter() {
	
	CreateProjectState state =
			stateWithDependencies();
	
	state.startDependencySearch();
	
	appendSearch(
			state,
			"actuator");
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	handler.handle(
			KeyEvent.of(
					KeyType.ENTER));
	
	assertThat(
			state.selectedDependencies())
			.containsExactly(
					"actuator");
}

@Test
void shouldCloseDependencySearchWithEscape() {
	
	CreateProjectState state =
			stateWithDependencies();
	
	state.startDependencySearch();
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	handler.handle(
			KeyEvent.of(
					KeyType.ESCAPE));
	
	assertThat(
			state.dependencySearchActive())
			.isFalse();
	
	assertThat(
			state.dependenciesPaneActive())
			.isTrue();
}

@Test
void shouldReturnToFormWithEscapeFromDependencies() {
	
	CreateProjectState state =
			stateWithDependencies();
	
	assertThat(
			state.dependenciesPaneActive())
			.isTrue();
	
	CreateProjectInputHandler handler =
			createHandler(
					state);
	
	handler.handle(
			KeyEvent.of(
					KeyType.ESCAPE));
	
	assertThat(
			state.dependenciesPaneActive())
			.isFalse();
	
	assertThat(
			state.formPaneActive())
			.isTrue();
}

@Test
void shouldNotGenerateWithEnterFromDependencyStage() {
	
	CreateProjectState state =
			stateWithDependencies();
	
	CreateProjectController controller =
			mock(
					CreateProjectController.class);
	
	when(controller.state())
			.thenReturn(
					state);
	
	CreateProjectInputHandler handler =
			new CreateProjectInputHandler(
					controller);
	
	handler.handle(
			KeyEvent.of(
					KeyType.ENTER));
	
	verify(
			controller,
			never())
			.generate(
					Path.of("")
							.toAbsolutePath());
}

private CreateProjectInputHandler createHandler(
		CreateProjectState state) {
	
	CreateProjectController controller =
			mock(
					CreateProjectController.class);
	
	when(controller.state())
			.thenReturn(
					state);
	
	return new CreateProjectInputHandler(
			controller);
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
	
	state.activateDependenciesPane();
	
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

private void moveToField(
		CreateProjectState state,
		int field) {
	
	while (state.selectedField()
				   < field) {
		
		int previousField =
				state.selectedField();
		
		state.nextField();
		
		if (state.selectedField()
					== previousField) {
			
			break;
		}
	}
	
	while (state.selectedField()
				   > field) {
		
		state.previousField();
	}
}
}