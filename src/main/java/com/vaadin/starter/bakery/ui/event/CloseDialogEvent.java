package com.vaadin.starter.bakery.ui.event;

import com.vaadin.ui.Component;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.DomEvent;

/**
 * The user requested the edit dialog to be closed.
 *
 */
@DomEvent("close-dialog")
public class CloseDialogEvent extends ComponentEvent<Component> {

	public CloseDialogEvent(Component source, boolean fromClient) {
		super(source, fromClient);
	}

}