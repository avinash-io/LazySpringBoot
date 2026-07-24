package io.github.avinashio.lazyspringboot.ui.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpringBootLogParserTest {

    private SpringBootLogParser
            parser;

    @BeforeEach
    void setUp() {

        parser =
                new SpringBootLogParser();
    }

    @Test
    void shouldFindTomcatPort() {

        assertThat(
                parser.findPort(
                        List.of(
                                "Tomcat started on port(s): 8080 (http)")))
                .isEqualTo(
                        "8080");
    }

    @Test
    void shouldFindNettyPort() {

        assertThat(
                parser.findPort(
                        List.of(
                                "Netty started on port 9090")))
                .isEqualTo(
                        "9090");
    }

    @Test
    void shouldReturnNullWhenNoPortExists() {

        assertThat(
                parser.findPort(
                        List.of(
                                "Application started.")))
                .isNull();
    }

    @Test
    void shouldReturnNullForEmptyOutput() {

        assertThat(
                parser.findPort(
                        List.of()))
                .isNull();
    }

    @Test
    void shouldFindPortNearEndOfOutput() {

        assertThat(
                parser.findPort(
                        List.of(
                                "Line 1",
                                "Line 2",
                                "Line 3",
                                "Tomcat started on port(s): 8081 (http)",
                                "Finished")))
                .isEqualTo(
                        "8081");
    }

    @Test
    void shouldParseTypicalSpringBootTomcatStartup() {

        assertThat(
                parser.findPort(
                        List.of(
                                "2026-07-24T10:15:41.123+05:30  INFO 12345 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat started on port(s): 8080 (http) with context path ''",
                                "2026-07-24T10:15:41.456+05:30  INFO 12345 --- [main] com.example.Application : Started Application in 2.341 seconds")))
                .isEqualTo(
                        "8080");
    }

    @Test
    void shouldFindTomcatInitializedPort() {

        assertThat(
                parser.findPort(
                        List.of(
                                "2026-07-24T09:55:56.297+05:30  INFO 48917 --- [test-apple] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat initialized with port 8080 (http)")))
                .isEqualTo(
                        "8080");
    }
}