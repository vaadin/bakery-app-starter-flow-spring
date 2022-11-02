package com.vaadin.starter.bakery.ui.views.storefront.beans;

import java.io.Serializable;

public class OrderCardHeader implements Serializable {

	private String main;
	private String secondary;

	public OrderCardHeader(String main, String secondary) {
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
