package com.vaadin.starter.bakery.elements;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

/**
 * Element object for {@code <vaadin-text-field>}.
 */
@Element("vaadin-text-field")
public class TextFieldElement extends TestBenchElement {

	public String getLabel() {
		return getPropertyString("label");
	}

	public void setValue(String value) {
		setProperty("value", value);
	}

	public String getValue() {
		return getPropertyString("value");
	}

}
