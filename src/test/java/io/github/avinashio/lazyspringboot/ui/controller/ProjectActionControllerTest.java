package io.github.avinashio.lazyspringboot.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import io.github.avinashio.lazyspringboot.application.action.ExecuteProjectActionUseCase;
import io.github.avinashio.lazyspringboot.domain.action.ActionItem;
import io.github.avinashio.lazyspringboot.domain.action.ProjectAction;
import io.github.avinashio.lazyspringboot.ui.input.KeyEvent;
import io.github.avinashio.lazyspringboot.ui.input.KeyType;
import io.github.avinashio.lazyspringboot.ui.screen.ProjectActionOutputScreen;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.github.avinashio.lazyspringboot.application.action.ProjectActionCatalog;
import io.github.avinashio.lazyspringboot.application.process.GetProjectProcessUseCase;

class ProjectActionControllerTest {

    private UiState uiState;

    private ProjectActionController controller;

    @BeforeEach
    void setUp() {

        uiState = mock(UiState.class);

        controller =
                new ProjectActionController(
                        uiState,
                        mock(
                                ExecuteProjectActionUseCase.class),
                        mock(
                                ProjectActionOutputScreen.class),
                        mock(
                                ProjectActionCatalog.class),
                        mock(
                                GetProjectProcessUseCase.class));
    }

    @Test
    void shouldHandleEscape() {

        boolean handled =
                controller.handleKey(
                        new KeyEvent(
                                KeyType.ESCAPE,
                                null),
                        actions());

        assertThat(handled).isTrue();

        verify(uiState)
                .stopProjectActions();
    }

    @Test
    void shouldHandleUp() {

        boolean handled =
                controller.handleKey(
                        new KeyEvent(
                                KeyType.UP,
                                null),
                        actions());

        assertThat(handled).isTrue();

        verify(uiState)
                .selectPreviousProjectAction();
    }

    @Test
    void shouldHandleDown() {

        boolean handled =
                controller.handleKey(
                        new KeyEvent(
                                KeyType.DOWN,
                                null),
                        actions());

        assertThat(handled).isTrue();

        verify(uiState)
                .selectNextProjectAction(
                        2);
    }

    @Test
    void shouldIgnoreUnhandledKey() {

        boolean handled =
                controller.handleKey(
                        new KeyEvent(
                                KeyType.ENTER,
                                null),
                        actions());

        assertThat(handled).isFalse();

        verifyNoInteractions(
                uiState);
    }

    private List<ActionItem> actions() {

        return List.of(
                new ActionItem(
                        ProjectAction.BUILD,
                        true),
                new ActionItem(
                        ProjectAction.TEST,
                        true));
    }
}