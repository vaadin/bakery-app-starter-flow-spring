package com.vaadin.starter.bakery.testbench.elements.components;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("order-details-full")
public class OrderDetailsFullElement extends TestBenchElement {

	public ButtonElement getEditButton() {
		return $(ButtonElement.class).id("edit");
	}

	public ButtonElement getSaveButton() {
		return $(ButtonElement.class).id("save");
	}

}
