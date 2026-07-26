package io.github.avinashio.lazyspringboot.ui.state;

import org.springframework.stereotype.Component;

@Component
public class ProjectExplorerState {

    private boolean open;

    public boolean open() {

        return open;
    }

    public void openExplorer() {

        open = true;
    }

    public void close() {

        open = false;
    }
}