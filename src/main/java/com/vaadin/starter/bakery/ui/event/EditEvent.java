package com.vaadin.starter.bakery.ui.event;

import com.vaadin.ui.Component;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.DomEvent;
import com.vaadin.ui.event.EventData;

@DomEvent("edit")
public class EditEvent extends ComponentEvent<Component> {

	private final String id;

	public EditEvent(Component source, boolean fromClient, @EventData("event.detail") String id) {
		super(source, fromClient);
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
