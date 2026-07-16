package io.github.avinashio.lazyspringboot.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

class ProjectActionExecutorTest {

    @Test
    void shouldCreateExecutor() {

        ProjectActionExecutor executor =
                new ProjectActionExecutor(
                        mock(ProcessController.class),
                        mock(ProjectActionController.class));

        assertThat(executor)
                .isNotNull();
    }
}