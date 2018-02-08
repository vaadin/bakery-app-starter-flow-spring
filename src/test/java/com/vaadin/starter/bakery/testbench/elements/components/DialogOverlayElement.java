package com.vaadin.starter.bakery.testbench.elements.components;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("vaadin-dialog-overlay")
public class DialogOverlayElement extends TestBenchElement {

	@Element("flow-component-renderer")
	public static class FlowComponentRendererElement extends TestBenchElement {
	}

	@Element("product-form")
	public static class ProductFormElement extends TestBenchElement {
	}
}
