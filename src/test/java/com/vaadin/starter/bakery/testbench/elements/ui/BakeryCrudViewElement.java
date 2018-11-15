package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.confirmdialog.testbench.ConfirmDialogElement;
import com.vaadin.flow.component.crud.testbench.CrudElement;
import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.starter.bakery.testbench.elements.components.SearchBarElement;
import org.openqa.selenium.NoSuchElementException;

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

	public int waitAndGetCellRow(String content) {
		GridElement grid = getGrid();
		waitUntil(condition ->
				{
					try {
						return grid.getCell(content);
					} catch (NoSuchElementException e) {
						return false;
					}
				}
		);
		return grid.getCell(content).getRow();
	}
}
