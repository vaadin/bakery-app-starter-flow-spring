package com.vaadin.starter.bakery.ui;

import static com.vaadin.starter.bakery.backend.service.UserService.MODIFY_LOCKED_USER_NOT_PERMITTED;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.starter.bakery.AbstractIT;
import com.vaadin.starter.bakery.elements.PaperToastElement;
import com.vaadin.starter.bakery.elements.TextFieldElement;
import com.vaadin.starter.bakery.ui.view.StorefrontViewElement;

public class UsersViewIT extends AbstractIT {

	private UsersViewElement openTestPage() {
		StorefrontViewElement storefront = openLoginView().login("admin@vaadin.com", "admin");
		return storefront.getMenu().navigateToUsers();
	}

	// @Test
	// public void updatePassword() {
	// UsersViewElement usersView = openTestPage();
	//
	// ItemDetailDialogElement editor =
	// usersView.getItemsView().getEditorDialog();
	// Assert.assertFalse(editor.isDisplayed());
	//
	// WebElement bakerCell = usersView.getGridCell("baker@vaadin.com");
	// Assert.assertNotNull(bakerCell);
	//
	// bakerCell.click();
	// Assert.assertTrue(editor.isDisplayed());
	//
	// PasswordFieldElement password =
	// usersView.getUserEdit().getPasswordField();
	// Assert.assertEquals("", password.getValue());
	//
	// password.setValue("foobar");
	// usersView.getUserEdit().getEditForm().getSaveButton().click();
	// Assert.assertFalse(editor.isDisplayed());
	//
	// bakerCell.click();
	// Assert.assertEquals("", password.getAttribute("value"));
	// }

	@Test
	public void tryToUpdateLockedEntity() {
		UsersViewElement page = openTestPage();

		page.getGridCell("barista@vaadin.com").click();

		TextFieldElement field = page.getFirstField();
		field.setValue(field.getValue() + "-updated");
		page.getButtonsBar().getSaveButton().click();

		PaperToastElement toast = $(PaperToastElement.class).onPage().id("_persistentToast");
		Assert.assertEquals(MODIFY_LOCKED_USER_NOT_PERMITTED, toast.getText());
		Assert.assertTrue(toast.isOpened());
	}

	@Test
	public void tryToDeleteLockedEntity() {
		UsersViewElement page = openTestPage();

		page.getGridCell("barista@vaadin.com").click();

		page.getButtonsBar().getDeleteButton().click();
		page.getConfirmDialog().confirm();

		PaperToastElement toast = $(PaperToastElement.class).onPage().id("_persistentToast");
		Assert.assertEquals(MODIFY_LOCKED_USER_NOT_PERMITTED, toast.getText());
		Assert.assertTrue(toast.isOpened());
	}
}
