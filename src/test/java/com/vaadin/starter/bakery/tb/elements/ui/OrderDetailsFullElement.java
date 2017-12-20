package com.vaadin.starter.bakery.tb.elements.ui;

import com.vaadin.starter.bakery.tb.elements.core.ButtonElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("order-details-full")
public class OrderDetailsFullElement extends TestBenchElement {

	public ButtonElement getCancelButton() {
		return $(ButtonElement.class).id("collapse-order-detail");
	}

	public ButtonElement getEditButton() {
		return $(ButtonElement.class).id("edit-order-detail");
	}

}
