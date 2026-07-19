package io.github.avinashio.lazyspringboot.ui.state;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import java.util.List;
import org.junit.jupiter.api.Test;

class CreateProjectStateTest {

    @Test
    void shouldOpenWizardAtMetadataStage() {

        CreateProjectState state =
                new CreateProjectState();

        state.open();

        assertThat(state.active())
                .isTrue();

        assertThat(state.metadataStage())
                .isTrue();

        assertThat(state.selectedField())
                .isZero();
    }

    @Test
    void shouldNavigateMetadataFields() {

        CreateProjectState state =
                new CreateProjectState();

        state.open();

        state.nextField();
        state.nextField();

        assertThat(state.selectedField())
                .isEqualTo(2);

        state.previousField();

        assertThat(state.selectedField())
                .isEqualTo(1);
    }

    @Test
    void shouldSwitchBetweenMetadataAndDependencyStages() {

        CreateProjectState state =
                new CreateProjectState();

        state.open();

        state.showDependencyStage();

        assertThat(state.dependencyStage())
                .isTrue();

        state.showMetadataStage();

        assertThat(state.metadataStage())
                .isTrue();
    }

    @Test
    void shouldSynchronizeArtifactAndPackageFromName() {

        CreateProjectState state =
                new CreateProjectState();

        state.open();

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

        editCurrentField(
                state,
                "my-awesome-app");

        state.nextField();

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

        editCurrentField(
                state,
                "first-app");

        state.nextField();
        state.nextField();

        editCurrentField(
                state,
                "custom-backend");

        state.previousField();
        state.previousField();

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

        editCurrentField(
                state,
                "first-app");

        state.nextField();
        state.nextField();
        state.nextField();

        editCurrentField(
                state,
                "io.github.custom.app");

        state.previousField();
        state.previousField();
        state.previousField();

        editCurrentField(
                state,
                "second-app");

        state.nextField();

        editCurrentField(
                state,
                "org.example");

        assertThat(state.packageName())
                .isEqualTo(
                        "io.github.custom.app");
    }

    @Test
    void shouldResetProjectMetadataWhenWizardReopens() {

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
    }

    @Test
    void shouldPreserveInitializrVersionsWhenWizardReopens() {

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
    void shouldSelectJavaVersion() {

        CreateProjectState state =
                new CreateProjectState();

        state.setAvailableJavaVersions(
                List.of(
                        "26",
                        "21",
                        "17"));

        state.setJavaVersion(
                "26");

        state.open();

        moveToField(
                state,
                4);

        state.startVersionSelection();

        assertThat(state.versionSelecting())
                .isTrue();

        assertThat(state.selectedVersionIndex())
                .isZero();

        state.selectNextVersion();

        state.confirmVersionSelection();

        assertThat(state.javaVersion())
                .isEqualTo(
                        "21");

        assertThat(state.versionSelecting())
                .isFalse();
    }

    @Test
    void shouldSelectSpringBootVersion() {

        CreateProjectState state =
                new CreateProjectState();

        state.setAvailableSpringBootVersions(
                List.of(
                        "4.1.0",
                        "4.0.0",
                        "3.5.0"));

        state.setSpringBootVersion(
                "4.1.0");

        state.open();

        moveToField(
                state,
                5);

        state.startVersionSelection();

        assertThat(state.versionSelecting())
                .isTrue();

        state.selectNextVersion();

        state.confirmVersionSelection();

        assertThat(state.springBootVersion())
                .isEqualTo(
                        "4.0.0");

        assertThat(state.versionSelecting())
                .isFalse();
    }

    @Test
    void shouldCancelVersionSelectionWithoutChangingVersion() {

        CreateProjectState state =
                new CreateProjectState();

        state.setAvailableJavaVersions(
                List.of(
                        "26",
                        "21"));

        state.setJavaVersion(
                "26");

        state.open();

        moveToField(
                state,
                4);

        state.startVersionSelection();

        state.selectNextVersion();

        state.stopVersionSelection();

        assertThat(state.javaVersion())
                .isEqualTo(
                        "26");

        assertThat(state.versionSelecting())
                .isFalse();
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

    private CreateProjectState
    stateWithDependencies() {

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
    }
}