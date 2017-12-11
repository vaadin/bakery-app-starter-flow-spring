package com.vaadin.starter.bakery.backend.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@BatchSize(size=5)
public class PickupLocation extends AbstractEntity {

	@Size(max = 255)
	@NotBlank
	@Column(unique = true)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
