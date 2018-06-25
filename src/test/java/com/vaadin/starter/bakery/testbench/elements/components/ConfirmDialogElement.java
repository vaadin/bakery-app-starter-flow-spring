package com.vaadin.starter.bakery.testbench.elements.components;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("vaadin-confirm-dialog")
public class ConfirmDialogElement extends TestBenchElement {

	private TestBenchElement getOverlayContext() {
		return $("vaadin-dialog-overlay").onPage().last().$(TestBenchElement.class).id("content");
	}

	public void confirm() {
		getOverlayContext().$(ButtonElement.class).id("confirm").click();
	}

	public String getMessage() {
		return getOverlayContext().$(TestBenchElement.class).attribute("part", "message").first().getText();
	}
}
