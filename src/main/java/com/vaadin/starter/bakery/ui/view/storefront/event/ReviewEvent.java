package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.ui.view.storefront.OrderEdit;
import com.vaadin.ui.event.ComponentEvent;

public class ReviewEvent extends ComponentEvent<OrderEdit> {

	public ReviewEvent(OrderEdit component) {
		super(component, false);
	}
}