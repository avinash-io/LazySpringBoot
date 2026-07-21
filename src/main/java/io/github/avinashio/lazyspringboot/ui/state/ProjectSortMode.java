package io.github.avinashio.lazyspringboot.ui.state;

public enum ProjectSortMode {

    NAME_ASC("Name ↑"),
    NAME_DESC("Name ↓"),
    PATH_ASC("Path ↑");

    private final String label;

    ProjectSortMode(
            String label) {

        this.label =
                label;
    }

    public String label() {
        return label;
    }

    public ProjectSortMode next() {

        return switch (this) {

            case NAME_ASC ->
                    NAME_DESC;

            case NAME_DESC ->
                    PATH_ASC;

            case PATH_ASC ->
                    NAME_ASC;
        };
    }
}