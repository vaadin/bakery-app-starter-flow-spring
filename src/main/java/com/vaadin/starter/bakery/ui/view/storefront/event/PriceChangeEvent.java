package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.ui.view.storefront.OrderItemEdit;
import com.vaadin.ui.event.ComponentEvent;

public class PriceChangeEvent extends ComponentEvent<OrderItemEdit> {

	private final int oldValue;

	private final int newValue;

	public PriceChangeEvent(OrderItemEdit component, int oldValue, int newValue) {
		super(component, false);
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public int getOldValue() {
		return oldValue;
	}

	public int getNewValue() {
		return newValue;
	}

}