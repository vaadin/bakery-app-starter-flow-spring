package com.vaadin.starter.bakery.testbench.elements.core;

import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("paper-toast")
public class PaperToastElement extends TestBenchElement {

	public String getMessage() {
		waitUntil(ExpectedConditions.attributeToBeNotEmpty(this, "text"));
		return getPropertyString("text");
	}
	
	public Boolean isOpened() {
		return getPropertyBoolean("opened");
	}
}
