package com.vaadin.starter.bakery.tb;

import static com.vaadin.starter.bakery.backend.service.UserService.MODIFY_LOCKED_USER_NOT_PERMITTED;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.vaadin.starter.bakery.tb.elements.core.FormLayoutElement;
import com.vaadin.starter.bakery.tb.elements.core.PaperToastElement;
import com.vaadin.starter.bakery.tb.elements.core.PasswordFieldElement;
import com.vaadin.starter.bakery.tb.elements.core.TextFieldElement;
import com.vaadin.starter.bakery.tb.elements.ui.StorefrontViewElement;
import com.vaadin.starter.bakery.tb.elements.ui.UsersViewElement;

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

		password.setValue("foobar");
		usersView.getButtonsBar().getSaveButton().click();
		Assert.assertFalse(form.isDisplayed());

		bakerCell.click();
		Assert.assertEquals("", password.getAttribute("value"));
	}

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
