package io.github.avinashio.lazyspringboot.domain.action;

public record ActionItem(
        ProjectAction action,
        boolean enabled) {}