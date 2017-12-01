package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.ui.view.storefront.OrderItemsEditor;
import com.vaadin.ui.event.ComponentEvent;

public class ValueChangeEvent extends ComponentEvent<OrderItemsEditor> {

	public ValueChangeEvent(OrderItemsEditor component) {
		super(component, false);
	}
}