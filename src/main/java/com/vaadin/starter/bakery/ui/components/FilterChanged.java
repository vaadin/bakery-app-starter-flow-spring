package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;

@DomEvent("filter-changed")
public class FilterChanged extends ComponentEvent<SearchBar> {
	public FilterChanged(SearchBar source, boolean fromClient) {
		super(source, fromClient);
	}
}