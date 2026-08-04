package io.github.avinashio.lazyspringboot.ui.screen;

import io.github.avinashio.lazyspringboot.ui.component.ModalRenderer;
import io.github.avinashio.lazyspringboot.ui.component.TerminalStyle;
import io.github.avinashio.lazyspringboot.ui.component.TextFormatter;
import io.github.avinashio.lazyspringboot.ui.state.CreateProjectState;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

class CreateProjectScreenTest {

@Test
void shouldRenderScreen() {
	
	Terminal terminal =
			mock(Terminal.class);
	
	StringWriter output =
			new StringWriter();
	
	PrintWriter writer =
			new PrintWriter(output);
	
	when(terminal.writer())
			.thenReturn(writer);
	
	when(terminal.getWidth())
			.thenReturn(120);
	
	when(terminal.getHeight())
			.thenReturn(40);
	
	TextFormatter textFormatter =
			new TextFormatter();
	
	ModalRenderer modalRenderer =
			new ModalRenderer(
					terminal,
					textFormatter);
	
	TerminalStyle terminalStyle =
			new TerminalStyle();
	
	CreateProjectScreen screen =
			new CreateProjectScreen(
					modalRenderer,
					terminalStyle);
	
	screen.render(
			new CreateProjectState());
	
	writer.flush();
	
	verify(terminal)
			.writer();
}
}