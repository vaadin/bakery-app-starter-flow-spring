package com.vaadin.starter.bakery.testbench.elements.components;

import com.vaadin.starter.bakery.testbench.elements.core.ButtonElement;

public class ShadyButtonElement extends ButtonElement {

	@Override
	public void click() {
		callFunction("click");
	}
}
