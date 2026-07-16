package io.github.avinashio.lazyspringboot.ui.state;

import io.github.avinashio.lazyspringboot.ui.form.Form;
import io.github.avinashio.lazyspringboot.ui.form.FormField;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CreateProjectState {

    private boolean active;

    private int selectedField;

    private String name = "";

    private String groupId = "com.example";

    private String artifactId = "";

    private String packageName = "";

    private String javaVersion = "21";

    private String springBootVersion = "4.1.0";

    private boolean editing;

    private final StringBuilder
            inputBuffer =
            new StringBuilder();


    public boolean active() {
        return active;
    }

    public void open() {
        active = true;
        selectedField = 0;
    }

    public void close() {
        active = false;
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
        this.springBootVersion = springBootVersion;
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

        inputBuffer.append(character);

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

            case 0 -> name = value;

            case 1 -> groupId = value;

            case 2 -> artifactId = value;

            case 3 -> packageName = value;

            case 4 -> javaVersion = value;

            case 5 -> springBootVersion = value;

            default -> {
            }
        }
    }




}