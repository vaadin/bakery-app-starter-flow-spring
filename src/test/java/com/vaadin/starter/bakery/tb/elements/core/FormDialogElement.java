package com.vaadin.starter.bakery.tb.elements.core;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("form-dialog")
public class FormDialogElement extends TestBenchElement {
	public Boolean isOpened() {
		return getPropertyBoolean("opened");
	}
}
