package com.vaadin.starter.bakery.backend.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class PickupLocation extends AbstractEntity {

	@Size(max = 255)
	@NotBlank
	@Column(unique = true)
	private String name;

	public PickupLocation() {
		// Empty constructor is needed by Spring Data / JPA
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
