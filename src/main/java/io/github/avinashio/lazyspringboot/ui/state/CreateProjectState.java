package io.github.avinashio.lazyspringboot.ui.state;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class CreateProjectState {

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

    private final StringBuilder inputBuffer =
            new StringBuilder();

    private List<SpringDependency> dependencies =
            List.of();

    private final Set<String>
            selectedDependencies =
            new LinkedHashSet<>();

    private int selectedDependencyIndex;

    private String dependencySearchQuery =
            "";

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

        selectedField = 0;

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

        if (selectedField < 5) {
            selectedField++;
        }
    }

    public void previousField() {

        if (selectedField > 0) {
            selectedField--;
        }
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
                        dependencySearchQuery.length()
                                - 1);

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

            case 0 -> name;

            case 1 -> groupId;

            case 2 -> artifactId;

            case 3 -> packageName;

            case 4 -> javaVersion;

            case 5 -> springBootVersion;

            default -> "";
        };
    }

    private void updateCurrentField() {

        String value =
                inputBuffer.toString();

        switch (selectedField) {

            case 0 -> {

                name = value;

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

            case 1 -> {

                groupId = value;

                if (!packageManuallyEdited) {

                    packageName =
                            buildPackageName();
                }
            }

            case 2 -> {

                artifactId = value;

                artifactManuallyEdited = true;
            }

            case 3 -> {

                packageName = value;

                packageManuallyEdited = true;
            }

            case 4 ->
                    javaVersion = value;

            case 5 ->
                    springBootVersion = value;

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

        versionSelecting = true;

        List<String> versions =
                currentVersionOptions();

        String currentVersion =
                selectedField == 4
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

        versionSelecting = false;
    }

    public int selectedVersionIndex() {

        return selectedVersionIndex;
    }

    public List<String> currentVersionOptions() {

        if (selectedField == 4) {
            return availableJavaVersions;
        }

        if (selectedField == 5) {
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
            versionSelecting = false;
            return;
        }

        String selectedVersion =
                versions.get(
                        selectedVersionIndex);

        if (selectedField == 4) {

            javaVersion =
                    selectedVersion;

        } else if (selectedField == 5) {

            springBootVersion =
                    selectedVersion;
        }

        versionSelecting = false;
    }
}