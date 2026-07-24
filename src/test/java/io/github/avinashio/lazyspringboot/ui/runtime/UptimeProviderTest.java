package io.github.avinashio.lazyspringboot.ui.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UptimeProviderTest {

    private UptimeProvider
            uptimeProvider;

    @BeforeEach
    void setUp() {

        uptimeProvider =
                new UptimeProvider();
    }

    @Test
    void shouldReturnUnavailableWhenStartTimeMissing() {

        assertThat(
                uptimeProvider.uptime(
                        process(
                                null)))
                .isEqualTo(
                        "-");
    }

    @Test
    void shouldReturnUnavailableConstant() {

        assertThat(
                uptimeProvider.unavailable())
                .isEqualTo(
                        "-");
    }

    @Test
    void shouldFormatElapsedTime() {

        Instant startedAt =
                Instant.now()
                        .minusSeconds(
                                3661);

        String uptime =
                uptimeProvider.uptime(
                        process(
                                startedAt));

        assertThat(uptime)
                .matches(
                        "\\d{2}:\\d{2}:\\d{2}");
    }

    private ProjectProcess process(
            Instant startedAt) {

        return new ProjectProcess(
                "demo",
                ProjectProcessStatus.RUNNING,
                List.of(),
                null,
                123L,
                startedAt,
                null);
    }
}