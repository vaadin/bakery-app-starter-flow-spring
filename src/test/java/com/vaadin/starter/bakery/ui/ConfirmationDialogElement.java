package com.vaadin.starter.bakery.ui;

import com.vaadin.starter.bakery.elements.ButtonElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("confirm-dialog")
public class ConfirmationDialogElement extends TestBenchElement {

	public void confirm() {
		getOkButton().click();
	}

	public ButtonElement getOkButton() {
		return $(ButtonElement.class).id("ok");
	}
}
