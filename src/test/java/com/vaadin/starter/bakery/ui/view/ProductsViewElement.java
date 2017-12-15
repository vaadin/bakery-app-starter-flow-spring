package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.starter.bakery.ui.components.ItemsViewElement;
import com.vaadin.starter.bakery.ui.components.ProductEditElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-products")
public class ProductsViewElement extends TestBenchElement implements HasBakeryLayout, HasGrid {

	@Override
	public GridElement getGrid() {
		return $(GridElement.class).id("products-grid");
	}

	public ItemsViewElement getItemsView() {
		return $(ItemsViewElement.class).first();
	}

	public ProductEditElement getProductEdit() {
		return $(ProductEditElement.class).first();
	}
}
