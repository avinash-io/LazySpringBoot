package io.github.avinashio.lazyspringboot.ui.component;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import org.springframework.stereotype.Component;

@Component
public class StatusFormatter {

    private final Spinner spinner;

    private final TerminalStyle terminalStyle;

    public StatusFormatter(
            Spinner spinner,
            TerminalStyle terminalStyle) {

        this.spinner = spinner;
        this.terminalStyle = terminalStyle;
    }

    public String format(
            ProjectProcessStatus status) {

        return icon(status)
                + " "
                + coloredLabel(status);
    }

    public String label(
            ProjectProcessStatus status) {

        return switch (status) {

            case STARTING -> "STARTING";
            case RUNNING -> "RUNNING";
            case STOPPED -> "STOPPED";
            case FAILED -> "FAILED";
        };
    }

    public String icon(
            ProjectProcessStatus status) {

        return switch (status) {

            case STARTING ->
                    "[" + spinner.nextFrame() + "]";

            case RUNNING ->
                    "[✓]";

            case STOPPED ->
                    "[ ]";

            case FAILED ->
                    "[✗]";
        };
    }

    public String stopped() {

        return format(ProjectProcessStatus.STOPPED);
    }

    private String coloredLabel(
            ProjectProcessStatus status) {

        String label = label(status);

        return switch (status) {

            case RUNNING ->
                    terminalStyle.running(label);

            case STARTING ->
                    terminalStyle.starting(label);

            case FAILED ->
                    terminalStyle.failed(label);

            case STOPPED ->
                    terminalStyle.stopped(label);
        };
    }
}