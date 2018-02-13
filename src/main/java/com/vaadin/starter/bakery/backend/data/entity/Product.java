package com.vaadin.starter.bakery.backend.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Product extends AbstractEntity {

	@Size(max = 255, message = "{bakery.name.max.length}")
	@NotBlank(message = "{bakery.name.required}")
	@Column(unique = true)
	private String name;

	// Real price * 100 as an int to avoid rounding errors
	@Min(value = 1, message = "{bakery.price.required}")
	private int price;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return name;
	}
}
