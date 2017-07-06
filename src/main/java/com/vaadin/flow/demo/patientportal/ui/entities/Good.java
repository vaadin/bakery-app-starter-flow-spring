package com.vaadin.flow.demo.patientportal.ui.entities;

import java.io.Serializable;

public class Good implements Serializable {

	private static final long serialVersionUID = 1412133251561188289L;

	@Override
	public String toString() {
		return "Good []";
	}

	private int count;
	private String name;

	public Good() {

	}

	public Good(int count, String name) {
		this.count = count;
		this.name = name;
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

}
