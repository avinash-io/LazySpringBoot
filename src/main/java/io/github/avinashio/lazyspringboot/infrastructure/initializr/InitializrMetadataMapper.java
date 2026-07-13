package io.github.avinashio.lazyspringboot.infrastructure.initializr;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class InitializrMetadataMapper {

    public List<SpringDependency> map(
            InitializrMetadata metadata) {
        return metadata.dependencies().values().stream()
                .flatMap(
                        group ->
                                group.values().stream()
                                        .map(
                                                dependency ->
                                                        new SpringDependency(
                                                                dependency.id(),
                                                                dependency.name(),
                                                                dependency.description(),
                                                                group.name())))
                .toList();
    }
}