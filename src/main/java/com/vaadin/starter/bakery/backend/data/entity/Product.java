package com.vaadin.starter.bakery.backend.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;

@Entity
public class Product extends AbstractEntity {

	@NotBlank(message = "{bakery.name.required}")
	@Size(max = 255)
	@Column(unique = true)
	private String name;

	// Real price * 100 as an int to avoid rounding errors
	@Min(value = 0, message = "{bakery.price.limits}")
	@Max(value = 100000, message = "{bakery.price.limits}")
	private Integer price;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		Product that = (Product) o;
		return Objects.equals(name, that.name) &&
				Objects.equals(price, that.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), name, price);
	}
}
