package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.confirmdialog.testbench.ConfirmDialogElement;
import com.vaadin.flow.component.crud.testbench.CrudElement;
import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;
import com.vaadin.starter.bakery.testbench.elements.components.SearchBarElement;

public class BakeryCrudViewElement extends CrudElement implements HasApp {

	public SearchBarElement getSearchBar() {
		return $(SearchBarElement.class).first();
	}

	public FormLayoutElement getForm() {
		return getEditor().$(FormLayoutElement.class).first();
	}

	public ConfirmDialogElement getDiscardConfirmDialog() {
		return $(ConfirmDialogElement.class).first();
	}

	public ConfirmDialogElement getDeleteConfirmDialog() {
		return $(ConfirmDialogElement.class).last();
	}
}
