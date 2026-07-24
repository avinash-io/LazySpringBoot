package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import org.springframework.stereotype.Component;

@Component
public class StatusFormatter {

    private final Spinner spinner;

    public StatusFormatter(
            Spinner spinner) {

        this.spinner =
                spinner;
    }

    public String format(
            ProjectProcessStatus status) {

        return icon(status)
                + " "
                + label(status);
    }

    public String label(
            ProjectProcessStatus status) {

        return switch (status) {

            case STARTING ->
                    "STARTING";

            case RUNNING ->
                    "RUNNING";

            case STOPPED ->
                    "STOPPED";

            case FAILED ->
                    "FAILED";
        };
    }

    public String icon(
            ProjectProcessStatus status) {

        return switch (status) {

            case STARTING ->
                    "["
                            + spinner.nextFrame()
                            + "]";

            case RUNNING ->
                    "[✓]";

            case STOPPED ->
                    "[ ]";

            case FAILED ->
                    "[✗]";
        };
    }

    public String stopped() {

        return format(
                ProjectProcessStatus.STOPPED);
    }
}