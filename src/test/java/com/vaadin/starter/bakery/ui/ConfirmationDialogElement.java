package com.vaadin.starter.bakery.ui;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("starter-confirm-dialog")
public class ConfirmationDialogElement extends TestBenchElement {

	public void confirm() {
		executeScript("arguments[0]._ok()", this);
	}
}
