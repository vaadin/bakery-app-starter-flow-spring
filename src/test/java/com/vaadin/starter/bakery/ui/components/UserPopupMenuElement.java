package com.vaadin.starter.bakery.ui.components;

import com.vaadin.starter.bakery.elements.ButtonElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("user-popup-menu")
public class UserPopupMenuElement extends TestBenchElement {

	public ButtonElement getLogoutButton() {
		return $(ButtonElement.class).first();
	}
}
