package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.domain.action.ProjectActionOutput;
import io.github.avinashio.lazyspringboot.ui.state.OutputViewport;
import io.github.avinashio.lazyspringboot.ui.state.TextInputPurpose;
import io.github.avinashio.lazyspringboot.ui.state.UiState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LogSearchControllerTest {

private UiState uiState;

private TextInputController
		textInputController;

private OutputViewport outputViewport;

private LogSearchController controller;

@BeforeEach
void setUp() {
	
	uiState =
			mock(
					UiState.class);
	
	textInputController =
			mock(
					TextInputController.class);
	
	outputViewport =
			mock(
					OutputViewport.class);
	
	when(uiState.outputViewport())
			.thenReturn(
					outputViewport);
	
	controller =
			new LogSearchController(
					uiState,
					textInputController);
}

@Test
void shouldStartLogSearch() {
	
	controller.start();
	
	verify(textInputController)
			.start(
					TextInputPurpose.LOG_SEARCH);
}

@Test
void shouldReportSearchAsActive() {
	
	when(
			textInputController.active(
					TextInputPurpose.LOG_SEARCH))
			.thenReturn(
					true);
	
	assertThat(controller.active())
			.isTrue();
}

@Test
void shouldHaveNoQueryInitially() {
	
	assertThat(controller.hasQuery())
			.isFalse();
	
	assertThat(controller.query())
			.isEmpty();
}

@Test
void shouldUseActiveInputAsEffectiveQuery() {
	
	when(
			textInputController.active(
					TextInputPurpose.LOG_SEARCH))
			.thenReturn(
					true);
	
	when(textInputController.value())
			.thenReturn(
					"error");
	
	assertThat(controller.hasQuery())
			.isTrue();
	
	assertThat(controller.query())
			.isEqualTo(
					"error");
}

@Test
void shouldApplyActiveQuery() {
	
	ProjectActionOutput output =
			output(
					"starting",
					"ERROR first",
					"finished");
	
	when(uiState.projectActionOutput())
			.thenReturn(
					output);
	
	when(
			textInputController.active(
					TextInputPurpose.LOG_SEARCH))
			.thenReturn(
					true,
					false);
	
	when(textInputController.value())
			.thenReturn(
					"error");
	
	controller.apply();
	
	verify(textInputController)
			.stop();
	
	assertThat(controller.query())
			.isEqualTo(
					"error");
	
	assertThat(controller.matchCount())
			.isEqualTo(
					1);
	
	assertThat(controller.selectedMatchNumber())
			.isEqualTo(
					1);
	
	assertThat(controller.selectedLineIndex())
			.isEqualTo(
					1);
	
	verify(outputViewport)
			.moveTo(
					1);
}

@Test
void shouldStopInputAndPreserveQuery() {
	
	ProjectActionOutput output =
			output(
					"BUILD SUCCESS");
	
	when(uiState.projectActionOutput())
			.thenReturn(
					output);
	
	when(
			textInputController.active(
					TextInputPurpose.LOG_SEARCH))
			.thenReturn(
					true,
					false);
	
	when(textInputController.value())
			.thenReturn(
					"build");
	
	controller.stopInput();
	
	verify(textInputController)
			.stop();
	
	assertThat(controller.query())
			.isEqualTo(
					"build");
	
	assertThat(controller.selectedLineIndex())
			.isZero();
	
	verify(outputViewport)
			.moveTo(
					0);
}

@Test
void shouldSelectFirstMatchWhenStoppingInactiveInput() {
	
	applyQuery(
			"error");
	
	ProjectActionOutput output =
			output(
					"line",
					"error one",
					"error two");
	
	when(uiState.projectActionOutput())
			.thenReturn(
					output);
	
	controller.stopInput();
	
	assertThat(controller.selectedMatchNumber())
			.isEqualTo(
					1);
	
	assertThat(controller.selectedLineIndex())
			.isEqualTo(
					1);
	
	verify(outputViewport)
			.moveTo(
					1);
}

@Test
void shouldClearSearch() {
	
	applyQuery(
			"error");
	
	controller.clear();
	
	assertThat(controller.query())
			.isEmpty();
	
	assertThat(controller.hasQuery())
			.isFalse();
	
	assertThat(controller.matchCount())
			.isZero();
	
	assertThat(controller.selectedMatchNumber())
			.isZero();
	
	assertThat(controller.selectedLineIndex())
			.isEqualTo(
					-1);
}

@Test
void shouldStopInputWhenClearingActiveSearch() {
	
	when(
			textInputController.active(
					TextInputPurpose.LOG_SEARCH))
			.thenReturn(
					true);
	
	controller.clear();
	
	verify(textInputController)
			.stop();
}

@Test
void shouldNotStopInputWhenClearingInactiveSearch() {
	
	when(
			textInputController.active(
					TextInputPurpose.LOG_SEARCH))
			.thenReturn(
					false);
	
	controller.clear();
	
	verify(
			textInputController,
			never())
			.stop();
}

@Test
void shouldAppendCharacterAndSelectFirstMatch() {
	
	ProjectActionOutput output =
			output(
					"normal",
					"error",
					"another error");
	
	when(uiState.projectActionOutput())
			.thenReturn(
					output);
	
	when(
			textInputController.active(
					TextInputPurpose.LOG_SEARCH))
			.thenReturn(
					true);
	
	when(textInputController.value())
			.thenReturn(
					"err");
	
	controller.append(
			'r');
	
	verify(textInputController)
			.append(
					'r');
	
	assertThat(controller.selectedLineIndex())
			.isEqualTo(
					1);
	
	verify(outputViewport)
			.moveTo(
					1);
}

@Test
void shouldBackspaceAndSelectFirstMatch() {
	
	ProjectActionOutput output =
			output(
					"normal",
					"WARN something");
	
	when(uiState.projectActionOutput())
			.thenReturn(
					output);
	
	when(
			textInputController.active(
					TextInputPurpose.LOG_SEARCH))
			.thenReturn(
					true);
	
	when(textInputController.value())
			.thenReturn(
					"warn");
	
	controller.backspace();
	
	verify(textInputController)
			.backspace();
	
	assertThat(controller.selectedLineIndex())
			.isEqualTo(
					1);
	
	verify(outputViewport)
			.moveTo(
					1);
}

@Test
void shouldMatchCaseInsensitively() {
	
	applyQuery(
			"error");
	
	ProjectActionOutput output =
			output(
					"ERROR first",
					"normal",
					"Error second",
					"error third");
	
	when(uiState.projectActionOutput())
			.thenReturn(
					output);
	
	assertThat(controller.matchCount())
			.isEqualTo(
					3);
}

@Test
void shouldMatchQueryAnywhereInLine() {
	
	applyQuery(
			"success");
	
	ProjectActionOutput output =
			output(
					"BUILD SUCCESS in 2s",
					"normal");
	
	when(uiState.projectActionOutput())
			.thenReturn(
					output);
	
	assertThat(controller.matchCount())
			.isEqualTo(
					1);
}

@Test
void shouldReturnNoMatchesWithoutOutput() {
	
	applyQuery(
			"error");
	
	when(uiState.projectActionOutput())
			.thenReturn(
					null);
	
	assertThat(controller.matchCount())
			.isZero();
	
	assertThat(controller.selectedLineIndex())
			.isEqualTo(
					-1);
}

@Test
void shouldReturnNoMatchesForBlankQuery() {
	
	ProjectActionOutput output =
			output(
					"error");
	
	when(uiState.projectActionOutput())
			.thenReturn(
					output);
	
	assertThat(controller.matchCount())
			.isZero();
}

@Test
void shouldMoveToNextMatch() {
	
	applyQuery(
			"error");
	
	ProjectActionOutput output =
			output(
					"error one",
					"normal",
					"error two");
	
	when(uiState.projectActionOutput())
			.thenReturn(
					output);
	
	controller.next();
	
	assertThat(controller.selectedMatchNumber())
			.isEqualTo(
					1);
	
	assertThat(controller.selectedLineIndex())
			.isZero();
	
	verify(outputViewport)
			.moveTo(
					0);
	
	controller.next();
	
	assertThat(controller.selectedMatchNumber())
			.isEqualTo(
					2);
	
	assertThat(controller.selectedLineIndex())
			.isEqualTo(
					2);
	
	verify(outputViewport)
			.moveTo(
					2);
}

@Test
void shouldWrapNextMatchToBeginning() {
	
	applyQuery(
			"error");
	
	ProjectActionOutput output =
			output(
					"error one",
					"error two");
	
	when(uiState.projectActionOutput())
			.thenReturn(
					output);
	
	controller.next();
	controller.next();
	controller.next();
	
	assertThat(controller.selectedMatchNumber())
			.isEqualTo(
					1);
	
	assertThat(controller.selectedLineIndex())
			.isZero();
	
	verify(
			outputViewport,
			times(
					2))
			.moveTo(
					0);
	
	verify(outputViewport)
			.moveTo(
					1);
}

@Test
void shouldMoveToPreviousMatch() {
	
	applyQuery(
			"error");
	
	ProjectActionOutput output =
			output(
					"error one",
					"normal",
					"error two");
	
	when(uiState.projectActionOutput())
			.thenReturn(
					output);
	
	controller.previous();
	
	assertThat(controller.selectedMatchNumber())
			.isEqualTo(
					2);
	
	assertThat(controller.selectedLineIndex())
			.isEqualTo(
					2);
	
	verify(outputViewport)
			.moveTo(
					2);
}

@Test
void shouldWrapPreviousMatchToEnd() {
	
	applyQuery(
			"error");
	
	ProjectActionOutput output =
			output(
					"error one",
					"error two");
	
	when(uiState.projectActionOutput())
			.thenReturn(
					output);
	
	controller.previous();
	
	assertThat(controller.selectedMatchNumber())
			.isEqualTo(
					2);
	
	assertThat(controller.selectedLineIndex())
			.isEqualTo(
					1);
	
	verify(outputViewport)
			.moveTo(
					1);
}

@Test
void shouldResetSelectionWhenNoNextMatchExists() {
	
	applyQuery(
			"missing");
	
	ProjectActionOutput output =
			output(
					"normal");
	
	when(uiState.projectActionOutput())
			.thenReturn(
					output);
	
	controller.next();
	
	assertThat(controller.selectedMatchNumber())
			.isZero();
	
	assertThat(controller.selectedLineIndex())
			.isEqualTo(
					-1);
	
	verify(
			outputViewport,
			never())
			.moveTo(
					org.mockito.ArgumentMatchers
							.anyInt());
}

@Test
void shouldResetSelectionWhenNoPreviousMatchExists() {
	
	applyQuery(
			"missing");
	
	ProjectActionOutput output =
			output(
					"normal");
	
	when(uiState.projectActionOutput())
			.thenReturn(
					output);
	
	controller.previous();
	
	assertThat(controller.selectedMatchNumber())
			.isZero();
	
	assertThat(controller.selectedLineIndex())
			.isEqualTo(
					-1);
	
	verify(
			outputViewport,
			never())
			.moveTo(
					org.mockito.ArgumentMatchers
							.anyInt());
}

@Test
void shouldRestoreAppliedQueryWhenSearchStartsAgain() {
	
	applyQuery(
			"error");
	
	controller.start();
	
	verify(textInputController)
			.start(
					TextInputPurpose.LOG_SEARCH);
	
	verify(textInputController)
			.append(
					'e');
	
	verify(
			textInputController,
			times(
					3))
			.append(
					'r');
	
	verify(textInputController)
			.append(
					'o');
}

private void applyQuery(
		String query) {
	
	when(
			textInputController.active(
					TextInputPurpose.LOG_SEARCH))
			.thenReturn(
					true,
					false);
	
	when(textInputController.value())
			.thenReturn(
					query);
	
	controller.apply();
}

private ProjectActionOutput output(
		String... lines) {
	
	ProjectActionOutput output =
			mock(
					ProjectActionOutput.class);
	
	when(output.lines())
			.thenReturn(
					List.of(
							lines));
	
	return output;
}
}