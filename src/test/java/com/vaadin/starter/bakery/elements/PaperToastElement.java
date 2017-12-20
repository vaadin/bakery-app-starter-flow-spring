package com.vaadin.starter.bakery.elements;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("paper-toast")
public class PaperToastElement extends TestBenchElement {

	@Override
	public String getText() {
		return getPropertyString("text");
	}

	public Boolean isOpened() {
		return getPropertyBoolean("opened");
	}
}
