package com.vaadin.starter.bakery.ui.entities;

import java.io.Serializable;

public class Good implements Serializable {

	private static final long serialVersionUID = 1412133251561188289L;

	@Override
	public String toString() {
		return "Good []";
	}

	private int count;
	private String name;
	private int unitPrice;
	private String description;

	public Good() {

	}

	public Good(int count, String name, int unitPrice, String comment) {
		this.count = count;
		this.name = name;
		this.unitPrice = unitPrice;
		this.setDescription(comment);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
