package com.vaadin.starter.bakery.tb.elements.components;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("confirm-dialog")
public class ConfirmDialogElement extends TestBenchElement {

	public void confirm() {
		executeScript("arguments[0]._ok()", this);
	}
}
