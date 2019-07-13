package com.vaadin.starter.bakery.backend.data.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class OrderItem extends AbstractEntity {

	@ManyToOne
	@NotNull(message = "{bakery.pickup.product.required}")
	private Product product;

	@Min(1)
	@NotNull
	private Double quantity = 1d;

	@Size(max = 255)
	private String comment;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getTotalPrice() {
		return quantity == null || product == null ? 0 : quantity.intValue() * product.getPrice();
	}
}
