package com.vaadin.starter.bakery.testbench;

import static com.vaadin.starter.bakery.backend.service.UserService.MODIFY_LOCKED_USER_NOT_PERMITTED;
import static com.vaadin.starter.bakery.ui.view.EntityPresenter.REQUIRED_MESSAGE;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.vaadin.starter.bakery.testbench.elements.core.FormLayoutElement;
import com.vaadin.starter.bakery.testbench.elements.core.PaperToastElement;
import com.vaadin.starter.bakery.testbench.elements.core.PasswordFieldElement;
import com.vaadin.starter.bakery.testbench.elements.ui.StorefrontViewElement;
import com.vaadin.starter.bakery.testbench.elements.ui.UsersViewElement;

public class UsersViewIT extends AbstractIT {

	private UsersViewElement openTestPage() {
		StorefrontViewElement storefront = openLoginView().login("admin@vaadin.com", "admin");
		return storefront.getMenu().navigateToUsers();
	}

	@Test
	public void updatePassword() {
		UsersViewElement usersView = openTestPage();

		Assert.assertFalse(usersView.getDialog().isOpened());

		WebElement bakerCell = usersView.getGridCell("baker@vaadin.com");
		Assert.assertNotNull(bakerCell);

		bakerCell.click();

		Assert.assertTrue(usersView.getDialog().isOpened());

		FormLayoutElement form = usersView.getForm();
		Assert.assertTrue(form.isDisplayed());

		PasswordFieldElement password = usersView.getPasswordField();
		Assert.assertEquals("", password.getValue());

		// Invalid password prevents closing form
		password.setValue("123");
		usersView.getButtonsBar().getSaveButton().click();
		PaperToastElement toast = $(PaperToastElement.class).onPage().id("_defaultToast");
		Assert.assertTrue(form.isDisplayed());
		Assert.assertEquals(REQUIRED_MESSAGE, toast.getText());

		// Good password
		password.setValue("Abc123");
		usersView.getButtonsBar().getSaveButton().click();
		Assert.assertFalse(form.isDisplayed());

		// Do not display any password when opening the form
		bakerCell.click();
		Assert.assertEquals("", password.getAttribute("value"));
	}

	@Test
	public void tryToUpdateLockedEntity() {
		UsersViewElement page = openTestPage();

		page.getGridCell("barista@vaadin.com").click();

		PasswordFieldElement field = page.getPasswordField();
		field.setValue("Abc123");
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
