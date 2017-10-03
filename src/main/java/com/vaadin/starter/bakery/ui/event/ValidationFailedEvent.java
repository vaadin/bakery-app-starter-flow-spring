package com.vaadin.starter.bakery.ui.event;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentEvent;

public class ValidationFailedEvent extends ComponentEvent<Component> {

	public ValidationFailedEvent(Component source) {
		super(source, false);
	}

}