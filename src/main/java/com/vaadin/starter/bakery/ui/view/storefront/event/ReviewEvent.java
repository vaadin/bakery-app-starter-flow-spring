package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.starter.bakery.ui.view.storefront.OrderEditor;

public class ReviewEvent extends ComponentEvent<OrderEditor> {

	public ReviewEvent(OrderEditor component) {
		super(component, false);
	}
}