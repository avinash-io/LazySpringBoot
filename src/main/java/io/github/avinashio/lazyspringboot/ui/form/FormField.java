package io.github.avinashio.lazyspringboot.ui.form;

public class FormField {

    private final String label;

    private String value;

    public FormField(
            String label,
            String value) {

        this.label = label;
        this.value = value;
    }

    public String label() {
        return label;
    }

    public String value() {
        return value;
    }

    public void value(
            String value) {
        this.value = value;
    }
}
