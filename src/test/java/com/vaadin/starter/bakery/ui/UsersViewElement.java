package com.vaadin.starter.bakery.ui;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.starter.bakery.ui.components.ItemsViewElement;
import com.vaadin.starter.bakery.ui.components.UserEditElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("bakery-users")
public class UsersViewElement extends TestBenchElement {

	public GridElement getGrid() {
		return $(GridElement.class).id("grid");
	}

	public List<WebElement> getGridCells() {
		return getGrid().findElements(By.cssSelector("vaadin-grid-cell-content"));
	}

	public ItemsViewElement getItemsView() {
		return $(ItemsViewElement.class).first();
	}

	public UserEditElement getUserEdit() {
		return $(UserEditElement.class).first();
	}

	public WebElement getGridCell(String text) {
		return getGridCells().stream().filter(cell -> cell.getText().equals(text)).findFirst().orElse(null);
	}

	public ConfirmationDialogElement getConfirmDialog() {
		return $(ConfirmationDialogElement.class).id("user-confirmation-dialog");
	}
}
