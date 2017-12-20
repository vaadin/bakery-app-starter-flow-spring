package com.vaadin.starter.bakery.ui.components;

import com.vaadin.starter.bakery.elements.ButtonElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("buttons-bar")
public class ButtonsBarElement extends TestBenchElement {

	public ButtonElement getSaveButton() {
		return $(ButtonElement.class).id("save");
	}

	public ButtonElement getDeleteButton() {
		return $(ButtonElement.class).id("delete");
	}

	public ButtonElement getCancelButton() {
		return $(ButtonElement.class).id("cancel");
	}
}
