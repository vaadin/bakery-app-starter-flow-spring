package com.vaadin.starter.bakery.testbench.elements.components;

import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("confirm-dialog")
public class ConfirmDialogElement extends DialogElement {

	public void confirm() {
		callFunction("_ok");
	}
}
