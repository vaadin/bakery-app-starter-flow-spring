package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.starter.bakery.testbench.elements.components.SearchBarElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;
import org.openqa.selenium.WebDriver;

@Element("products-view")
public class ProductsViewElement extends TestBenchElement implements HasApp, HasCrudView {

	@Element("product-form")
	public static class ProductFormElement extends TestBenchElement {
	}

	@Override
	public WebDriver getRootContext() {
		return getDriver();
	}

	@Override
	public GridElement getGrid() {
		return $(GridElement.class).waitForFirst();
	}

	public TextFieldElement getPrice() {
		return getForm().$(TextFieldElement.class).id("price");
	}

	public TextFieldElement getProductName() {
		return getForm().$(TextFieldElement.class).id("name");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<ProductFormElement> getFormClass() {
		return ProductFormElement.class;
	}

	public SearchBarElement getSearchBar() {
		return $(SearchBarElement.class).first();
	}
}
