package io.github.avinashio.lazyspringboot.infrastructure.maven;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.avinashio.lazyspringboot.domain.dependency.DependencyCoordinate;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class MavenDependencyParserTest {

    private final MavenDependencyParser parser =
            new MavenDependencyParser();

    @Test
    void shouldParseDirectDependencies()
            throws Exception {
        String pom =
                """
                <project>
                  <dependencies>
                    <dependency>
                      <groupId>org.springframework.boot</groupId>
                      <artifactId>spring-boot-starter-webmvc</artifactId>
                    </dependency>
        
                    <dependency>
                      <groupId>org.projectlombok</groupId>
                      <artifactId>lombok</artifactId>
                    </dependency>
                  </dependencies>
                </project>
                """;

        List<DependencyCoordinate> dependencies =
                parser.parse(inputStream(pom));

        assertThat(dependencies)
                .containsExactly(
                        new DependencyCoordinate(
                                "org.springframework.boot",
                                "spring-boot-starter-webmvc"),
                        new DependencyCoordinate(
                                "org.projectlombok",
                                "lombok"));
    }

    @Test
    void shouldIgnoreDependencyManagement()
            throws Exception {
        String pom =
                """
                <project>
                  <dependencyManagement>
                    <dependencies>
                      <dependency>
                        <groupId>com.example</groupId>
                        <artifactId>managed-dependency</artifactId>
                      </dependency>
                    </dependencies>
                  </dependencyManagement>
        
                  <dependencies>
                    <dependency>
                      <groupId>org.projectlombok</groupId>
                      <artifactId>lombok</artifactId>
                    </dependency>
                  </dependencies>
                </project>
                """;

        List<DependencyCoordinate> dependencies =
                parser.parse(inputStream(pom));

        assertThat(dependencies)
                .containsExactly(
                        new DependencyCoordinate(
                                "org.projectlombok",
                                "lombok"));
    }

    @Test
    void shouldReturnEmptyListWhenDependenciesAreMissing()
            throws Exception {
        String pom =
                """
                <project>
                  <groupId>com.example</groupId>
                  <artifactId>demo</artifactId>
                </project>
                """;

        assertThat(parser.parse(inputStream(pom)))
                .isEmpty();
    }

    private ByteArrayInputStream inputStream(
            String content) {
        return new ByteArrayInputStream(
                content.getBytes(StandardCharsets.UTF_8));
    }
}