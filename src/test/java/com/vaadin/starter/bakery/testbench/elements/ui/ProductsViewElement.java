package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("products-view")
public class ProductsViewElement extends TestBenchElement implements HasApp, HasCrudView {

	@Element("product-form")
	public static class ProductFormElement extends TestBenchElement {
	}

	@Override
	public GridElement getGrid() {
		return $(GridElement.class).waitForFirst();
	}

	public TextFieldElement getPrice() {
		return getForm().$(TextFieldElement.class).id("price");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<ProductFormElement> getFormClass() {
		return ProductFormElement.class;
	}
}
