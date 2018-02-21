package com.vaadin.starter.bakery.testbench.elements.components;

import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.HasStringValueProperty;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("amount-field")
public class AmountFieldElement extends TestBenchElement implements HasStringValueProperty {
	public void setValue(Integer i) {
		this.setValue(i == null ? "" : i.toString());
	}

	private TextFieldElement getTextFieldElement() {
		return $(TextFieldElement.class).first();
	}

	public void click(int value) {
		if (value == 0) {
			throw new IllegalArgumentException("Value should be -1 or 1");
		}
		final int idx = value < 0 ? 0 : 1;
		getTextFieldElement().$("iron-icon").get(idx).click();
	}
}
