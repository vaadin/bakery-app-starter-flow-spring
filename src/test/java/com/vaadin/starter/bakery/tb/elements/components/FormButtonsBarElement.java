package com.vaadin.starter.bakery.tb.elements.components;

import com.vaadin.starter.bakery.tb.elements.core.ButtonElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("form-buttons-bar")
public class FormButtonsBarElement extends TestBenchElement {

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
