package com.vaadin.starter.bakery.tb.elements.ui;

import com.vaadin.starter.bakery.tb.elements.core.ButtonElement;

public class ShadyButtonElement extends ButtonElement {

	@Override
	public void click() {
		callFunction("click");
	}
}
