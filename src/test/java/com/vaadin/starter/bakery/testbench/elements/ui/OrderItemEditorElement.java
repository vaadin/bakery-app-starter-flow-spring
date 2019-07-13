package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("order-item-editor")
public class OrderItemEditorElement extends TestBenchElement {

	public void clickNumberFieldPlus() {
		clickNumberFieldPlusOrMinus("increase-button");
	}
	
	public void clickNumberFieldMinus() {
		clickNumberFieldPlusOrMinus("decrease-button");
	}
	
	public TextFieldElement getCommentField() {
		return $(TextFieldElement.class).id("comment");
	}
	
	private void clickNumberFieldPlusOrMinus(String selector) {
		$("vaadin-number-field").first().$("div[part=" + selector + "]").first().click();
	}
}
