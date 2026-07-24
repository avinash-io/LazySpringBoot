package io.github.avinashio.lazyspringboot.ui.runtime;

import io.github.avinashio.lazyspringboot.domain.process.ProjectProcess;
import io.github.avinashio.lazyspringboot.domain.process.ProjectProcessStatus;
import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class UptimeProvider {

    public String uptime(
            ProjectProcess process) {

        if (process.status()
                != ProjectProcessStatus.RUNNING) {

            return unavailable();
        }

        if (!process.hasStartTime()) {

            return unavailable();
        }

        Duration duration =
                Duration.between(
                        process.startedAt(),
                        Instant.now());

        long hours =
                duration.toHours();

        long minutes =
                duration.toMinutesPart();

        long seconds =
                duration.toSecondsPart();

        return String.format(
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds);
    }

    public String unavailable() {

        return "-";
    }
}