package com.vaadin.flow.demo.patientportal.ui.entities;

import java.io.Serializable;

public class Customer implements Serializable {

	@Override
	public String toString() {
		return "Customer [name=" + name + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String name;

	Customer() {

	}

	public Customer(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
