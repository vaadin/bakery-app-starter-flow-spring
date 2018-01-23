package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.starter.bakery.testbench.elements.components.OrderEditorElement;
import com.vaadin.starter.bakery.testbench.elements.components.OrderCardElement;
import com.vaadin.starter.bakery.testbench.elements.core.GridElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("storefront-view")
public class StorefrontViewElement extends TestBenchElement implements HasApp, HasCrudView {

	@Override
	public GridElement getGrid() {
		return $(GridElement.class).waitForFirst();
	}

	public OrderCardElement getFirstOrderCard() {
		return getGrid().$(OrderCardElement.class).all().get(0);
	}

	public OrderEditorElement getOrderEdit() {
		return $(OrderEditorElement.class).waitForFirst();
	}
}
