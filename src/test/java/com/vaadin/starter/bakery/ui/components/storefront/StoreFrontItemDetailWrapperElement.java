package com.vaadin.starter.bakery.ui.components.storefront;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("storefront-item-detail-wrapper")
public class StoreFrontItemDetailWrapperElement extends TestBenchElement {

	public boolean isOrderSelected() {
		return getAttribute("selected") != null;
	}

	public OrderDetailElement getDetail() {
		return $(OrderDetailElement.class).first();
	}


}
