package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.ui.view.storefront.OrderEditor;
import com.vaadin.ui.event.ComponentEvent;

public class ReviewEvent extends ComponentEvent<OrderEditor> {

	public ReviewEvent(OrderEditor component) {
		super(component, false);
	}
}