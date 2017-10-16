package com.vaadin.starter.bakery.ui.components.storefront.converter;

import java.io.Serializable;

public class StorefrontDate implements Serializable {
	private String day;
	private String weekday;
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

}