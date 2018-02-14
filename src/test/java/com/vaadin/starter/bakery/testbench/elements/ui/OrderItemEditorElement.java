package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("order-item-editor")
public class OrderItemEditorElement extends TestBenchElement {

	public void clickAmountFieldPlus(int index) {
		clickAmountFieldPlusOrMinus(index, 1);
	}
	
	public void clickAmountFieldMinus(int index) {
		clickAmountFieldPlusOrMinus(index, -1);
	}
	
	private void clickAmountFieldPlusOrMinus(int index, int value) {
		if (value == 0) {
			throw new IllegalArgumentException("Value should be -1 or 1");
		}
		final String buttonId = value < 0 ? "minus" : "plus";
		$("amount-field").first().$("iron-icon").id(buttonId).click();
	}
}
