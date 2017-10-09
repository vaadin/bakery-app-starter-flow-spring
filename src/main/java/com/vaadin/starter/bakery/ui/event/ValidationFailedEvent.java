package com.vaadin.starter.bakery.ui.event;

import com.vaadin.ui.Component;
import com.vaadin.ui.event.ComponentEvent;

public class ValidationFailedEvent extends ComponentEvent<Component> {

	public ValidationFailedEvent(Component source) {
		super(source, false);
	}

}