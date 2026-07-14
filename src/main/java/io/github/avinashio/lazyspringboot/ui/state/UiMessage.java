package io.github.avinashio.lazyspringboot.ui.state;

public record UiMessage(
        UiMessageType type,
        String text) {}