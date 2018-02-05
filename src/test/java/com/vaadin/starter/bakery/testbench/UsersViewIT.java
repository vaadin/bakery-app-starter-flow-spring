package com.vaadin.starter.bakery.testbench;

import static com.vaadin.starter.bakery.backend.service.UserService.MODIFY_LOCKED_USER_NOT_PERMITTED;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.starter.bakery.testbench.elements.core.PaperToastElement;
import com.vaadin.starter.bakery.testbench.elements.ui.StorefrontViewElement;
import com.vaadin.starter.bakery.testbench.elements.ui.UsersViewElement;
import com.vaadin.starter.bakery.ui.utils.messages.CrudErrorMessage;

public class UsersViewIT extends AbstractIT {

	private UsersViewElement openTestPage() {
		StorefrontViewElement storefront = openLoginView().login("admin@vaadin.com", "admin");
		return storefront.getMenu().navigateToUsers();
	}

	@Test
	public void updatePassword() throws InterruptedException {
		UsersViewElement usersView = openTestPage();

		Assert.assertFalse(usersView.getFormDialog().isOpen());

		WebElement bakerCell = usersView.getGrid().getCell("baker@vaadin.com");
		Assert.assertNotNull(bakerCell);

		bakerCell.click();

		Assert.assertTrue(usersView.getFormDialog().isOpen());

		FormLayoutElement form = usersView.getForm();
		Assert.assertTrue(form.isDisplayed());

		// When opening form the password value must be always empty
		PasswordFieldElement password = usersView.getPasswordField();
		Assert.assertEquals("", password.getValue());

		// Saving any field without changing password should save and close
		TextFieldElement emailField = usersView.getEmailField();
		emailField.setValue("foo@bar.com");
		usersView.getButtonsBar().getSaveButton().click();
		Assert.assertFalse(form.isDisplayed());

		// Invalid password prevents closing form
		bakerCell.click();
		emailField.setValue("baker@vaadin.com");
		password.setValue("123");
		usersView.getButtonsBar().getSaveButton().click();
		PaperToastElement toast = $(PaperToastElement.class).onPage().id("_defaultToast");
		Assert.assertTrue(form.isDisplayed());
		Assert.assertEquals(CrudErrorMessage.REQUIRED_FIELDS_MISSING, toast.getMessage());

		// Good password
		password.setValue("Abc123");
		usersView.getButtonsBar().getSaveButton().click();
		Assert.assertFalse(form.isDisplayed());

		// When reopening the form password field must be empty.
		bakerCell.click();
		Assert.assertEquals("", password.getAttribute("value"));
	}

	@Test
	public void tryToUpdateLockedEntity() {
		UsersViewElement page = openTestPage();

		page.getGrid().getCell("barista@vaadin.com").click();

		PasswordFieldElement field = page.getPasswordField();
		field.setValue("Abc123");
		page.getButtonsBar().getSaveButton().click();

		PaperToastElement toast = $(PaperToastElement.class).onPage().id("_persistentToast");

		Assert.assertEquals(MODIFY_LOCKED_USER_NOT_PERMITTED, toast.getMessage());
		Assert.assertTrue(toast.isOpened());
	}

	@Test
	public void tryToDeleteLockedEntity() {
		UsersViewElement page = openTestPage();

		page.getGrid().getCell("barista@vaadin.com").click();

		page.getButtonsBar().getDeleteButton().click();
		page.getConfirmDialog().confirm();

		PaperToastElement toast = $(PaperToastElement.class).onPage().id("_persistentToast");
		Assert.assertEquals(MODIFY_LOCKED_USER_NOT_PERMITTED, toast.getMessage());
		Assert.assertTrue(toast.isOpened());
	}
}
