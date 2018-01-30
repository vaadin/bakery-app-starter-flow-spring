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
		return getOrderCard(0);
	}

	public OrderCardElement getOrderCard(int index) {
		return getGrid().$(OrderCardElement.class).all().get(index);
	}

	public OrderEditorElement getOrderEdit() {
		return $(OrderEditorElement.class).waitForFirst();
	}
}
