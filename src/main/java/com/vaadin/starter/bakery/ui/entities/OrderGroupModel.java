package com.vaadin.starter.bakery.ui.entities;

import java.util.List;

public class OrderGroupModel {
	private String mainTitle;
	private String secondaryTitle;
	private List<Order> orders;

	public OrderGroupModel() {
	}

	public OrderGroupModel(String mainTitle, String secondaryTitle, List<Order> orders) {
		this.mainTitle = mainTitle;
		this.secondaryTitle = secondaryTitle;
		this.orders = orders;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getSecondaryTitle() {
		return secondaryTitle;
	}

	public void setSecondaryTitle(String secondaryTitle) {
		this.secondaryTitle = secondaryTitle;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}