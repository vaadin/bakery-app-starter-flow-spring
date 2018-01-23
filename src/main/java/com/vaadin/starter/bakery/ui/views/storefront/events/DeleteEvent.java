package com.vaadin.starter.bakery.ui.views.storefront.events;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.views.storefront.OrderItemEditor;

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