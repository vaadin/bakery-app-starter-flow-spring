package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.starter.bakery.testbench.elements.core.GridElement;
import com.vaadin.starter.bakery.testbench.elements.core.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-products")
public class ProductsViewElement extends TestBenchElement implements HasApp, HasCrudView {

	@Override
	public GridElement getGrid() {
		return $(GridElement.class).id("products-grid");
	}

	public TextFieldElement getName() {
		return getField("product-edit-name", TextFieldElement.class);
	};

	public TextFieldElement getPrice() {
		return getField("price", TextFieldElement.class);
	}
}
