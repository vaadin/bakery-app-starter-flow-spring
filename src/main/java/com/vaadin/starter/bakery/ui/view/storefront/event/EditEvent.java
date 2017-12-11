package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.ui.Component;
import com.vaadin.ui.event.ComponentEvent;

public class EditEvent extends ComponentEvent<Component> {

	public EditEvent(Component source) {
		super(source, false);
	}
}
