package io.github.avinashio.lazyspringboot.ui.input;

import io.github.avinashio.lazyspringboot.ui.controller.EnvironmentController;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentInputHandler {

private final EnvironmentController
		environmentController;

public EnvironmentInputHandler(
		EnvironmentController
				environmentController) {
	
	this.environmentController =
			environmentController;
}

public boolean handle(
		KeyEvent keyEvent) {
	
	if (!environmentController.isOpen()) {
		return false;
	}
	
	if (keyEvent.type()
				== KeyType.ESCAPE) {
		
		environmentController.close();
		
		return true;
	}
	
	return false;
}
}