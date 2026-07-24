package io.github.avinashio.lazyspringboot.ui.model;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;

public record ProjectRuntimeInfo(
        ProjectProcessStatus status,
        String port,
        String uptime) {
}