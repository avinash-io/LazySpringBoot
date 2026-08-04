package io.github.avinashio.lazyspringboot.domain.project;

public enum ProjectPackaging {
	
	JAR("jar", "Jar"),
	
	WAR("war", "War");

private final String initializrId;

private final String label;

ProjectPackaging(
		String initializrId,
		String label) {
	
	this.initializrId =
			initializrId;
	
	this.label =
			label;
}

public String initializrId() {
	return initializrId;
}

public String label() {
	return label;
}
}