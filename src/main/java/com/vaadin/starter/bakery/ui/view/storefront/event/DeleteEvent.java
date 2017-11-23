package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.ui.view.storefront.OrderItemEdit;
import com.vaadin.ui.event.ComponentEvent;

public class DeleteEvent extends ComponentEvent<OrderItemEdit> {

	private final int totalPrice;

	public DeleteEvent(OrderItemEdit component, int totalPrice) {
		super(component, false);
		this.totalPrice = totalPrice;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

}