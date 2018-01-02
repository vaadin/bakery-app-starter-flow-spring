package com.vaadin.ui.event;

import com.vaadin.flow.component.Component;

/**
 * FIXME: remove this when charts is updated to alpha11
 */
@Deprecated
public class ComponentEvent<T extends Component> extends com.vaadin.flow.component.ComponentEvent<Component> {

	public ComponentEvent(T source, boolean fromClient) {
		super(source, fromClient);
	}

}