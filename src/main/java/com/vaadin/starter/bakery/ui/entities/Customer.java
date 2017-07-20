package com.vaadin.starter.bakery.ui.entities;

import java.io.Serializable;

public class Customer implements Serializable {

	private static final long serialVersionUID = 5750103957741266863L;

	@Override
	public String toString() {
		return "Customer [name=" + name + "]";
	}

	private String name;
	private String number;
	private String details;

	public Customer() {

	}

	public Customer(String name, String phoneNumber, String details) {
		this.name = name;
		this.number = phoneNumber;
		this.details = details;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
