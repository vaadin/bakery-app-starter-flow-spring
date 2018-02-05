package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.starter.bakery.testbench.elements.components.ConfirmDialogElement;
import com.vaadin.starter.bakery.testbench.elements.components.FormButtonsBarElement;
import com.vaadin.starter.bakery.testbench.elements.core.FormDialogElement;
import com.vaadin.testbench.HasElementQuery;

public interface HasCrudView extends HasElementQuery {

	default GridElement getGrid() {
		return $(GridElement.class).first();
	}

	default ConfirmDialogElement getConfirmDialog() {
		return $(ConfirmDialogElement.class).onPage().first();
	}

	default FormDialogElement getFormDialog() {
		return $(FormDialogElement.class).first();
	}

	default FormButtonsBarElement getButtonsBar() {
		return getFormDialog().$(FormButtonsBarElement.class).first();
	}

	default FormLayoutElement getForm() {
		return getFormDialog().$(FormLayoutElement.class).first();
	}

}
