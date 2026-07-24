package io.github.avinashio.lazyspringboot.ui.runtime;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import org.springframework.stereotype.Component;

@Component
public class PortProvider {

    private final SpringBootLogParser
            logParser;

    public PortProvider(
            SpringBootLogParser logParser) {

        this.logParser =
                logParser;
    }

    public String port(
            ProjectProcess process) {

        String port =
                logParser.findPort(
                        process.output());

        if (port != null) {

            return port;
        }

        return unavailable();
    }

    public String unavailable() {

        return "-";
    }
}