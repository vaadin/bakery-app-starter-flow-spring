package com.vaadin.starter.bakery.testbench.elements.components;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("order-card")
public class OrderCardElement extends TestBenchElement {

	public boolean isOrderSelected() {
		return hasAttribute("selected");
	}

	public OrderDetailsFullElement getDetail() {
		return $(OrderDetailsFullElement.class).first();
	}

}
