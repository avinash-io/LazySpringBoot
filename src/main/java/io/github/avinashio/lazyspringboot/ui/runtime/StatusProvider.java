package io.github.avinashio.lazyspringboot.ui.runtime;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import org.springframework.stereotype.Component;

@Component
public class StatusProvider {

    public ProjectProcessStatus status(
            ProjectProcess process) {

        return process.status();
    }

    public ProjectProcessStatus stopped() {

        return ProjectProcessStatus.STOPPED;
    }
}