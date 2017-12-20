package com.vaadin.starter.bakery.tb.elements.ui;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-app")
public class BakeryAppElement extends TestBenchElement {

	public BakeryNavigationElement getMenu() {
		return $(BakeryNavigationElement.class).first();
	}

}
