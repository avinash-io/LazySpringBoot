package io.github.avinashio.lazyspringboot.config;

public record WorkspaceConfiguration(
        String workspace) {

    public static WorkspaceConfiguration defaultConfiguration(
            String workspace) {

        return new WorkspaceConfiguration(
                workspace);
    }
}