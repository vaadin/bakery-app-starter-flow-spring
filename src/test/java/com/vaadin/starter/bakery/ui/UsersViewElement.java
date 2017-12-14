package com.vaadin.starter.bakery.ui;

import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.starter.bakery.ui.components.ItemsViewElement;
import com.vaadin.starter.bakery.ui.components.UserEditElement;
import com.vaadin.starter.bakery.ui.view.HasGrid;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-users")
public class UsersViewElement extends TestBenchElement implements HasGrid {

	@Override
	public GridElement getGrid() {
		return $(GridElement.class).id("users-grid");
	}

	public ItemsViewElement getItemsView() {
		return $(ItemsViewElement.class).first();
	}

	public UserEditElement getUserEdit() {
		return $(UserEditElement.class).first();
	}

	public ConfirmationDialogElement getConfirmDialog() {
		return $(ConfirmationDialogElement.class).onPage().first();
	}
}
