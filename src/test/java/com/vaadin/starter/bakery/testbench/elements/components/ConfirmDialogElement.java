package com.vaadin.starter.bakery.testbench.elements.components;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("confirm-dialog")
public class ConfirmDialogElement extends TestBenchElement {

	public void confirm() {
		callFunction("_ok");
	}
}
