package io.github.avinashio.lazyspringboot.ui.runtime;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class SpringBootLogParser {

    private static final Pattern
            TOMCAT_STARTED_PATTERN =
            Pattern.compile(
                    "Tomcat started on port\\(s\\):\\s*(\\d+)");

    private static final Pattern
            TOMCAT_INITIALIZED_PATTERN =
            Pattern.compile(
                    "Tomcat initialized with port\\s+(\\d+)");

    private static final Pattern
            NETTY_PATTERN =
            Pattern.compile(
                    "Netty started on port\\s*(\\d+)");

    private static final List<Pattern>
            PORT_PATTERNS =
            List.of(
                    TOMCAT_STARTED_PATTERN,
                    TOMCAT_INITIALIZED_PATTERN,
                    NETTY_PATTERN);

    public String findPort(
            List<String> output) {

        for (String line : output) {

            String port =
                    findPort(
                            line);

            if (port != null) {

                return port;
            }
        }

        return null;
    }

    private String findPort(
            String line) {

        for (Pattern pattern :
                PORT_PATTERNS) {

            Matcher matcher =
                    pattern.matcher(
                            line);

            if (matcher.find()) {

                return matcher.group(
                        1);
            }
        }

        return null;
    }
}