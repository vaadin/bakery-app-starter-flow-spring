package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.ui.view.storefront.OrderItemsEdit;
import com.vaadin.ui.event.ComponentEvent;

public class TotalPriceChangeEvent extends ComponentEvent<OrderItemsEdit> {

	private final Integer totalPrice;

	public TotalPriceChangeEvent(OrderItemsEdit component, Integer totalPrice) {
		super(component, false);
		this.totalPrice = totalPrice;
	}

	public Integer getTotalPrice() {
		return totalPrice;
	}

}