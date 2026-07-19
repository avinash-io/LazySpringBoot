package io.github.avinashio.lazyspringboot.application.initializr;

import io.github.avinashio.lazyspringboot.domain.initializr.InitializrConfiguration;
import io.github.avinashio.lazyspringboot.infrastructure.initializr.InitializrMetadataMapper;
import io.github.avinashio.lazyspringboot.infrastructure.initializr.SpringInitializrClient;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class GetInitializrConfigurationUseCase {

    private final SpringInitializrClient
            springInitializrClient;

    private final InitializrMetadataMapper
            metadataMapper;

    public GetInitializrConfigurationUseCase(
            SpringInitializrClient springInitializrClient,
            InitializrMetadataMapper metadataMapper) {

        this.springInitializrClient =
                springInitializrClient;

        this.metadataMapper =
                metadataMapper;
    }

    public InitializrConfiguration getConfiguration()
            throws IOException,
            InterruptedException {

        return metadataMapper.map(
                springInitializrClient.getMetadata());
    }
}