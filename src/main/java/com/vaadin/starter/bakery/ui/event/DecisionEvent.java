package com.vaadin.starter.bakery.ui.event;

import com.vaadin.ui.Component;
import com.vaadin.ui.event.ComponentEvent;

public class DecisionEvent extends ComponentEvent<Component> {

	private final boolean confirmed;

	public DecisionEvent(Component source, boolean confirmed) {
		super(source, false);
		this.confirmed = confirmed;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void ifConfirmed(Runnable r) {
		if (confirmed) {
			r.run();
		}
	}
}