package io.github.avinashio.lazyspringboot.domain.actuator;

import java.util.List;

public record ActuatorInfo(
        String health,
        String applicationName,
        String springBootVersion,
        List<String> activeProfiles,
        Long heapUsedBytes,
        Integer liveThreads) {

    public boolean available() {
        return health != null;
    }
}