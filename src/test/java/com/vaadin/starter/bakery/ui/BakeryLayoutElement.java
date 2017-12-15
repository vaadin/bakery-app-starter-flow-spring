package com.vaadin.starter.bakery.ui;

import com.vaadin.starter.bakery.ui.view.BakeryNavigationElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-layout")
public class BakeryLayoutElement extends TestBenchElement {

	public BakeryNavigationElement getMenu() {
		return $(BakeryNavigationElement.class).first();
	}

}
