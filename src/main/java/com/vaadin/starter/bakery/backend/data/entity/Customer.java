package com.vaadin.starter.bakery.backend.data.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;


@Entity
public class Customer extends AbstractEntity {

	@NotBlank
	@Size(max = 255)
	private String fullName;

	@NotBlank
	@Size(max = 20, message = "{bakery.phone.number.invalid}")
	// A simple phone number checker, allowing an optional international prefix
	// plus a variable number of digits that could be separated by dashes or
	// spaces
	@Pattern(regexp = "^(\\+\\d+)?([ -]?\\d+){4,14}$", message = "{bakery.phone.number.invalid}")
	private String phoneNumber;
	
	@Size(max = 255)
	private String details;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Customer customer = (Customer) o;
		return Objects.equals(fullName, customer.fullName) &&
				Objects.equals(phoneNumber, customer.phoneNumber) &&
				Objects.equals(details, customer.details);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), fullName, phoneNumber, details);
	}
}
