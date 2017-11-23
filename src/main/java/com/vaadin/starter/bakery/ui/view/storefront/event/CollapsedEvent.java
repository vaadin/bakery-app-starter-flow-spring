package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.ui.view.storefront.StorefrontItemDetailWrapper;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.DomEvent;

@DomEvent("collapsed")
public class CollapsedEvent extends ComponentEvent<StorefrontItemDetailWrapper> {
	public CollapsedEvent(StorefrontItemDetailWrapper source, boolean fromClient) {
		super(source, fromClient);
	}
}