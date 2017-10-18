package com.vaadin.starter.bakery.ui.form;

import com.vaadin.starter.bakery.elements.ButtonElement;
import com.vaadin.starter.bakery.elements.H3Element;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("edit-form")
public class EditFormElement extends TestBenchElement {
	public H3Element getTitle() {
		return $(H3Element.class).id("title");
	}

	public ButtonElement getSaveButton() {
		return $(ButtonElement.class).id("save");
	}

	public ButtonElement getDeleteButton() {
		return $(ButtonElement.class).id("delete");
	}

	public ButtonElement getCancelButton() {
		return $(ButtonElement.class).id("cancel");
	}

}
