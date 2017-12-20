package com.vaadin.starter.bakery.ui;

import org.openqa.selenium.By;

import com.vaadin.starter.bakery.elements.DialogOverlayElement;
import com.vaadin.starter.bakery.elements.FormLayoutElement;
import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.starter.bakery.elements.PasswordFieldElement;
import com.vaadin.starter.bakery.elements.TextFieldElement;
import com.vaadin.starter.bakery.ui.components.ButtonsBarElement;
import com.vaadin.starter.bakery.ui.components.ItemsViewElement;
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

	public ConfirmDialogElement getConfirmDialog() {
		return $(ConfirmDialogElement.class).onPage().first();
	}

	private DialogOverlayElement getVaadinOverlay() {
		return $(DialogOverlayElement.class).onPage().first();
	}

	public ButtonsBarElement getButtonsBar() {
		return getVaadinOverlay().$(ButtonsBarElement.class).first();
	}

	private FormLayoutElement getUserEditForm() {
		return getVaadinOverlay().$(FormLayoutElement.class).first();
	}

	public TextFieldElement getFirstField() {
		return getUserEditForm().findElement(By.id("first")).wrap(TextFieldElement.class);
	};

	public TextFieldElement getLastField() {
		return getUserEditForm().findElement(By.id("last")).wrap(TextFieldElement.class);
	}

	public TextFieldElement getEmailField() {
		return getUserEditForm().findElement(By.id("email")).wrap(TextFieldElement.class);
	}

	public PasswordFieldElement getPasswordField() {
		return getUserEditForm().findElement(By.id("user-edit-password")).wrap(PasswordFieldElement.class);
	}
}
