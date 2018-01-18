package com.vaadin.starter.bakery.testbench.elements.components;

import com.vaadin.starter.bakery.testbench.elements.core.ButtonElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("order-details-full")
public class OrderDetailsFullElement extends TestBenchElement {

	public ButtonElement getCancelButton() {
		return $(ButtonElement.class).id("collapse-order-detail");
	}

	public ButtonElement getEditButton() {
		return $(ButtonElement.class).id("edit");
	}

}
