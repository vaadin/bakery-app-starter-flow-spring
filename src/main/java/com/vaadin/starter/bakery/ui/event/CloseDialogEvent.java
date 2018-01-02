package com.vaadin.starter.bakery.ui.event;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;

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