package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.starter.bakery.testbench.elements.components.AmountFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("order-item-editor")
public class OrderItemEditorElement extends TestBenchElement {

	public AmountFieldElement getAmountField() {
		return $(AmountFieldElement.class).first();
	}

	public void clickAmountFieldMinus() {
		getAmountField().click(-1);
	}

	public void clickAmountFieldPlus() {
		getAmountField().click(1);
	}
}
