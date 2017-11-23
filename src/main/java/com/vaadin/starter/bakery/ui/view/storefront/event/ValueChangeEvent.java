package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.ui.view.storefront.OrderItemsEdit;
import com.vaadin.ui.event.ComponentEvent;

public class ValueChangeEvent extends ComponentEvent<OrderItemsEdit> {

	public ValueChangeEvent(OrderItemsEdit component) {
		super(component, false);
	}
}