package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.starter.bakery.testbench.elements.core.GridElement;
import com.vaadin.starter.bakery.testbench.elements.core.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("products-view")
public class ProductsViewElement extends TestBenchElement implements HasApp, HasCrudView {

	@Override
	public GridElement getGrid() {
		return $(GridElement.class).waitForFirst();
	}

	public TextFieldElement getName() {
		return getField("name", TextFieldElement.class);
	};

	public TextFieldElement getPrice() {
		return getField("price", TextFieldElement.class);
	}
}
