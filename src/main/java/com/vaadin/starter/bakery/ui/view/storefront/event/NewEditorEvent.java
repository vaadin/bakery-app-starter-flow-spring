package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.ui.view.storefront.OrderItemsEditor;
import com.vaadin.ui.event.ComponentEvent;

public class NewEditorEvent extends ComponentEvent<OrderItemsEditor> {

	public NewEditorEvent(OrderItemsEditor component) {
		super(component, false);
	}
}