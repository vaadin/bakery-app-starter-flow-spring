package com.vaadin.starter.bakery.ui.event;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentEvent;

public class CancelEvent extends ComponentEvent<Component> {

	public CancelEvent(Component source, boolean fromClient) {
		super(source, fromClient);
	}
}
