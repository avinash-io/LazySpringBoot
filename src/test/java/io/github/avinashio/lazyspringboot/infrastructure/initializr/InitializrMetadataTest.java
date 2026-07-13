package io.github.avinashio.lazyspringboot.infrastructure.initializr;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class InitializrMetadataTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldDeserializeDependencyMetadataWithUnknownFields()
            throws Exception {
        String json =
                """
                {
                  "_links": {
                    "gradle-project": {
                      "href": "https://start.spring.io/starter.zip",
                      "templated": true
                    }
                  },
                  "dependencies": {
                    "type": "hierarchical-multi-select",
                    "values": [
                      {
                        "name": "Developer Tools",
                        "values": [
                          {
                            "id": "devtools",
                            "name": "Spring Boot DevTools",
                            "description": "Provides fast application restarts.",
                            "_links": {
                              "reference": {
                                "href": "https://docs.spring.io",
                                "templated": true
                              }
                            }
                          }
                        ]
                      }
                    ]
                  }
                }
                """;

        InitializrMetadata metadata =
                objectMapper.readValue(json, InitializrMetadata.class);

        InitializrDependency dependency =
                metadata
                        .dependencies()
                        .values()
                        .getFirst()
                        .values()
                        .getFirst();

        assertThat(dependency.id()).isEqualTo("devtools");
        assertThat(dependency.name())
                .isEqualTo("Spring Boot DevTools");
    }

}