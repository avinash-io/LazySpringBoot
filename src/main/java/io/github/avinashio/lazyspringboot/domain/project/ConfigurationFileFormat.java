package io.github.avinashio.lazyspringboot.domain.project;

public enum ConfigurationFileFormat {
	
	PROPERTIES("properties", "Props"),
	
	YAML("yaml", "YAML");

private final String initializrId;

private final String label;

ConfigurationFileFormat(
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