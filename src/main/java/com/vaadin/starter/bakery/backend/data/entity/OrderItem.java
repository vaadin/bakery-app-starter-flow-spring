package com.vaadin.starter.bakery.backend.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class OrderItem extends AbstractEntity {

	@ManyToOne
	@NotNull(message = "{bakery.pickup.product.required}")
	private Product product;

	@Min(1)
	@NotNull
	private Integer quantity = 1;

	@Size(max = 255)
	private String comment;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getTotalPrice() {
		return quantity == null || product == null ? 0 : quantity * product.getPrice();
	}
}
