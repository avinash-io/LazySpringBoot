package io.github.avinashio.lazyspringboot.ui.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PortProviderTest {

    private PortProvider
            portProvider;

    @BeforeEach
    void setUp() {

        portProvider =
                new PortProvider(
                        new SpringBootLogParser());
    }

    @Test
    void shouldExtractTomcatPort() {

        ProjectProcess process =
                process(
                        List.of(
                                "Starting application...",
                                "Tomcat started on port(s): 8080 (http)"));

        assertThat(
                portProvider.port(
                        process))
                .isEqualTo(
                        "8080");
    }

    @Test
    void shouldExtractNettyPort() {

        ProjectProcess process =
                process(
                        List.of(
                                "Booting...",
                                "Netty started on port 9090"));

        assertThat(
                portProvider.port(
                        process))
                .isEqualTo(
                        "9090");
    }

    @Test
    void shouldReturnUnavailableWhenPortNotFound() {

        ProjectProcess process =
                process(
                        List.of(
                                "Application started successfully."));

        assertThat(
                portProvider.port(
                        process))
                .isEqualTo(
                        "-");
    }

    @Test
    void shouldReturnUnavailableForEmptyOutput() {

        ProjectProcess process =
                process(
                        List.of());

        assertThat(
                portProvider.port(
                        process))
                .isEqualTo(
                        "-");
    }

    @Test
    void shouldFindPortNearEndOfOutput() {

        ProjectProcess process =
                process(
                        List.of(
                                "Line 1",
                                "Line 2",
                                "Line 3",
                                "Line 4",
                                "Tomcat started on port(s): 8081 (http)",
                                "Finished"));

        assertThat(
                portProvider.port(
                        process))
                .isEqualTo(
                        "8081");
    }

    private ProjectProcess process(
            List<String> output) {

        return new ProjectProcess(
                "demo",
                ProjectProcessStatus.RUNNING,
                output,
                null,
                12345L,
                Instant.now(),
                null);
    }
}