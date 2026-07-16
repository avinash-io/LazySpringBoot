package io.github.avinashio.lazyspringboot.domain.process;

public record ProcessMetrics(
        long memoryBytes,
        double cpuLoad,
        int threadCount) {

    public boolean available() {
        return memoryBytes >= 0
                && cpuLoad >= 0
                && threadCount >= 0;
    }
}