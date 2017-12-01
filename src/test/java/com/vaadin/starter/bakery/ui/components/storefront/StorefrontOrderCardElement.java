package com.vaadin.starter.bakery.ui.components.storefront;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("storefront-order-card")
public class StorefrontOrderCardElement extends TestBenchElement {

	public boolean isOrderSelected() {
		return getAttribute("selected") != null;
	}

	public OrderDetailsFullElement getDetail() {
		return $(OrderDetailsFullElement.class).first();
	}


}
