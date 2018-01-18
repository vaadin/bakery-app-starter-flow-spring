package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.starter.bakery.testbench.elements.components.BakeryNavigationElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-app")
public class BakeryAppElement extends TestBenchElement {

	public BakeryNavigationElement getMenu() {
		return $(BakeryNavigationElement.class).first();
	}

}
