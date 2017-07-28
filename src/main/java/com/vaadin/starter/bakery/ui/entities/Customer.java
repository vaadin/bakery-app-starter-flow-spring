package com.vaadin.starter.bakery.ui.entities;

import java.io.Serializable;

public class Customer implements Serializable {

	private static final long serialVersionUID = 5750103957741266863L;

	@Override
	public String toString() {
		return "Customer [name=" + name + ", number=" + number + ", details=" + details + "]";
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((details == null) ? 0 : details.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (details == null) {
			if (other.details != null)
				return false;
		} else if (!details.equals(other.details))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		return true;
	}

}
