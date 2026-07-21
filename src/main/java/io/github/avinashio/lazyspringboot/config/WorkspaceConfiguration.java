package io.github.avinashio.lazyspringboot.config;

import io.github.avinashio.lazyspringboot.ui.state.ProjectSortMode;

public record WorkspaceConfiguration(
        String workspace,
        ProjectSortMode projectSortMode) {

    public static WorkspaceConfiguration defaultConfiguration(
            String workspace) {

        return new WorkspaceConfiguration(
                workspace,
                ProjectSortMode.NAME_ASC);
    }
}