package com.vaadin.starter.bakery.tb.elements.ui;

import com.vaadin.starter.bakery.tb.elements.core.GridElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-storefront")
public class StorefrontViewElement extends TestBenchElement implements HasApp, HasCrudView {

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
