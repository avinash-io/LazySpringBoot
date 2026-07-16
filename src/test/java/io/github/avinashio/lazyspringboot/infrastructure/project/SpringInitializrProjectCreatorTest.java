package io.github.avinashio.lazyspringboot.infrastructure.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

class SpringInitializrProjectCreatorTest {

    @Test
    void shouldCreateService() {

        SpringInitializrProjectCreator creator =
                new SpringInitializrProjectCreator(
                        mock(
                                SpringInitializrRequestBuilder.class),
                        mock(
                                ZipExtractor.class));

        assertThat(creator)
                .isNotNull();
    }
}