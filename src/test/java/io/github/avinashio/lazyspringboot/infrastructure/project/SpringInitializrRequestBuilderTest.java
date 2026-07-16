package io.github.avinashio.lazyspringboot.infrastructure.project;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.project.BuildTool;
import io.github.avinashio.lazyspringboot.domain.project.NewProjectRequest;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;

class SpringInitializrRequestBuilderTest {

    private final SpringInitializrRequestBuilder builder =
            new SpringInitializrRequestBuilder();

    @Test
    void shouldBuildStarterUrl() {

        URI uri =
                builder.build(
                        new NewProjectRequest(
                                "com.example",
                                "demo",
                                "demo",
                                "com.example.demo",
                                "21",
                                "4.1.0",
                                BuildTool.MAVEN,
                                List.of(
                                        "web",
                                        "data-jpa")));

        assertThat(uri.toString())
                .contains("groupId=com.example")
                .contains("artifactId=demo")
                .contains("dependencies=web%2Cdata-jpa");
    }
}