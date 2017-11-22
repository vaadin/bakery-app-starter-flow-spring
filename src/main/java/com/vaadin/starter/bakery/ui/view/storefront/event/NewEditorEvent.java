package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.ui.view.storefront.OrderItemsEdit;
import com.vaadin.ui.event.ComponentEvent;

public class NewEditorEvent extends ComponentEvent<OrderItemsEdit> {

	public NewEditorEvent(OrderItemsEdit component) {
		super(component, false);
	}
}