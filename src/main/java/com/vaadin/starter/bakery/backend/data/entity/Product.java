package com.vaadin.starter.bakery.backend.data.entity;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Product extends AbstractEntity {

	@Size(max = 255, message = "The maximum length of a product name is 255 characters.")
	@NotBlank(message = "Name is required.")
	private String name;

	// Real price * 100 as an int to avoid rounding errors
	@Min(value = 0, message = "Price must be equal or higher than zero.")
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

}
