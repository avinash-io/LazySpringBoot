package io.github.avinashio.lazyspringboot.ui.state;

import org.springframework.stereotype.Component;

@Component
public class ProjectManagerState {

    private boolean open;

    public boolean isOpen() {

        return open;
    }

    public void open() {

        open = true;
    }

    public void close() {

        open = false;
    }
}