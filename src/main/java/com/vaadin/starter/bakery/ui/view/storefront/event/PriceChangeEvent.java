package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.view.storefront.OrderItemEditor;

public class PriceChangeEvent extends ComponentEvent<OrderItemEditor> {

	private final int oldValue;

	private final int newValue;

	public PriceChangeEvent(OrderItemEditor component, int oldValue, int newValue) {
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