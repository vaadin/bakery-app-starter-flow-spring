package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.elements.ButtonElement;

public class ShadyButtonElement extends ButtonElement {

	@Override
	public void click() {
		// Workaround for https://github.com/webcomponents/shadydom/issues/141
		callFunction("click");
	}
}
