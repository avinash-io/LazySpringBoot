package io.github.avinashio.lazyspringboot.application.dependency;

import io.github.avinashio.lazyspringboot.domain.dependency.SpringDependency;
import io.github.avinashio.lazyspringboot.infrastructure.initializr.InitializrMetadataMapper;
import io.github.avinashio.lazyspringboot.infrastructure.initializr.SpringInitializrClient;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GetDependenciesUseCase {

    private final SpringInitializrClient springInitializrClient;
    private final InitializrMetadataMapper metadataMapper;

    public GetDependenciesUseCase(
            SpringInitializrClient springInitializrClient,
            InitializrMetadataMapper metadataMapper) {
        this.springInitializrClient = springInitializrClient;
        this.metadataMapper = metadataMapper;
    }

    public List<SpringDependency> getDependencies()
            throws IOException, InterruptedException {
        return metadataMapper.map(
                springInitializrClient.getMetadata());
    }
}