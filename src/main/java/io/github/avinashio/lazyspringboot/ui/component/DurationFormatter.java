package io.github.avinashio.lazyspringboot.ui.component;

import java.time.Duration;
import org.springframework.stereotype.Component;

@Component
public class DurationFormatter {

    public String format(
            Duration duration) {

        long totalSeconds =
                duration.getSeconds();

        long hours =
                totalSeconds / 3600;

        long minutes =
                (totalSeconds % 3600) / 60;

        long seconds =
                totalSeconds % 60;

        return "%02d:%02d:%02d".formatted(
                hours,
                minutes,
                seconds);
    }
}