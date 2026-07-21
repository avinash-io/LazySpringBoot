package io.github.avinashio.lazyspringboot.ui.state;

import org.springframework.stereotype.Component;

@Component
public class ProjectSortState {

    private ProjectSortMode mode =
            ProjectSortMode.NAME_ASC;

    public ProjectSortMode mode() {
        return mode;
    }

    public void next() {

        mode =
                mode.next();
    }
}