package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.dialog.testbench.DialogElement;
import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.starter.bakery.testbench.elements.components.ConfirmDialogElement;
import com.vaadin.starter.bakery.testbench.elements.components.DialogOverlayElement;
import com.vaadin.starter.bakery.testbench.elements.components.FormButtonsBarElement;
import com.vaadin.testbench.HasElementQuery;

public interface HasCrudView extends HasElementQuery {

	default GridElement getGrid() {
		return $(GridElement.class).first();
	}

	default ConfirmDialogElement getConfirmDialog() {
		return $(ConfirmDialogElement.class).onPage().first();
	}

	default DialogElement getDialog() {
		return $(DialogElement.class).first();
	}

	default FormButtonsBarElement getButtonsBar() {
		// This does not work although DialogElement changes the context to
		// the overlay in body:
		// getDialog().$(FormButtonsBarElement.class).first();
		//
		// This neither works:
		return $(DialogOverlayElement.class).onPage().first().$(FormButtonsBarElement.class).first();
	}

	default FormLayoutElement getForm() {
		return $(DialogOverlayElement.class).onPage().first().$(FormLayoutElement.class).first();
	}

}
