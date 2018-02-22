package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("order-item-editor")
public class OrderItemEditorElement extends TestBenchElement {

	public void clickAmountFieldPlus() {
		clickAmountFieldPlusOrMinus(1);
	}
	
	public void clickAmountFieldMinus() {
		clickAmountFieldPlusOrMinus(-1);
	}
	
	private void clickAmountFieldPlusOrMinus(int value) {
		if (value == 0) {
			throw new IllegalArgumentException("Value should be -1 or 1");
		}
		final int idx = value < 0 ? 0 : 1;
		$("amount-field").first().$("iron-icon").get(idx).click();
	}
}
