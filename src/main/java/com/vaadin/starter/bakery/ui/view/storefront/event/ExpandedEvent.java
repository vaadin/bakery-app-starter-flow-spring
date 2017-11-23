package com.vaadin.starter.bakery.ui.view.storefront.event;

import com.vaadin.starter.bakery.ui.view.storefront.StorefrontItemDetailWrapper;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.DomEvent;

@DomEvent("expanded")
public class ExpandedEvent extends ComponentEvent<StorefrontItemDetailWrapper> {
	public ExpandedEvent(StorefrontItemDetailWrapper source, boolean fromClient) {
		super(source, fromClient);
	}
}