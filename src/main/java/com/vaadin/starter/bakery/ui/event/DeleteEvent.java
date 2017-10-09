package com.vaadin.starter.bakery.ui.event;

import com.vaadin.ui.Component;
import com.vaadin.ui.event.ComponentEvent;

public class DeleteEvent extends ComponentEvent<Component> {

	public DeleteEvent(Component source, boolean fromClient) {
		super(source, fromClient);
	}

}