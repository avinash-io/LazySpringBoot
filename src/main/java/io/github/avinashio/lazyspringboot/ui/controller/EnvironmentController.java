package io.github.avinashio.lazyspringboot.ui.controller;

import io.github.avinashio.lazyspringboot.application.environment.GetEnvironmentInfoUseCase;
import io.github.avinashio.lazyspringboot.domain.environment.EnvironmentInfo;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentController {

private final GetEnvironmentInfoUseCase
		getEnvironmentInfoUseCase;

private EnvironmentInfo
		environmentInfo;

private boolean open;

public EnvironmentController(
		GetEnvironmentInfoUseCase
				getEnvironmentInfoUseCase) {
	
	this.getEnvironmentInfoUseCase =
			getEnvironmentInfoUseCase;
}

public void open() {
	
	environmentInfo =
			getEnvironmentInfoUseCase
					.getEnvironmentInfo();
	
	open = true;
}

public void close() {
	
	open = false;
	
	environmentInfo = null;
}

public boolean isOpen() {
	
	return open;
}

public EnvironmentInfo environmentInfo() {
	
	return environmentInfo;
}
}