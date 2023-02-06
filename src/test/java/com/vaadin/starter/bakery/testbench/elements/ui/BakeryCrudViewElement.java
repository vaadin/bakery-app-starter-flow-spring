package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.confirmdialog.testbench.ConfirmDialogElement;
import com.vaadin.flow.component.crud.testbench.CrudElement;
import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;
import com.vaadin.starter.bakery.testbench.elements.components.SearchBarElement;
import com.vaadin.testbench.TestBenchElement;

public class BakeryCrudViewElement extends TestBenchElement implements HasApp {

	public SearchBarElement getSearchBar() {
		return $(SearchBarElement.class).first();
	}

	public CrudElement getCrud() {
		return $(CrudElement.class).first();
	}

	public FormLayoutElement getForm() {
		return getCrud().getEditor().$(FormLayoutElement.class).first();
	}

	public ConfirmDialogElement getDiscardConfirmDialog() {
		return getCrud().$(ConfirmDialogElement.class).first();
	}

	public ConfirmDialogElement getDeleteConfirmDialog() {
		return getCrud().$(ConfirmDialogElement.class).last();
	}

	public void openRowForEditing(int row, int editCol) {
		getCrud().getGrid().getCell(row, editCol).$(TestBenchElement.class).first().click();
	}
}
