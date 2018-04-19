package com.vaadin.starter.bakery.testbench;

import static com.vaadin.starter.bakery.backend.service.UserService.MODIFY_LOCKED_USER_NOT_PERMITTED;
import static org.hamcrest.CoreMatchers.containsString;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.starter.bakery.testbench.elements.ui.StorefrontViewElement;
import com.vaadin.starter.bakery.testbench.elements.ui.UsersViewElement;
import com.vaadin.starter.bakery.ui.utils.messages.CrudErrorMessage;

public class UsersViewIT extends AbstractIT {

	private static Random r = new Random();

	private UsersViewElement openTestPage() {
		StorefrontViewElement storefront = openLoginView().login("admin@vaadin.com", "admin");
		return storefront.getMenu().navigateToUsers();
	}

	@Test
	public void updatePassword() {
		UsersViewElement usersView = openTestPage();

		Assert.assertFalse(usersView.getDialog().isPresent());

		String uniqueEmail = "e" + r.nextInt() + "@vaadin.com";

		createUser(usersView, uniqueEmail, "Paul", "Irwin", "Vaadin10", "baker");

		WebElement bakerCell = usersView.getGrid().getCell(uniqueEmail);
		Assert.assertNotNull(bakerCell);

		bakerCell.click();

		Assert.assertTrue(usersView.getDialog().get().isOpen());

		FormLayoutElement form = usersView.getForm();
		Assert.assertTrue(form.isDisplayed());

		// When opening form the password value must be always empty
		PasswordFieldElement password = usersView.getPasswordField();
		Assert.assertEquals("", password.getValue());

		// Saving any field without changing password should save and close
		TextFieldElement emailField = usersView.getEmailField();
		emailField.setValue("foo" + r.nextInt() + "@bar.com");

		usersView.getButtonsBar().getSaveButton().click();

		Assert.assertFalse(usersView.getDialog().isPresent());

		NotificationElement notification = $(NotificationElement.class).last();
		Assert.assertThat(notification.getText(), containsString("was updated"));


		// Invalid password prevents closing form
		bakerCell.click();
		emailField = usersView.getEmailField(); // Requery email field.
		password = usersView.getPasswordField(); // Requery password field.

		emailField.setValue(uniqueEmail);
		password.setValue("123");

		usersView.getButtonsBar().getSaveButton().click();
		notification = $(NotificationElement.class).last();
		Assert.assertEquals(CrudErrorMessage.REQUIRED_FIELDS_MISSING, notification.getText());

		form = usersView.getForm();
		Assert.assertTrue(form.isDisplayed());

		password = usersView.getPasswordField(); // Requery password field.

		// Good password
		password.setValue("Abc123");
		password.sendKeys(Keys.TAB);
		usersView.getButtonsBar().getSaveButton().click();
		Assert.assertFalse(usersView.getDialog().isPresent());

		// When reopening the form password field must be empty.
		bakerCell.click();
		password = usersView.getPasswordField(); // Requery password field.
		Assert.assertEquals("", password.getAttribute("value"));
	}

	private void createUser(UsersViewElement usersView, String email, String firstName, String lastName,
							String password, String role) {
		usersView.getSearchBar().getCreateNewButton().click();
		Assert.assertTrue(usersView.getDialog().get().isOpen());

		usersView.getEmailField().setValue(email);
		usersView.getFirstName().setValue(firstName);
		usersView.getLastName().setValue(lastName);
		usersView.getPasswordField().setValue(password);
		usersView.getRole().selectByText(role);

		usersView.getButtonsBar().getSaveButton().click();

		NotificationElement notification = $(NotificationElement.class).waitForFirst();
		Assert.assertThat(notification.getText(), containsString("was created"));

		Assert.assertFalse(usersView.getDialog().isPresent());
	}

	@Test
	public void tryToUpdateLockedEntity() {
		UsersViewElement page = openTestPage();

		page.getGrid().getCell("barista@vaadin.com").click();

		PasswordFieldElement field = page.getPasswordField();
		field.setValue("Abc123");
		page.getButtonsBar().getSaveButton().click();

		NotificationElement notification = $(NotificationElement.class).last();

		Assert.assertThat(notification.getText(), containsString(MODIFY_LOCKED_USER_NOT_PERMITTED));
		Assert.assertTrue(notification.isOpen());
	}

	@Test
	public void tryToDeleteLockedEntity() {
		UsersViewElement page = openTestPage();

		page.getGrid().getCell("barista@vaadin.com").click();

		page.getButtonsBar().getDeleteButton().click();
		page.getConfirmDialog().get().confirm();

		NotificationElement notification = $(NotificationElement.class).last();
		Assert.assertThat(notification.getText(), containsString(MODIFY_LOCKED_USER_NOT_PERMITTED));
		Assert.assertTrue(notification.isOpen());
	}
}
