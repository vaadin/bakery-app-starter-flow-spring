package com.vaadin.starter.bakery.testbench.elements.ui;

import org.openqa.selenium.SearchContext;

import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.testbench.TestBenchElement;

/**
 * Reads content from dialog overlay "content" property
 *
 */
public class DialogContentElement extends DialogElement {

	@Override
	public SearchContext getContext() {
		// Find child elements inside the overlay, not the dialog element
		return getOverlayContent();
	}

	/**
	 * Gets the overlay element connected to the dialog.
	 * <p>
	 * The overlay contains the content of the dialog but is not a child element of
	 * the dialog element.
	 *
	 * @return the overlay element
	 */
	private TestBenchElement getOverlayContent() {
		return getPropertyElement("$", "overlay", "content");
	}

}