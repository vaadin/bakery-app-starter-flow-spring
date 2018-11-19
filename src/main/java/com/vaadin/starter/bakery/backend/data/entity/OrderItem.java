package com.vaadin.starter.bakery.backend.data.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		OrderItem orderItem = (OrderItem) o;
		return Objects.equals(product, orderItem.product) &&
				Objects.equals(quantity, orderItem.quantity) &&
				Objects.equals(comment, orderItem.comment);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), product, quantity, comment);
	}
}
