package io.github.avinashio.lazyspringboot.domain.environment;

public record EnvironmentInfo(

        String javaVersion,

        String mavenVersion,

        String gitVersion,

        boolean hasIntelliJ,

        boolean hasVSCode,

        String workspace) {
}