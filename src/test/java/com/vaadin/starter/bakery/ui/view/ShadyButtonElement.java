package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.elements.ButtonElement;

public class ShadyButtonElement extends ButtonElement {

	@Override
	public void click() {
		callFunction("click");
	}
}
