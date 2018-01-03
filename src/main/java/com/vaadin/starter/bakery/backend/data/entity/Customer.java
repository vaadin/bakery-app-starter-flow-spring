package com.vaadin.starter.bakery.backend.data.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Entity
public class Customer extends AbstractEntity {

	@NotBlank
	@Size(max = 255)
	private String fullName;

	@NotBlank
	@Size(max = 255)
	@Pattern(regexp = "^(\\+\\d+[ \\-]+)?(\\d+[ \\-]+)?(\\d[ \\-\\.]?){6,14}$", message = "{bakery.phone.number.invalid}")
	private String phoneNumber;
	
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

}
