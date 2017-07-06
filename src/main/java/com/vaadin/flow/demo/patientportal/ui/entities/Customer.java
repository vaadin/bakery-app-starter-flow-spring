package com.vaadin.flow.demo.patientportal.ui.entities;

import java.io.Serializable;

public class Customer implements Serializable {

	private static final long serialVersionUID = 5750103957741266863L;

	@Override
	public String toString() {
		return "Customer [name=" + name + "]";
	}

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
