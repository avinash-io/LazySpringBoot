package io.github.avinashio.lazyspringboot.application.workspace;

public class InvalidWorkspaceException
        extends Exception {

    public InvalidWorkspaceException(
            String message) {

        super(message);
    }
}