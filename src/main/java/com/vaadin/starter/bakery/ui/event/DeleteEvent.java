package com.vaadin.starter.bakery.ui.event;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class DeleteEvent extends ComponentEvent<Component> {

	public DeleteEvent(Component source, boolean fromClient) {
		super(source, fromClient);
	}

}