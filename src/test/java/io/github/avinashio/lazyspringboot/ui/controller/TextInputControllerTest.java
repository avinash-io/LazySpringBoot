package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;
import io.github.avinashio.lazyspringboot.ui.state.TextInputState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TextInputControllerTest {

private TextInputState textInputState;

private TextInputController controller;

@BeforeEach
void setUp() {
	
	textInputState =
			mock(
					TextInputState.class);
	
	controller =
			new TextInputController(
					textInputState);
}

@Test
void shouldStartTextInput() {
	
	controller.start(
			TextInputPurpose.PROJECT_SEARCH);
	
	verify(textInputState)
			.start(
					TextInputPurpose.PROJECT_SEARCH);
}

@Test
void shouldStartLogSearchInput() {
	
	controller.start(
			TextInputPurpose.LOG_SEARCH);
	
	verify(textInputState)
			.start(
					TextInputPurpose.LOG_SEARCH);
}

@Test
void shouldStopTextInput() {
	
	controller.stop();
	
	verify(textInputState)
			.stop();
}

@Test
void shouldAppendCharacter() {
	
	controller.append(
			'a');
	
	verify(textInputState)
			.append(
					'a');
}

@Test
void shouldAppendSpecialCharacter() {
	
	controller.append(
			'-');
	
	verify(textInputState)
			.append(
					'-');
}

@Test
void shouldBackspace() {
	
	controller.backspace();
	
	verify(textInputState)
			.backspace();
}

@Test
void shouldReportPurposeAsActive() {
	
	when(
			textInputState.isActive(
					TextInputPurpose.PROJECT_SEARCH))
			.thenReturn(
					true);
	
	assertThat(
			controller.active(
					TextInputPurpose.PROJECT_SEARCH))
			.isTrue();
	
	verify(textInputState)
			.isActive(
					TextInputPurpose.PROJECT_SEARCH);
}

@Test
void shouldReportPurposeAsInactive() {
	
	when(
			textInputState.isActive(
					TextInputPurpose.LOG_SEARCH))
			.thenReturn(
					false);
	
	assertThat(
			controller.active(
					TextInputPurpose.LOG_SEARCH))
			.isFalse();
	
	verify(textInputState)
			.isActive(
					TextInputPurpose.LOG_SEARCH);
}

@Test
void shouldReturnCurrentValue() {
	
	when(textInputState.value())
			.thenReturn(
					"spring");
	
	assertThat(controller.value())
			.isEqualTo(
					"spring");
	
	verify(textInputState)
			.value();
}

@Test
void shouldReturnEmptyValue() {
	
	when(textInputState.value())
			.thenReturn(
					"");
	
	assertThat(controller.value())
			.isEmpty();
	
	verify(textInputState)
			.value();
}
}