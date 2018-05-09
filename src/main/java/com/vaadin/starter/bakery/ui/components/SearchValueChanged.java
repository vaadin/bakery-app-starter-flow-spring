package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DebounceSettings;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.DebouncePhase;

@DomEvent(value = "value-changed", debounce = @DebounceSettings(timeout = 300, phases = DebouncePhase.TRAILING))
public class SearchValueChanged extends ComponentEvent<TextField> {
	public SearchValueChanged(TextField source, boolean fromClient) {
		super(source, fromClient);
	}
}