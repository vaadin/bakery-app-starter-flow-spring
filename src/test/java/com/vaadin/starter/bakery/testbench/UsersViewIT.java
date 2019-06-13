package com.vaadin.starter.bakery.testbench;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import com.vaadin.testbench.addons.junit5.extensions.unittest.VaadinTest;

import com.vaadin.flow.component.tabs.testbench.TabElement;
import com.vaadin.flow.component.textfield.testbench.EmailFieldElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.starter.bakery.testbench.elements.ui.StorefrontViewElement;
import com.vaadin.starter.bakery.testbench.elements.ui.UsersViewElement;
import com.vaadin.testbench.TestBenchElement;

public class UsersViewIT extends AbstractIT<UsersViewElement> {

	private static Random r = new Random();

	@Override
	protected UsersViewElement openView() {
		StorefrontViewElement storefront = openLoginView().login("admin@vaadin.com", "admin");
		return storefront.getMenu().navigateToUsers();
	}

	@VaadinTest
	public void updatePassword() {
		UsersViewElement usersView = openView();

		Assertions.assertFalse(usersView.isEditorOpen());

		String uniqueEmail = "e" + r.nextInt() + "@vaadin.com";

		createUser(usersView, uniqueEmail, "Paul", "Irwin", "Vaadin10", "baker");

		int rowNum = usersView.getGrid().getCell(uniqueEmail).getRow();
		usersView.openRowForEditing(rowNum);

		Assertions.assertTrue(usersView.isEditorOpen());

		Assertions.assertTrue(usersView.isEditorOpen());

		// When opening form the password value must be always empty
		PasswordFieldElement password = usersView.getPasswordField();
		Assertions.assertEquals("", password.getValue());

		// Saving any field without changing password should save and close
		EmailFieldElement emailField = usersView.getEmailField();
		String newEmail = "foo" + r.nextInt() + "@bar.com";
		emailField.setValue(newEmail);

		usersView.getEditorSaveButton().click();
		Assertions.assertFalse(usersView.isEditorOpen());

		// Invalid password prevents closing form
		rowNum = usersView.getGrid().getCell(newEmail).getRow();
		usersView.openRowForEditing(rowNum);

		emailField = usersView.getEmailField(); // Requery email field.
		password = usersView.getPasswordField(); // Requery password field.

		emailField.setValue(uniqueEmail);
		password.setValue("123");

		usersView.getEditorSaveButton().click();

		Assertions.assertTrue(usersView.isEditorOpen());

		password = usersView.getPasswordField(); // Requery password field.

		// Good password
		password.setValue("Abc123");
		usersView.getEditorSaveButton().click();
		Assertions.assertFalse(usersView.isEditorOpen());

		// When reopening the form password field must be empty.
		rowNum = usersView.getGrid().getCell(uniqueEmail).getRow();
		usersView.openRowForEditing(rowNum);

		password = usersView.getPasswordField(); // Requery password field.
		Assertions.assertEquals("", password.getAttribute("value"));
	}

	private void createUser(UsersViewElement usersView, String email, String firstName, String lastName,
			String password, String role) {
		usersView.getSearchBar().getCreateNewButton().click();
		Assertions.assertTrue(usersView.isEditorOpen());

		usersView.getEmailField().setValue(email);
		usersView.getFirstName().setValue(firstName);
		usersView.getLastName().setValue(lastName);
		usersView.getPasswordField().setValue(password);

		usersView.getRole().selectByText(role);

		usersView.getEditorSaveButton().click();
		Assertions.assertFalse(usersView.isEditorOpen());
	}

	@VaadinTest
	public void tryToUpdateLockedEntity() {
		UsersViewElement page = openView();

		int rowNum = page.getGrid().getCell("barista@vaadin.com").getRow();
		page.openRowForEditing(rowNum);

		PasswordFieldElement field = page.getPasswordField();
		field.setValue("Abc123");
		page.getEmailField().setValue("barista123@vaadin.com");
		page.getEditorSaveButton().click();

		Assertions.assertEquals(rowNum, page.getGrid().getCell("barista@vaadin.com").getRow());
	}

	@VaadinTest
	public void tryToDeleteLockedEntity() {
		UsersViewElement page = openView();

		int rowNum = page.getGrid().getCell("barista@vaadin.com").getRow();
		page.openRowForEditing(rowNum);

		Assertions.assertTrue(page.isEditorOpen());

		page.getEditorDeleteButton().click();
		page.getDeleteConfirmDialog().getConfirmButton().click();

		Assertions.assertEquals(rowNum, page.getGrid().getCell("barista@vaadin.com").getRow());
	}

	@VaadinTest
	public void testCancelConfirmationMessage() {
		UsersViewElement page = openView();
		page.getSearchBar().getCreateNewButton().click();
		page.getFirstName().setValue("Some name");
		page.getEditorCancelButton().click();

		Assertions.assertEquals(page.getDiscardConfirmDialog().getHeaderText(), "Discard changes");
	}

	@VaadinTest
	public void accessDenied() {
		StorefrontViewElement storefront = openLoginView().login("barista@vaadin.com", "barista");
		Assertions.assertEquals(3, storefront.getMenu().$(TabElement.class).all().size());

		getDriver().get(APP_URL + "users");
		TestBenchElement accessDeniedPage = $("access-denied-view").waitForFirst();

		Assertions.assertEquals("Access denied", accessDeniedPage.$("h3").first().getText());
	}
}
