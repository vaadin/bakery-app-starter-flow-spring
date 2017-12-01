package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.ui.view.storefront.OrderItemEditor;
import com.vaadin.ui.event.ComponentEvent;

public class DeleteEvent extends ComponentEvent<OrderItemEditor> {

	private final int totalPrice;

	public DeleteEvent(OrderItemEditor component, int totalPrice) {
		super(component, false);
		this.totalPrice = totalPrice;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

}