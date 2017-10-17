package com.vaadin.starter.bakery.ui.components;

import com.vaadin.starter.bakery.elements.TextFieldElement;
import com.vaadin.starter.bakery.ui.form.EditFormElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("product-edit")
public class ProductEditElement extends TestBenchElement {

	public TextFieldElement getName() {
		return $(TextFieldElement.class).id("product-edit-name");
	};

	public TextFieldElement getPrice() {
		return $(TextFieldElement.class).id("price");
	}

	public EditFormElement getEditForm() {
		return $(EditFormElement.class).id("product-edit-form");
	}

}
