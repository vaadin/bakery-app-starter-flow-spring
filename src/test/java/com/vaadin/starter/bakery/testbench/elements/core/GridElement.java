package com.vaadin.starter.bakery.testbench.elements.core;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("vaadin-grid")
public class GridElement extends TestBenchElement {

	public int getGridSize() {
		String size = getPropertyString("size");
		try {
			return Integer.parseInt(size);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}
