package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;

public class TextFieldWithPrefix extends TextField {

	@Override
	public TextField addToPrefix(Component... components) {
		return super.addToPrefix(components);
	}
}
