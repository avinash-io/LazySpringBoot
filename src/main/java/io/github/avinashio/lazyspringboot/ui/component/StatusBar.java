package io.github.avinashio.lazyspringboot.ui.component;

import org.springframework.stereotype.Component;

@Component
public class StatusBar {

    public String render() {
        return " ↑↓ Navigate    q Quit";
    }
}