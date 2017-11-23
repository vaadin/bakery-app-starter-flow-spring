package com.vaadin.starter.bakery.ui.components;

import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.DomEvent;

@DomEvent("filter-changed")
public class FilterChanged extends ComponentEvent<BakerySearch> {
	public FilterChanged(BakerySearch source, boolean fromClient) {
		super(source, fromClient);
	}
}