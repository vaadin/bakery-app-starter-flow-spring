package com.vaadin.starter.bakery.ui.components.storefront;

import com.vaadin.starter.bakery.elements.ButtonElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("storefront-item-detail")
public class StoreFrontItemDetailElement extends TestBenchElement {

	public ButtonElement getCancelButton() {
		return $(ButtonElement.class).id("collapse-order-detail");
	}

	public ButtonElement getEditButton() {
		return $(ButtonElement.class).id("edit-order-detail");
	}

}
