package com.vaadin.starter.bakery.ui.entities;

public class StorefrontItemHeader {

	private String main;
	private String secondary;

	public StorefrontItemHeader(String main, String secondary) {
		this.main = main;
		this.secondary = secondary;
	}

	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public String getSecondary() {
		return secondary;
	}

	public void setSecondary(String secondary) {
		this.secondary = secondary;
	}
}
