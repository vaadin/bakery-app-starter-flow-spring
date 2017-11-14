package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.starter.bakery.ui.components.storefront.OrderEditElement;
import com.vaadin.starter.bakery.ui.components.storefront.StoreFrontItemDetailWrapperElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-storefront")
public class StoreFrontViewElement extends TestBenchElement implements HasApp, HasGrid {

	@Override
	public GridElement getGrid() {
		return $(GridElement.class).onPage().first();
	}

	public StoreFrontItemDetailWrapperElement getFirstOrderDetailWrapper() {
		// vaadin-grid does not yet have a public API to get the currently displayed items
		// (see https://github.com/vaadin/vaadin-grid/issues/1054)
		// Using a hack here until there is a better API - BFF-359.
		return getGrid().$(StoreFrontItemDetailWrapperElement.class).all().get(0);
	}

	public OrderEditElement getOrderEdit() {
		return $(OrderEditElement.class).waitForFirst();
	}
}
