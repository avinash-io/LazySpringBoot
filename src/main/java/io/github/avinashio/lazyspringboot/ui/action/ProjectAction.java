package io.github.avinashio.lazyspringboot.ui.action;

public record ProjectAction(

        ProjectCommand type,

        boolean enabled) {
}