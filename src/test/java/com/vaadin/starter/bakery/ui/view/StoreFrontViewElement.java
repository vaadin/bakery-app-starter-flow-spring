package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.starter.bakery.ui.components.storefront.OrderEditElement;
import com.vaadin.starter.bakery.ui.components.storefront.StoreFrontItemDetailWrapperElement;
import com.vaadin.starter.bakery.ui.components.storefront.ViewSelectorElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-storefront")
public class StoreFrontViewElement extends TestBenchElement implements HasApp, HasGrid {

	@Override
	public GridElement getGrid() {
		return $(GridElement.class).id("list");
	}

	public StoreFrontItemDetailWrapperElement getOrderDetailWrapper(int index) {
		return getGrid().$(StoreFrontItemDetailWrapperElement.class).all().get(index);
	}

	private ViewSelectorElement getOrderDialog() {
		return $(ViewSelectorElement.class).first();
	}

	public OrderEditElement getOrderEdit() {
		return getOrderDialog().$(OrderEditElement.class).waitForFirst();
	}
}
