package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class ProductsViewElement extends BakeryCrudViewElement {

	public TextFieldElement getProductName() {
		return getCrud().getEditor().$(FormLayoutElement.class).first().$(TextFieldElement.class).first();
	}

	public TextFieldElement getPrice() {
		return getCrud().getEditor().$(FormLayoutElement.class).first().$(TextFieldElement.class).all().get(1);
	}

	public void openRowForEditing(int row) {
		openRowForEditing(row, 2);
	}
}
