package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.environment.GetEnvironmentInfoUseCase;
import io.github.avinashio.lazyspringboot.domain.environment.EnvironmentInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EnvironmentControllerTest {

private GetEnvironmentInfoUseCase
		getEnvironmentInfoUseCase;

private EnvironmentController controller;

@BeforeEach
void setUp() {
	
	getEnvironmentInfoUseCase =
			mock(
					GetEnvironmentInfoUseCase.class);
	
	controller =
			new EnvironmentController(
					getEnvironmentInfoUseCase);
}

@Test
void shouldBeClosedInitially() {
	
	assertThat(controller.isOpen())
			.isFalse();
}

@Test
void shouldHaveNoEnvironmentInfoInitially() {
	
	assertThat(controller.environmentInfo())
			.isNull();
}

@Test
void shouldOpenEnvironment() {
	
	EnvironmentInfo environmentInfo =
			mock(
					EnvironmentInfo.class);
	
	when(
			getEnvironmentInfoUseCase
					.getEnvironmentInfo())
			.thenReturn(
					environmentInfo);
	
	controller.open();
	
	assertThat(controller.isOpen())
			.isTrue();
	
	assertThat(controller.environmentInfo())
			.isSameAs(
					environmentInfo);
	
	verify(getEnvironmentInfoUseCase)
			.getEnvironmentInfo();
}

@Test
void shouldRefreshEnvironmentInfoEveryTimeEnvironmentIsOpened() {
	
	EnvironmentInfo firstEnvironmentInfo =
			mock(
					EnvironmentInfo.class);
	
	EnvironmentInfo secondEnvironmentInfo =
			mock(
					EnvironmentInfo.class);
	
	when(
			getEnvironmentInfoUseCase
					.getEnvironmentInfo())
			.thenReturn(
					firstEnvironmentInfo,
					secondEnvironmentInfo);
	
	controller.open();
	
	assertThat(controller.environmentInfo())
			.isSameAs(
					firstEnvironmentInfo);
	
	controller.close();
	controller.open();
	
	assertThat(controller.environmentInfo())
			.isSameAs(
					secondEnvironmentInfo);
	
	verify(
			getEnvironmentInfoUseCase,
			org.mockito.Mockito.times(
					2))
			.getEnvironmentInfo();
}

@Test
void shouldCloseEnvironment() {
	
	EnvironmentInfo environmentInfo =
			mock(
					EnvironmentInfo.class);
	
	when(
			getEnvironmentInfoUseCase
					.getEnvironmentInfo())
			.thenReturn(
					environmentInfo);
	
	controller.open();
	
	assertThat(controller.isOpen())
			.isTrue();
	
	controller.close();
	
	assertThat(controller.isOpen())
			.isFalse();
}

@Test
void shouldClearEnvironmentInfoWhenClosed() {
	
	EnvironmentInfo environmentInfo =
			mock(
					EnvironmentInfo.class);
	
	when(
			getEnvironmentInfoUseCase
					.getEnvironmentInfo())
			.thenReturn(
					environmentInfo);
	
	controller.open();
	
	assertThat(controller.environmentInfo())
			.isSameAs(
					environmentInfo);
	
	controller.close();
	
	assertThat(controller.environmentInfo())
			.isNull();
}

@Test
void shouldRemainClosedWhenCloseIsCalledBeforeOpen() {
	
	controller.close();
	
	assertThat(controller.isOpen())
			.isFalse();
	
	assertThat(controller.environmentInfo())
			.isNull();
}
}