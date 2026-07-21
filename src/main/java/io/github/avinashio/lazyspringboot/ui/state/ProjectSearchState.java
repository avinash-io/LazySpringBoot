package io.github.avinashio.lazyspringboot.ui.state;

import org.springframework.stereotype.Component;

@Component
public class ProjectSearchState {

    private boolean active;

    private String query = "";

    public boolean active() {
        return active;
    }

    public String query() {
        return query;
    }

    public void start() {
        active = true;
        query = "";
    }

    public void stop() {
        active = false;
        query = "";
    }

    public void append(char character) {
        if (!active) {
            return;
        }

        query += character;
    }

    public void backspace() {
        if (!active || query.isEmpty()) {
            return;
        }

        query =
                query.substring(
                        0,
                        query.length() - 1);
    }

    public boolean hasQuery() {
        return !query.isBlank();
    }
}