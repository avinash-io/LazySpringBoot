package io.github.avinashio.lazyspringboot.ui.state;

import org.springframework.stereotype.Component;

@Component
public class ProjectManagerState {

private boolean open;

private int selectedIndex;

public boolean isOpen() {
	
	return open;
}

public void open(
		int selectedIndex) {
	
	open = true;
	
	this.selectedIndex =
			Math.max(
					0,
					selectedIndex);
}

public void close() {
	
	open = false;
}

public int selectedIndex() {
	
	return selectedIndex;
}

public void selectPrevious(
		int projectCount) {
	
	if (projectCount <= 0) {
		selectedIndex = 0;
		return;
	}
	
	selectedIndex =
			Math.max(
					0,
					selectedIndex - 1);
}

public void selectNext(
		int projectCount) {
	
	if (projectCount <= 0) {
		selectedIndex = 0;
		return;
	}
	
	selectedIndex =
			Math.min(
					projectCount - 1,
					selectedIndex + 1);
}
}