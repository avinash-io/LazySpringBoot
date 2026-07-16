package io.github.avinashio.lazyspringboot.infrastructure.actuator;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import org.junit.jupiter.api.Test;

class ActuatorClientTest {

    @Test
    void shouldCreateClient() {

        ActuatorClient client =
                new ActuatorClient();

        assertThat(client)
                .isNotNull();
    }

    @Test
    void shouldInitializeHttpClient()
            throws Exception {

        ActuatorClient client =
                new ActuatorClient();

        Field field =
                ActuatorClient.class.getDeclaredField(
                        "httpClient");

        field.setAccessible(true);

        Object value =
                field.get(client);

        assertThat(value)
                .isInstanceOf(HttpClient.class);
    }

    @Test
    void shouldAcceptUri() {

        URI uri =
                URI.create(
                        "http://localhost:8080/actuator/health");

        assertThat(uri.getPath())
                .isEqualTo(
                        "/actuator/health");
    }
}