package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.starter.bakery.ui.components.storefront.OrderEditorElement;
import com.vaadin.starter.bakery.ui.components.storefront.StorefrontOrderCardElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-storefront")
public class StorefrontViewElement extends TestBenchElement implements HasApp, HasGrid {

	@Override
	public GridElement getGrid() {
		return $(GridElement.class).waitForFirst();
	}

	public StorefrontOrderCardElement getFirstOrderCard() {
		return getGrid().$(StorefrontOrderCardElement.class).all().get(0);
	}

	public OrderEditorElement getOrderEdit() {
		return $(OrderEditorElement.class).waitForFirst();
	}
}
