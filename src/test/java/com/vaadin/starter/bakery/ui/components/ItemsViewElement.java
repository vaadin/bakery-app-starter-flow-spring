package com.vaadin.starter.bakery.ui.components;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("items-view")
public class ItemsViewElement extends TestBenchElement {

	public ItemDetailDialogElement getEditorDialog() {
		return $(ItemDetailDialogElement.class).first();
	}

}
