package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.view.storefront.OrderItemEdit;
import com.vaadin.ui.event.ComponentEvent;

public class ProductChangeEvent extends ComponentEvent<OrderItemEdit> {

	private final Product product;

	public ProductChangeEvent(OrderItemEdit component, Product product) {
		super(component, false);
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}

}