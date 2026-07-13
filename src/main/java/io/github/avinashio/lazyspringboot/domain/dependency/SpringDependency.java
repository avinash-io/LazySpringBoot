package io.github.avinashio.lazyspringboot.domain.dependency;

public record SpringDependency(
        String id,
        String name,
        String description,
        String group) {}