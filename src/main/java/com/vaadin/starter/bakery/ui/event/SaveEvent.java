package com.vaadin.starter.bakery.ui.event;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentEvent;

public class SaveEvent extends ComponentEvent<Component> {

	public SaveEvent(Component source, boolean fromClient) {
		super(source, fromClient);
	}

}