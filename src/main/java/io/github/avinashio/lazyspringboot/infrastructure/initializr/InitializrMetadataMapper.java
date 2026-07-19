package io.github.avinashio.lazyspringboot.infrastructure.initializr;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.domain.initializr.InitializrConfiguration;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class InitializrMetadataMapper {

    public InitializrConfiguration map(
            InitializrMetadata metadata) {

        return new InitializrConfiguration(
                mapDependencies(
                        metadata),
                mapOptions(
                        metadata.javaVersion()),
                metadata.javaVersion()
                        .defaultValue(),
                mapOptions(
                        metadata.bootVersion()),
                metadata.bootVersion()
                        .defaultValue());
    }

    private List<SpringDependency> mapDependencies(
            InitializrMetadata metadata) {

        return metadata.dependencies()
                .values()
                .stream()
                .flatMap(
                        group ->
                                group.values()
                                        .stream()
                                        .map(
                                                dependency ->
                                                        new SpringDependency(
                                                                dependency.id(),
                                                                dependency.name(),
                                                                dependency.description(),
                                                                group.name())))
                .toList();
    }

    private List<String> mapOptions(
            InitializrOptionMetadata metadata) {

        return metadata.values()
                .stream()
                .map(
                        InitializrOption::id)
                .toList();
    }
}