package com.vaadin.starter.bakery.testbench.elements.components;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("order-card")
public class StorefrontOrderCardElement extends TestBenchElement {

	public boolean isOrderSelected() {
		return getAttribute("selected") != null;
	}

	public OrderDetailsFullElement getDetail() {
		return $(OrderDetailsFullElement.class).first();
	}


}
