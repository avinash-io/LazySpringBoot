package io.github.avinashio.lazyspringboot.ui.form;

import java.util.List;

public class Form {

    private final List<FormField> fields;

    private int selectedField;

    private boolean editing;

    public Form(
            List<FormField> fields) {

        this.fields = fields;
    }

    public List<FormField> fields() {
        return fields;
    }

    public FormField selectedField() {
        return fields.get(selectedField);
    }

    public int selectedIndex() {
        return selectedField;
    }

    public boolean editing() {
        return editing;
    }

    public void startEditing() {
        editing = true;
    }

    public void stopEditing() {
        editing = false;
    }

    public void nextField() {

        if (selectedField
                < fields.size() - 1) {

            selectedField++;
        }
    }

    public void previousField() {

        if (selectedField > 0) {

            selectedField--;
        }
    }
}